package com.bff.example.domain.user.model;

import com.bff.example.configuration.Constants;
import com.bff.example.infrastructure.mongo.user.UserEntity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * A DTO representing a user, with his authorities.
 */
@RegisterForReflection
public class User {
    public String id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    public String login;

    @Size(max = 50)
    public String firstName;

    @Size(max = 50)
    public String lastName;

    @Email
    @Size(min = 5, max = 254)
    public String email;

    @Size(max = 256)
    public String imageUrl;

    public boolean activated = false;

    @Size(min = 2, max = 10)
    public String langKey;

    public String createdBy;

    public Instant createdDate;

    public String lastModifiedBy;

    public Instant lastModifiedDate;

    public Set<String> authorities;

    public User() {
        // Empty constructor needed for Jackson.
    }

    public User(UserEntity userEntity) {
        this.id = userEntity.id;
        this.login = userEntity.login;
        this.firstName = userEntity.firstName;
        this.lastName = userEntity.lastName;
        this.email = userEntity.email;
        this.activated = userEntity.activated;
        this.imageUrl = userEntity.imageUrl;
        this.langKey = userEntity.langKey;
        this.createdBy = userEntity.createdBy;
        this.createdDate = userEntity.createdDate;
        this.lastModifiedBy = userEntity.lastModifiedBy;
        this.lastModifiedDate = userEntity.lastModifiedDate;
        this.authorities = userEntity.authorities.stream().map(authority -> authority.name).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return (
            "User{" +
            "login='" +
            login +
            '\'' +
            ", firstName='" +
            firstName +
            '\'' +
            ", lastName='" +
            lastName +
            '\'' +
            ", email='" +
            email +
            '\'' +
            ", imageUrl='" +
            imageUrl +
            '\'' +
            ", activated=" +
            activated +
            ", langKey='" +
            langKey +
            '\'' +
            ", createdBy=" +
            createdBy +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy='" +
            lastModifiedBy +
            '\'' +
            ", lastModifiedDate=" +
            lastModifiedDate +
            ", authorities=" +
            authorities +
            "}"
        );
    }
}
