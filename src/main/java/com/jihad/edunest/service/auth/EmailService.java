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

    @Async
    public void sendSchoolApprovalEmail(String toEmail, String schoolName) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<h3>Félicitations ! Votre école a été approuvée</h3>"
                    + "<p>Nous sommes heureux de vous informer que votre école <strong>" + schoolName + "</strong> a été approuvée sur EduNest.</p>"
                    + "<p>Vous pouvez maintenant vous connecter et gérer votre page d'école.</p>"
                    + "<p>Votre compte a été mis à jour avec les droits d'administrateur d'école. Vous pouvez désormais :</p>"
                    + "<ul>"
                    + "<li>Mettre à jour les informations de votre école</li>"
                    + "<li>Publier des offres spéciales</li>"
                    + "<li>Gérer le personnel</li>"
                    + "<li>Répondre aux demandes de contact</li>"
                    + "<li>Voir les statistiques de visite de votre page</li>"
                    + "</ul>"
                    + "<p>Pour accéder à votre tableau de bord, <a href=\"" + frontendUrl + "/dashboard\">cliquez ici</a>.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(toEmail);
            helper.setSubject("Votre école a été approuvée sur EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email d'approbation d'école", e);
        }
    }

    @Async
    public void sendSchoolRejectionEmail(String toEmail, String schoolName, String rejectionReason) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<h3>Mise à jour concernant votre demande d'enregistrement d'école</h3>"
                    + "<p>Nous avons examiné votre demande pour l'école <strong>" + schoolName + "</strong> sur EduNest.</p>"
                    + "<p>Malheureusement, nous ne pouvons pas approuver votre demande pour le moment.</p>";

            if (rejectionReason != null && !rejectionReason.isEmpty()) {
                htmlMsg += "<p><strong>Raison :</strong> " + rejectionReason + "</p>";
            }

            htmlMsg += "<p>Vous pouvez soumettre une nouvelle demande en tenant compte des remarques ci-dessus.</p>"
                    + "<p>Si vous avez des questions, n'hésitez pas à contacter notre équipe de support.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(toEmail);
            helper.setSubject("Mise à jour sur votre demande d'école - EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de rejet d'école", e);
        }
    }

    @Async
    public void sendSchoolRegistrationNotification(String memberEmail, Long requestId) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlMsg = "<h3>Confirmation de votre demande d'enregistrement d'école</h3>"
                    + "<p>Nous avons bien reçu votre demande d'enregistrement d'école sur EduNest.</p>"
                    + "<p>Numéro de référence de votre demande : <strong>" + requestId + "</strong></p>"
                    + "<p>Notre équipe va examiner votre demande dans les plus brefs délais. "
                    + "Vous recevrez une notification par email dès que votre demande aura été traitée.</p>"
                    + "<p>Vous pouvez suivre l'état de votre demande en vous connectant à votre compte EduNest.</p>";

            helper.setText(htmlMsg, true);
            helper.setTo(memberEmail);
            helper.setSubject("Confirmation de votre demande d'enregistrement d'école - EduNest");
            helper.setFrom(fromEmail);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de confirmation de demande", e);
        }
    }
}
