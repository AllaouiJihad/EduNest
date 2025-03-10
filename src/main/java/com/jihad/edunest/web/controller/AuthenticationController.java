package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.AuthService;
import com.jihad.edunest.web.vms.request.ForgotPasswordVM;
import com.jihad.edunest.web.vms.request.LoginVM;
import com.jihad.edunest.web.vms.request.RegisterVM;
import com.jihad.edunest.web.vms.request.ResetPasswordVM;
import com.jihad.edunest.web.vms.responce.TokenVM;
import jakarta.servlet.http.HttpServletRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication and registration")
public class AuthenticationController {
    private final AuthService authService;

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur
     *
     * @param registerVM Les informations d'inscription
     * @param request La requête HTTP pour obtenir l'origine du client
     * @return TokenVM contenant les jetons d'authentification et de rafraîchissement
     */
    @PostMapping("/register")
    public ResponseEntity<TokenVM> register(@Valid @RequestBody RegisterVM registerVM, HttpServletRequest request) {
        String clientOrigin = request.getHeader("Origin");
        TokenVM tokenVM = authService.register(registerVM, clientOrigin);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenVM);
    }

    /**
     * Endpoint pour l'authentification d'un utilisateur
     *
     * @param loginVM Les informations de connexion
     * @return TokenVM contenant les jetons d'authentification et de rafraîchissement
     */
    @PostMapping("/login")
    public ResponseEntity<TokenVM> login(@Valid @RequestBody LoginVM loginVM) {
        TokenVM tokenVM = authService.login(loginVM.getEmail(), loginVM.getPassword());
        return ResponseEntity.ok(tokenVM);
    }

    /**
     * Endpoint pour vérifier l'email d'un utilisateur
     *
     * @param token Le jeton de vérification
     * @return Un message de confirmation
     */
    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully");
    }

    /**
     * Endpoint pour rafraîchir le jeton d'authentification
     *
     * @param refreshToken Le jeton de rafraîchissement
     * @return TokenVM contenant le nouveau jeton d'authentification et le même jeton de rafraîchissement
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenVM> refreshToken(@RequestBody String refreshToken) {
        TokenVM tokenVM = authService.refresh(refreshToken);
        return ResponseEntity.ok(tokenVM);
    }

    /**
     * Endpoint pour initier le processus de récupération de mot de passe
     *
     * @param forgotPasswordVM La requête contenant l'email de l'utilisateur
     * @param request La requête HTTP pour obtenir l'origine du client
     * @return Un message de confirmation
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordVM forgotPasswordVM, HttpServletRequest request) {
        String clientOrigin = request.getHeader("Origin");
        authService.forgotPassword(forgotPasswordVM.getEmail(), clientOrigin);
        return ResponseEntity.ok("Password reset email sent");
    }

    /**
     * Endpoint pour réinitialiser le mot de passe
     *
     * @param resetPasswordVM La requête contenant le jeton et le nouveau mot de passe
     * @return Un message de confirmation
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordVM resetPasswordVM) {
        authService.resetPassword(resetPasswordVM.getToken(), resetPasswordVM.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }
}
