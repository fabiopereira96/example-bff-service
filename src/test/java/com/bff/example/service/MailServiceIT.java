package com.bff.example.service;

import com.bff.example.domain.mail.MailService;
import com.bff.example.infrastructure.dataprovider.user.UserEntity;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link MailService}.
 */
@QuarkusTest
public class MailServiceIT {
    @Inject
    MockMailbox mailbox;

    @Inject
    MailService mailService;

    private static final String[] languages = {
        "en"
        // jhipster-needle-i18n-language-constant - JHipster will add/remove languages in this array
    };

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    UserEntity user() {
        UserEntity userEntity = new UserEntity();
        userEntity.login = "john";
        userEntity.email = "john.doe@example.com";
        userEntity.langKey = "en";

        return userEntity;
    }

    @Test
    void should_containsActivationInfosWhenCallSendActivationEmail() {
        UserEntity userEntity = user();

        mailService.sendActivationEmail(userEntity);

        List<Mail> sent = mailbox.getMessagesSentTo(userEntity.email);
        assertThat(sent).hasSize(1);
        Mail actual = sent.get(0);
        assertThat(actual.getHtml()).contains("Your JHipster account has been created, please click on the URL below to activate it:");
        assertThat(actual.getSubject()).isEqualTo("jhipsterSampleApplication account activation is required");
    }

    @Test
    void should_containsActivationInfosWhenCallSendCreationEmail() {
        UserEntity userEntity = user();

        mailService.sendCreationEmail(userEntity);

        List<Mail> sent = mailbox.getMessagesSentTo(userEntity.email);
        assertThat(sent).hasSize(1);
        Mail actual = sent.get(0);
        assertThat(actual.getHtml()).contains("Your JHipster account has been created, please click on the URL below to access it:");
        assertThat(actual.getSubject()).isEqualTo("jhipsterSampleApplication account activation is required");
    }

    @Test
    void should_containsResetInfosWhenCallSendPasswordResetMail() {
        UserEntity userEntity = user();

        mailService.sendPasswordResetMail(userEntity);

        List<Mail> sent = mailbox.getMessagesSentTo(userEntity.email);
        assertThat(sent).hasSize(1);
        Mail actual = sent.get(0);
        assertThat(actual.getHtml()).contains("For your JHipster account a password reset was requested, please click on the URL below to reset it:");
        assertThat(actual.getSubject()).isEqualTo("jhipsterSampleApplication password reset");
    }
}
