package com.jihad.edunest.service;

import com.jihad.edunest.web.vms.request.RegisterVM;
import com.jihad.edunest.web.vms.responce.TokenVM;
import jakarta.validation.Valid;

public interface AuthService {
    /**
     * Enregistre un nouvel utilisateur et envoie un e-mail de vérification
     *
     * @param registerVM Les informations d'inscription de l'utilisateur
     * @param clientOrigin L'URL d'origine du client pour les liens dans l'e-mail
     * @return TokenVM contenant les jetons d'authentification et de rafraîchissement
     */
    TokenVM register(@Valid RegisterVM registerVM, String clientOrigin);

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur/email et mot de passe
     *
     * @param username Le nom d'utilisateur ou l'e-mail
     * @param password Le mot de passe
     * @return TokenVM contenant les jetons d'authentification et de rafraîchissement
     */
    TokenVM login(String username, String password);

    /**
     * Rafraîchit le jeton d'authentification à l'aide du jeton de rafraîchissement
     *
     * @param refreshToken Le jeton de rafraîchissement
     * @return TokenVM contenant le nouveau jeton d'authentification et le même jeton de rafraîchissement
     */
    TokenVM refresh(String refreshToken);

    /**
     * Vérifie l'e-mail d'un utilisateur à l'aide du jeton de vérification
     *
     * @param token Le jeton de vérification
     */
    void verifyEmail(String token);

    /**
     * Initie le processus de réinitialisation de mot de passe oublié
     *
     * @param email L'e-mail de l'utilisateur
     * @param clientOrigin L'URL d'origine du client pour les liens dans l'e-mail
     */
    void forgotPassword(String email, String clientOrigin);

    /**
     * Réinitialise le mot de passe de l'utilisateur avec le jeton de réinitialisation
     *
     * @param token Le jeton de réinitialisation du mot de passe
     * @param newPassword Le nouveau mot de passe
     */
    void resetPassword(String token, String newPassword);

    /**
     * Génère un jeton de vérification unique
     *
     * @return Le jeton de vérification généré
     */
    String generateVerificationToken();

    /**
     * Génère un jeton de réinitialisation de mot de passe unique
     *
     * @return Le jeton de réinitialisation généré
     */
    String generatePasswordResetToken();
}
