package com.bff.example.infrastructure.dataprovider.user;

import com.bff.example.configuration.Constants;
import com.bff.example.infrastructure.dataprovider.authority.Authority;
import io.quarkus.cache.CacheResult;
import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import io.quarkus.panache.common.Page;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.json.bind.annotation.JsonbTransient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@MongoEntity(collection="jhi_user")
public class UserEntity extends PanacheMongoEntityBase implements Serializable {
    private static final long serialVersionUID = 1L;
    @BsonIgnore
    public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";
    @BsonIgnore
    public static final String USERS_BY_LOGIN_CACHE = "usersByLogin";

    @BsonId
    public String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    public String login;

    @NotNull
    @Size(min = 60, max = 60)
    @JsonbTransient
    public String password;

    @Size(max = 50)
    public String firstName;

    @Size(max = 50)
    public String lastName;

    @Email
    @Size(min = 5, max = 254)
    public String email;

    @NotNull
    public boolean activated = false;

    @Size(min = 2, max = 10)
    public String langKey;

    @Size(max = 256)
    public String imageUrl;

    @Size(max = 20)
    @JsonbTransient
    public String activationKey;

    @Size(max = 20)
    @JsonbTransient
    public String resetKey;

    public Instant resetDate = null;

    @JsonbTransient
    public Set<Authority> authorities = new HashSet<>();

    //To move to an audit mechanism
    //    @CreatedBy
    @JsonbTransient
    public String createdBy = "";

    //    @CreatedDate
    @JsonbTransient
    public Instant createdDate = Instant.now();

    //    @LastModifiedBy
    @JsonbTransient
    public String lastModifiedBy = "";

    //    @LastModifiedDate
    @JsonbTransient
    public Instant lastModifiedDate = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEntity)) {
            return false;
        }
        return id != null && id.equals(((UserEntity) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return (
            "UserEntity{" +
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
            ", activated='" +
            activated +
            '\'' +
            ", langKey='" +
            langKey +
            '\'' +
            ", activationKey='" +
            activationKey +
            '\'' +
            "}"
        );
    }

    public static Optional<UserEntity> findOneByActivationKey(String activationKey) {
        return find("activationKey", activationKey).firstResultOptional();
    }

    public static List<UserEntity> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime) {
        return list("activated = false and activationKey not null and createdDate <= ?1", dateTime);
    }

    public static Optional<UserEntity> findOneByResetKey(String resetKey) {
        return find("resetKey", resetKey).firstResultOptional();
    }

    public static Optional<UserEntity> findOneByEmailIgnoreCase(String email) {
        return find("LOWER(email)", email).firstResultOptional();
    }

    public static Optional<UserEntity> findOneByLogin(String login) {
        return find("login", login).firstResultOptional();
    }

    public static Optional<UserEntity> findOneWithAuthoritiesById(Long id) {
        return find("FROM UserEntity u LEFT JOIN FETCH u.authorities WHERE u.id = ?1", id).firstResultOptional();
    }

    @CacheResult(cacheName = USERS_BY_LOGIN_CACHE)
    public static Optional<UserEntity> findOneWithAuthoritiesByLogin(String login) {
        return find("login", login)
            .firstResultOptional();
    }

    @CacheResult(cacheName = USERS_BY_EMAIL_CACHE)
    public static Optional<UserEntity> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return find("FROM UserEntity u LEFT JOIN FETCH u.authorities WHERE LOWER(u.login) = LOWER(?1)", email)
            .firstResultOptional();
    }

    public static List<UserEntity> findAllByLoginNot(Page page, String login) {
        return find("login != ?1", login).page(page).list();
    }
}
