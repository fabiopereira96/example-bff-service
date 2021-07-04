package com.bff.example.domain.user.mapper;

import com.bff.example.domain.user.model.User;
import com.bff.example.infrastructure.mongo.authority.Authority;
import com.bff.example.infrastructure.mongo.user.UserEntity;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UserEntity} and its DTO called {@link User}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Singleton
public class UserMapper {

    public List<User> usersToUserDTOs(List<UserEntity> userEntities) {
        return userEntities.stream()
            .filter(Objects::nonNull)
            .map(this::userToUserDTO)
            .collect(Collectors.toList());
    }

    public User userToUserDTO(UserEntity userEntity) {
        return new User(userEntity);
    }

    public List<UserEntity> userDTOsToUsers(List<User> users) {
        return users.stream()
            .filter(Objects::nonNull)
            .map(this::userDTOToUser)
            .collect(Collectors.toList());
    }

    public UserEntity userDTOToUser(User user) {
        if (user == null) {
            return null;
        } else {
            UserEntity userEntity = new UserEntity();
            userEntity.id = user.id;
            userEntity.login = user.login;
            userEntity.firstName = user.firstName;
            userEntity.lastName = user.lastName;
            userEntity.email = user.email;
            userEntity.imageUrl = user.imageUrl;
            userEntity.activated = user.activated;
            userEntity.langKey = user.langKey;
            Set<Authority> authorities = this.authoritiesFromStrings(user.authorities);
            userEntity.authorities = authorities;
            return userEntity;
        }
    }


    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString.stream().map(string -> {
                Authority auth = new Authority();
                auth.name = string;
                return auth;
            }).collect(Collectors.toSet());
        }

        return authorities;
    }

    public UserEntity userFromId(String id) {
        if (id == null) {
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.id = id;
        return userEntity;
    }
}
