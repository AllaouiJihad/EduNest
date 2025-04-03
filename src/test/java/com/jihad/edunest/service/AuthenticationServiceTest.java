package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.enums.UserRole;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.service.auth.AuthenticationService;
import com.jihad.edunest.service.auth.EmailService;
import com.jihad.edunest.service.auth.JwtService;
import com.jihad.edunest.web.vms.request.auth.AuthenticationRequest;
import com.jihad.edunest.web.vms.request.auth.RegisterRequest;
import com.jihad.edunest.web.vms.responce.auth.AuthenticationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private Member member;
    private AuthenticationRequest authRequest;
    private String validToken;

    @BeforeEach
    void setUp() {
        // Préparer les données de test
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password123");

        validToken = UUID.randomUUID().toString();

        member = Member.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("encodedPassword")
                .role(UserRole.MEMBER)
                .active(true)
                .verified(true)
                .verificationToken(validToken)
                .build();

        authRequest = new AuthenticationRequest();
        authRequest.setEmail("john.doe@example.com");
        authRequest.setPassword("password123");
    }

//    @Test
//    void testRegister_Success() {
//        // Arrange
//        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(jwtService.generateToken(any(Member.class))).thenReturn("jwtToken");
//        when(jwtService.generateRefreshToken(any(Member.class))).thenReturn("refreshToken");
//
//        // Important: Capturer l'objet Member et configurer le mock pour le retourner lors de la vérification du token
//        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
//        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
//            Member savedMember = invocation.getArgument(0);
//            // On configure le mock pour retourner cet utilisateur lors de la recherche par token
//            when(memberRepository.findByVerificationToken(savedMember.getVerificationToken())).thenReturn(Optional.of(savedMember));
//            return savedMember;
//        });
//
//        // Act
//        AuthenticationResponse response = authenticationService.register(registerRequest);
//
//        // Assert
//        verify(memberRepository, atLeastOnce()).save(memberCaptor.capture());
//        verify(emailService).sendVerificationEmail(anyString(), anyString());
//
//        Member savedMember = memberCaptor.getValue();
//        assertEquals(registerRequest.getFirstName(), savedMember.getFirstName());
//        assertEquals(registerRequest.getLastName(), savedMember.getLastName());
//        assertEquals(registerRequest.getEmail(), savedMember.getEmail());
//        assertEquals("encodedPassword", savedMember.getPassword());
//        assertEquals(UserRole.MEMBER, savedMember.getRole());
//        assertTrue(savedMember.getActive());
//
//        // Après la vérification, l'utilisateur devrait être vérifié
//        assertTrue(savedMember.getVerified());
//        assertNull(savedMember.getVerificationToken());
//
//        assertNotNull(response);
//        assertEquals("jwtToken", response.getToken());
//        assertEquals("refreshToken", response.getRefreshToken());
//    }

    @Test
    void testRegister_EmailAlreadyExists() {
        // Arrange
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.register(registerRequest)
        );

        assertEquals("Email déjà utilisé", exception.getMessage());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testAuthenticate_Success() {
        // Arrange
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(jwtService.generateToken(any(Member.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(Member.class))).thenReturn("refreshToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        // Assert
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
    }

    @Test
    void testAuthenticate_UserNotFound() {
        // Arrange
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.authenticate(authRequest)
        );

        assertEquals("Utilisateur non trouvé", exception.getMessage());
    }

    @Test
    void testAuthenticate_AccountNotVerified() {
        // Arrange
        member.setVerified(false);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.authenticate(authRequest)
        );

        assertEquals("Compte non vérifié. Veuillez vérifier votre email.", exception.getMessage());
    }

    @Test
    void testVerifyAccount_Success() {
        // Arrange
        when(memberRepository.findByVerificationToken(validToken)).thenReturn(Optional.of(member));

        // Act
        authenticationService.verifyAccount(validToken);

        // Assert
        verify(memberRepository).save(member);
        assertTrue(member.getVerified());
        assertNull(member.getVerificationToken());
    }

    @Test
    void testVerifyAccount_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";
        when(memberRepository.findByVerificationToken(invalidToken)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.verifyAccount(invalidToken)
        );

        assertEquals("Token invalide", exception.getMessage());
    }

    @Test
    void testInitiatePasswordReset_Success() {
        // Arrange
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);

        // Act
        authenticationService.initiatePasswordReset("john.doe@example.com");

        // Assert
        verify(memberRepository).save(memberCaptor.capture());
        verify(emailService).sendPasswordResetEmail(eq("john.doe@example.com"), anyString());

        Member savedMember = memberCaptor.getValue();
        assertNotNull(savedMember.getPasswordResetToken());
        assertNotNull(savedMember.getPasswordResetTokenExpiry());
    }

    @Test
    void testInitiatePasswordReset_EmailNotFound() {
        // Arrange
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.initiatePasswordReset("nonexistent@example.com")
        );

        assertEquals("Email non trouvé", exception.getMessage());
    }

    @Test
    void testResetPassword_Success() {
        // Arrange
        String resetToken = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        member.setPasswordResetToken(resetToken);
        member.setPasswordResetTokenExpiry(LocalDateTime.now().plusHours(1));

        when(memberRepository.findByPasswordResetToken(resetToken)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        // Act
        authenticationService.resetPassword(resetToken, newPassword);

        // Assert
        verify(memberRepository).save(member);
        assertEquals("encodedNewPassword", member.getPassword());
        assertNull(member.getPasswordResetToken());
        assertNull(member.getPasswordResetTokenExpiry());
    }

    @Test
    void testResetPassword_InvalidToken() {
        // Arrange
        String invalidToken = "invalid-token";
        when(memberRepository.findByPasswordResetToken(invalidToken)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.resetPassword(invalidToken, "newPassword123")
        );

        assertEquals("Token invalide", exception.getMessage());
    }

    @Test
    void testResetPassword_ExpiredToken() {
        // Arrange
        String resetToken = UUID.randomUUID().toString();
        String newPassword = "newPassword123";

        member.setPasswordResetToken(resetToken);
        member.setPasswordResetTokenExpiry(LocalDateTime.now().minusHours(1)); // Token expiré

        when(memberRepository.findByPasswordResetToken(resetToken)).thenReturn(Optional.of(member));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticationService.resetPassword(resetToken, newPassword)
        );

        assertEquals("Le token a expiré", exception.getMessage());
    }
}
