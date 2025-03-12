package com.jihad.edunest.service.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${application.frontend-url:http://localhost:3000}")
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String toEmail, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String verificationUrl = frontendUrl + "/verify?token=" + token;
            String htmlMsg = "<h3>Vérification de compte EduNest</h3>"
                    + "<p>Pour vérifier votre compte, veuillez cliquer sur le lien ci-dessous:</p>"
                    + "<p><a href=\"" + verificationUrl + "\">Vérifier mon compte</a></p>"
                    + "<p>Ce lien est valide pendant 24 heures.</p>"
                    + "<p>Si vous n'avez pas créé de compte sur EduNest, veuillez ignorer cet email.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(toEmail);
            helper.setSubject("Vérification de votre compte EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de vérification", e);
        }
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String resetUrl = frontendUrl + "/reset-password?token=" + token;
            String htmlMsg = "<h3>Réinitialisation de mot de passe EduNest</h3>"
                    + "<p>Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien ci-dessous:</p>"
                    + "<p><a href=\"" + resetUrl + "\">Réinitialiser mon mot de passe</a></p>"
                    + "<p>Ce lien est valide pendant 24 heures.</p>"
                    + "<p>Si vous n'avez pas demandé de réinitialisation de mot de passe, veuillez ignorer cet email.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(toEmail);
            helper.setSubject("Réinitialisation de votre mot de passe EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de réinitialisation", e);
        }
    }

    @Async
    public void sendContactRequestNotification(String schoolEmail, String memberName, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<h3>Nouvelle demande de contact sur EduNest</h3>"
                    + "<p>Vous avez reçu une nouvelle demande de contact de la part de " + memberName + ".</p>"
                    + "<p>Sujet: " + subject + "</p>"
                    + "<p>Connectez-vous à votre compte EduNest pour consulter tous les détails et y répondre.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(schoolEmail);
            helper.setSubject("Nouvelle demande de contact - EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de notification", e);
        }
    }
}
