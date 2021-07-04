package com.bff.example.domain.security;

import com.bff.example.domain.security.exception.UserNotActivatedException;
import com.bff.example.domain.security.exception.UsernameNotFoundException;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.runtime.QuarkusPrincipal;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Locale;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public static final String emailValidator =
        "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

    final BCryptPasswordHasher passwordHasher;


    @Inject
    public AuthenticationService(BCryptPasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public QuarkusSecurityIdentity authenticate(String login, String password) {
        UserEntity userEntity = loadByUsername(login);
        if (!userEntity.activated) {
            throw new UserNotActivatedException("UserEntity " + login + " was not activated");
        }
        if (passwordHasher.checkPassword(password, userEntity.password)) {
            return createQuarkusSecurityIdentity(userEntity);
        }
        log.debug("Authentication failed: password does not match stored value");
        throw new AuthenticationFailedException("Authentication failed: password does not match stored value");
    }

    private UserEntity loadByUsername(String login) {
        log.debug("Authenticating {}", login);

        if (login.matches(emailValidator)) {
            return UserEntity
                .findOneWithAuthoritiesByEmailIgnoreCase(login)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity with email " + login + " was not found in the database"));
        }
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return UserEntity
            .findOneWithAuthoritiesByLogin(lowercaseLogin)
            .orElseThrow(() -> new UsernameNotFoundException("UserEntity " + lowercaseLogin + " was not found in the database"));
    }

    private QuarkusSecurityIdentity createQuarkusSecurityIdentity(UserEntity userEntity) {
        QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder();
        builder.setPrincipal(new QuarkusPrincipal(userEntity.login));
        builder.addCredential(new io.quarkus.security.credential.PasswordCredential(userEntity.password.toCharArray()));
        builder.addRoles(userEntity.authorities.stream().map(authority -> authority.name).collect(Collectors.toSet()));
        return builder.build();
    }
}
