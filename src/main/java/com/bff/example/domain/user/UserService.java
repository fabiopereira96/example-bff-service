package com.bff.example.domain.user;

import com.bff.example.configuration.Constants;
import com.bff.example.constants.AuthoritiesConstants;
import com.bff.example.domain.mail.exception.EmailAlreadyUsedException;
import com.bff.example.domain.security.BCryptPasswordHasher;
import com.bff.example.domain.security.RandomUtil;
import com.bff.example.domain.user.exception.InvalidPasswordException;
import com.bff.example.domain.user.exception.UsernameAlreadyUsedException;
import com.bff.example.domain.user.model.User;
import com.bff.example.infrastructure.mongo.authority.Authority;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.panache.common.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    final BCryptPasswordHasher passwordHasher;

    @Inject
    public UserService(BCryptPasswordHasher passwordHasher) {
        this.passwordHasher = passwordHasher;
    }

    public Optional<UserEntity> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return UserEntity
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.activated = true;
                    user.activationKey = null;
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public void changePassword(String login, String currentClearTextPassword, String newPassword) {
        UserEntity
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.password;
                    if (!passwordHasher.checkPassword(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    user.password = passwordHasher.hash(newPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for UserEntity: {}", user);
                }
            );
    }

    public Optional<UserEntity> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return UserEntity
            .findOneByResetKey(key)
            .filter(user -> user.resetDate.isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.password = passwordHasher.hash(newPassword);
                    user.resetKey = null;
                    user.resetDate = null;
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<UserEntity> requestPasswordReset(String mail) {
        return UserEntity
            .findOneByEmailIgnoreCase(mail)
            .filter(user -> user.activated)
            .map(
                user -> {
                    user.resetKey = RandomUtil.generateResetKey();
                    user.resetDate = Instant.now();
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public UserEntity registerUser(User user, String password) {
        UserEntity
            .findOneByLogin(user.login.toLowerCase())
            .ifPresent(
                existingUser -> {
                    var removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        UserEntity
            .findOneByEmailIgnoreCase(user.email)
            .ifPresent(
                existingUser -> {
                    var removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        var newUser = new UserEntity();
        newUser.login = user.login.toLowerCase();
        // new user gets initially a generated password
        newUser.password = passwordHasher.hash(password);
        newUser.firstName = user.firstName;
        newUser.lastName = user.lastName;
        if (user.email != null) {
            newUser.email = user.email.toLowerCase();
        }
        newUser.imageUrl = user.imageUrl;
        newUser.langKey = user.langKey;
        // new user is not active
        newUser.activated = false;
        // new user gets registration key
        newUser.activationKey = RandomUtil.generateActivationKey();
        Set<Authority> authorities = new HashSet<>();
        Authority.<Authority>findByIdOptional(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.authorities = authorities;
        UserEntity.persist(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for UserEntity: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(UserEntity existingUserEntity) {
        if (existingUserEntity.activated) {
            return false;
        }
        UserEntity.delete("id", existingUserEntity.id);
        this.clearUserCaches(existingUserEntity);
        return true;
    }

    public UserEntity createUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.login = user.login.toLowerCase();
        userEntity.firstName = user.firstName;
        userEntity.lastName = user.lastName;
        if (user.email != null) {
            userEntity.email = user.email.toLowerCase();
        }
        userEntity.imageUrl = user.imageUrl;
        if (user.langKey == null) {
            userEntity.langKey = Constants.DEFAULT_LANGUAGE; // default language
        } else {
            userEntity.langKey = user.langKey;
        }
        userEntity.password = passwordHasher.hash(RandomUtil.generatePassword());
        userEntity.resetKey = RandomUtil.generateResetKey();
        userEntity.resetDate = Instant.now();
        userEntity.activated = true;
        if (user.authorities != null) {
            userEntity.authorities = user
                .authorities.stream()
                .map(authority -> Authority.<Authority>findByIdOptional(authority))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        }
        UserEntity.persist(userEntity);
        this.clearUserCaches(userEntity);
        log.debug("Created Information for UserEntity: {}", userEntity);
        return userEntity;
    }

    public void deleteUser(String login) {
        UserEntity
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    UserEntity.delete("id", user.id);
                    this.clearUserCaches(user);
                    user.update();
                    log.debug("Deleted UserEntity: {}", user);
                }
            );
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<User> updateUser(User userDTO) {
        return UserEntity
            .<UserEntity>findByIdOptional(userDTO.id)
            .map(
                user -> {
                    user.login = userDTO.login.toLowerCase();
                    user.firstName = userDTO.firstName;
                    user.lastName = userDTO.lastName;
                    if (userDTO.email != null) {
                        user.email = userDTO.email.toLowerCase();
                    }
                    user.imageUrl = userDTO.imageUrl;
                    user.activated = userDTO.activated;
                    user.langKey = userDTO.langKey;
                    Set<Authority> managedAuthorities = user.authorities;
                    managedAuthorities.clear();
                    userDTO
                        .authorities.stream()
                        .map(authority -> Authority.<Authority>findByIdOptional(authority))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    user.update();
                    log.debug("Changed Information for UserEntity: {}", user);
                    return user;
                }
            )
            .map(User::new);
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param login     the login to find the user to update.
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String login, String firstName, String lastName, String email, String langKey, String imageUrl) {
        UserEntity
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    user.firstName = firstName;
                    user.lastName = lastName;
                    if (email != null) {
                        user.email = email.toLowerCase();
                    }
                    user.langKey = langKey;
                    user.imageUrl = imageUrl;
                    this.clearUserCaches(user);
                    log.debug("Changed Information for UserEntity: {}", user);
                }
            );
    }

    public Optional<UserEntity> getUserWithAuthoritiesByLogin(String login) {
        return UserEntity.findOneWithAuthoritiesByLogin(login);
    }

    public List<User> getAllManagedUsers() {

        return UserEntity.findAllByLoginNot(Page.ofSize(20), Constants.ANONYMOUS_USER).stream().map(User::new).collect(Collectors.toList());
    }

    public List<String> getAuthorities() {
        return Authority.<Authority>streamAll().map(authority -> authority.name).collect(Collectors.toList());
    }

    public void clearUserCaches(UserEntity userEntity) {
        this.clearUserCachesByLogin(userEntity.login);
        if (userEntity.email != null) {
            this.clearUserCachesByEmail(userEntity.email);
        }
    }

    @CacheInvalidate(cacheName = UserEntity.USERS_BY_EMAIL_CACHE)
    public void clearUserCachesByEmail(String email) {}

    @CacheInvalidate(cacheName = UserEntity.USERS_BY_LOGIN_CACHE)
    public void clearUserCachesByLogin(String login) {}
}
