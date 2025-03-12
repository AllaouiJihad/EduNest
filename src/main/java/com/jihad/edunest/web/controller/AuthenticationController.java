package com.jihad.edunest.web.controller;


import com.jihad.edunest.service.auth.AuthenticationService;
import com.jihad.edunest.web.vms.request.auth.AuthenticationRequest;
import com.jihad.edunest.web.vms.request.auth.PasswordResetRequest;
import com.jihad.edunest.web.vms.request.auth.RegisterRequest;
import com.jihad.edunest.web.vms.responce.auth.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam String token) {
        authService.verifyAccount(token);
        return ResponseEntity.ok("Compte vérifié avec succès");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok("Si cet email existe, vous recevrez un lien de réinitialisation");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
    }
}
