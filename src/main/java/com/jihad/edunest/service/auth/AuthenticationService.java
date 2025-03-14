package com.jihad.edunest.service.auth;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.enums.UserRole;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.web.vms.request.auth.AuthenticationRequest;
import com.jihad.edunest.web.vms.request.auth.RegisterRequest;
import com.jihad.edunest.web.vms.responce.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Générer un token de vérification
        String verificationToken = UUID.randomUUID().toString();

        var user = Member.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.MEMBER) // Par défaut, un nouvel utilisateur est un MEMBER
                .active(true)
                .verified(false)
                .verificationToken(verificationToken)
                .build();

        memberRepository.save(user);

        // Envoyer l'email de vérification
        emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        verifyAccount(verificationToken);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.getVerified()) {
            throw new RuntimeException("Compte non vérifié. Veuillez vérifier votre email.");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void verifyAccount(String token) {
        var user = memberRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        user.setVerified(true);
        user.setVerificationToken(null);
        memberRepository.save(user);
    }

    @Transactional
    public void initiatePasswordReset(String email) {
        var user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email non trouvé"));

        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(24));
        memberRepository.save(user);

        // Envoyer l'email de réinitialisation
        emailService.sendPasswordResetEmail(email, resetToken);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        var user = memberRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide"));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Le token a expiré");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        memberRepository.save(user);
    }
}
