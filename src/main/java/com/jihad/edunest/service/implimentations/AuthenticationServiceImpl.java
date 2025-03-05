package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.enums.UserRole;
import com.jihad.edunest.exception.user.UserNameAlreadyExistsException;
import com.jihad.edunest.exception.user.UserNotFoundException;
import com.jihad.edunest.exception.user.UsernameOrPasswordInvalidException;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.service.AuthService;
import com.jihad.edunest.web.vms.mapper.UserVMMapper;
import com.jihad.edunest.web.vms.request.RegisterVM;
import com.jihad.edunest.web.vms.responce.TokenVM;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthService {
    private final MemberRepository userRepository;
    private final JwtService jwtService;
    private final UserVMMapper userVMMapper;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public TokenVM register(@Valid RegisterVM registerVM, String clientOrigin) {

        userService.findByUsername(registerVM.getUsername())
                .ifPresent(existingUser -> {
                    throw new UserNameAlreadyExistsException("Username already exists");
                });

        userService.findByEmail(registerVM.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserNameAlreadyExistsException("Email already exists");
                });

        Member newUser = userVMMapper.registerVMtoUser(registerVM);


        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(UserRole.MEMBER);
        newUser.setVerificationToken(generateVerificationToken());

        Member savedUser = userRepository.save(newUser);

        String authToken = jwtService.generateToken(savedUser.getUsername());
        String refreshToken = jwtService.generateRefreshToken(savedUser.getUsername());

        emailService.sendVerificationEmail(newUser.getEmail(), newUser.getVerificationToken(), clientOrigin);

        return TokenVM.builder().token(authToken).refreshToken(refreshToken).build();
    }


    public TokenVM login(String username, String password) {

        Optional<Member> opUser;
        if (isEmail(username)) opUser = userRepository.findByEmailAndDeletedFalse(username);
        else opUser = userRepository.findByUsernameAndDeletedFalse(username);

        return opUser.filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(authenticatedUser -> {

                    String authToken = jwtService.generateToken(authenticatedUser.getUsername());
                    String refreshToken = jwtService.generateRefreshToken(authenticatedUser.getUsername());
                    return TokenVM.builder()
                            .token(authToken)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new UsernameOrPasswordInvalidException("Invalid credentials."));
    }


    public TokenVM refresh(String refreshToken) {

        if(jwtService.isTokenExpired(refreshToken)) {
            throw new ExpiredJwtException(null, null, "Refresh token has expired");
        }
        String username = jwtService.extractUsername(refreshToken);
        userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!jwtService.isTokenValid(refreshToken,username )) {
            throw new UsernameOrPasswordInvalidException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(username);


        return new TokenVM(newAccessToken, refreshToken);
    }



    public void verifyEmail(String token) {
        Member user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token."));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

    }

    public void forgotPassword(String email, String clientOrigin) {
        Member user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        String resetToken = generatePasswordResetToken();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        emailService.sendPasswordResetEmail(user.getEmail(), resetToken,clientOrigin);
    }

    public void resetPassword(String token, String newPassword) {
        Member user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token."));

        if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token has expired.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);

    }
//--------------------------- helper methods ------------------------------

    public String generateVerificationToken() {
        String token = UUID.randomUUID().toString();

        List<Member> user = userRepository.findAllByVerificationToken(token);

        if(!user.isEmpty()) return generateVerificationToken();

        return token;
    }

    public String generatePasswordResetToken() {
        String token = UUID.randomUUID().toString();

        List<Member>  user = userRepository.findAllByPasswordResetToken(token);

        if(!user.isEmpty()) return generatePasswordResetToken();

        return token;
    }

    private boolean isEmail(String input) {
        return input != null && input.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
