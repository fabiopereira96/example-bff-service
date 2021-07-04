package com.bff.example.domain.mail;

import com.bff.example.configuration.JHipsterProperties;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.qute.api.ResourcePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * Service for sending emails.
 */
@ApplicationScoped
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    final JHipsterProperties jHipsterProperties;

    final MailTemplate activationEmail;

    final MailTemplate creationEmail;

    final MailTemplate passwordResetEmail;

    @Inject
    public MailService(
        JHipsterProperties jHipsterProperties,
        @ResourcePath("mail/activationEmail") MailTemplate activationEmail,
        @ResourcePath("mail/creationEmail") MailTemplate creationEmail,
        @ResourcePath("mail/passwordResetEmail") MailTemplate passwordResetEmail
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.activationEmail = activationEmail;
        this.creationEmail = creationEmail;
        this.passwordResetEmail = passwordResetEmail;
    }

    public CompletionStage<Void> sendEmailFromTemplate(UserEntity userEntity, MailTemplate template, String subject) {
        return template
            .to(userEntity.email)
            .subject(subject)
            .data(BASE_URL, jHipsterProperties.mail.baseUrl)
            .data(USER, userEntity)
            .send()
            .subscribeAsCompletionStage()
            .thenAccept(
                it -> {
                    log.debug("Sent email to UserEntity '{}'", userEntity.email);
                }
            );
    }

    public CompletionStage<Void> sendActivationEmail(UserEntity userEntity) {
        log.debug("Sending activation email to '{}'", userEntity.email);
        return sendEmailFromTemplate(userEntity, activationEmail, "jhipsterSampleApplication account activation is required");
    }

    public CompletionStage<Void> sendCreationEmail(UserEntity userEntity) {
        log.debug("Sending creation email to '{}'", userEntity.email);
        return sendEmailFromTemplate(userEntity, creationEmail, "jhipsterSampleApplication account activation is required");
    }

    public CompletionStage<Void> sendPasswordResetMail(UserEntity userEntity) {
        log.debug("Sending password reset email to '{}'", userEntity.email);
        return sendEmailFromTemplate(userEntity, passwordResetEmail, "jhipsterSampleApplication password reset");
    }
}