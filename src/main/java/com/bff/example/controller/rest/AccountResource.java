package com.bff.example.controller.rest;

import com.bff.example.controller.rest.exception.EmailAlreadyUsedException;
import com.bff.example.controller.rest.exception.LoginAlreadyUsedException;
import com.bff.example.controller.rest.vm.ManagedUserVM;
import com.bff.example.domain.user.UserService;
import com.bff.example.domain.user.exception.InvalidPasswordException;
import com.bff.example.domain.user.exception.UsernameAlreadyUsedException;
import com.bff.example.domain.user.model.PasswordChange;
import com.bff.example.domain.user.model.User;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import io.quarkus.security.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Optional;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AccountResource {
    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private static class AccountResourceException extends RuntimeException {

        AccountResourceException(String message) {
            super(message);
        }
    }

    final UserService userService;

    @Inject
    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * {@code GET /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GET
    @Path("/account")
    @Authenticated
    public User getAccount(@Context SecurityContext ctx) {
        return userService
            .getUserWithAuthoritiesByLogin(ctx.getUserPrincipal().getName())
            .map(User::new)
            .orElseThrow(() -> new AccountResourceException("UserEntity could not be found"));
    }

    /**
     * {@code POST /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @POST
    @Path("/account")
    public Response saveAccount(@Valid User userDTO, @Context SecurityContext ctx) {
        var userLogin = Optional
            .ofNullable(ctx.getUserPrincipal().getName())
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        var existingUser = UserEntity.findOneByEmailIgnoreCase(userDTO.email);
        if (existingUser.isPresent() && (!existingUser.get().login.equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        var user = UserEntity.findOneByLogin(userLogin);
        if (user.isEmpty()) {
            throw new AccountResourceException("UserEntity could not be found");
        }
        userService.updateUser(userLogin, userDTO.firstName, userDTO.lastName, userDTO.email, userDTO.langKey, userDTO.imageUrl);
        return Response.ok().build();
    }

    /**
     * {@code POST /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     * @return
     */
    @POST
    @Path("/register")
    @PermitAll
    public Response registerAccount(@Valid ManagedUserVM managedUserVM) {
        if (checkPasswordLength(managedUserVM.password)) {
            throw new InvalidPasswordException();
        }
        try {
            userService.registerUser(managedUserVM, managedUserVM.password);
            return Response.created(null).build();
        } catch (UsernameAlreadyUsedException exception) {
            throw new LoginAlreadyUsedException();
        } catch (EmailAlreadyUsedException exception) {
            throw new EmailAlreadyUsedException();
        }
    }

    /**
     * {@code GET /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GET
    @Path("/activate")
    @PermitAll
    public void activateAccount(@QueryParam(value = "key") String key) {
        var user = userService.activateRegistration(key);
        if (user.isEmpty()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param ctx the request security context.
     * @return the login if the user is authenticated.
     */
    @GET
    @Path("/authenticate")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String isAuthenticated(@Context SecurityContext ctx) {
        log.debug("REST request to check if the current user is authenticated");
        return Optional.ofNullable(ctx.getUserPrincipal()).map(Principal::getName).orElse("");
    }

    /**
     * {@code POST /account/change-password} : changes the current user's password.
     *
     * @param passwordChange current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @POST
    @Path("/account/change-password")
    public Response changePassword(PasswordChange passwordChange, @Context SecurityContext ctx) {
        var userLogin = Optional
            .ofNullable(ctx.getUserPrincipal().getName())
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        if (checkPasswordLength(passwordChange.newPassword)) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(userLogin, passwordChange.currentPassword, passwordChange.newPassword);
        return Response.ok().build();
    }

    private static boolean checkPasswordLength(String password) {
        return (password.isEmpty() ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH);
    }
}
