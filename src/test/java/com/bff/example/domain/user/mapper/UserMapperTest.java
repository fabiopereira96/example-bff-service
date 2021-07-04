package com.bff.example.domain.user.mapper;

import com.bff.example.domain.user.model.User;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
public class UserMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String DEFAULT_ID = "userEntity-1";

    private UserMapper userMapper;
    private UserEntity userEntity;
    private User user;

    @BeforeEach
    public void init() {
        userMapper = new UserMapper();
        userEntity = new UserEntity();
        userEntity.login = DEFAULT_LOGIN;
        userEntity.password = RandomStringUtils.random(60);
        userEntity.activated = true;
        userEntity.email = "johndoe@localhost";
        userEntity.firstName = "john";
        userEntity.lastName = "doe";
        userEntity.imageUrl = "image_url";
        userEntity.langKey = "en";

        user = new User(userEntity);
    }

    @Test
    public void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(userEntity);
        userEntities.add(null);

        List<User> users = userMapper.usersToUserDTOs(userEntities);

        assertThat(users).isNotEmpty();
        assertThat(users).size().isEqualTo(1);
    }

    @Test
    public void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<User> usersDto = new ArrayList<>();
        usersDto.add(user);
        usersDto.add(null);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty();
        assertThat(userEntities).size().isEqualTo(1);
    }

    @Test
    public void userDTOsToUsersWithAuthoritiesStringShouldMapToUsersWithAuthoritiesDomain() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        user.authorities = authoritiesAsString;

        List<User> usersDto = new ArrayList<>();
        usersDto.add(user);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty();
        assertThat(userEntities).size().isEqualTo(1);
        assertThat(userEntities.get(0).authorities).isNotNull();
        assertThat(userEntities.get(0).authorities).isNotEmpty();
        assertThat(userEntities.get(0).authorities.iterator().next().name).isEqualTo("ADMIN");
    }

    @Test
    public void userDTOsToUsersMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        user.authorities = null;

        List<User> usersDto = new ArrayList<>();
        usersDto.add(user);

        List<UserEntity> userEntities = userMapper.userDTOsToUsers(usersDto);

        assertThat(userEntities).isNotEmpty();
        assertThat(userEntities).size().isEqualTo(1);
        assertThat(userEntities.get(0).authorities).isNotNull();
        assertThat(userEntities.get(0).authorities).isEmpty();
    }

    @Test
    public void userDTOToUserMapWithAuthoritiesStringShouldReturnUserWithAuthorities() {
        Set<String> authoritiesAsString = new HashSet<>();
        authoritiesAsString.add("ADMIN");
        user.authorities = authoritiesAsString;

        UserEntity userEntity = userMapper.userDTOToUser(user);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.authorities).isNotNull();
        assertThat(userEntity.authorities).isNotEmpty();
        assertThat(userEntity.authorities.iterator().next().name).isEqualTo("ADMIN");
    }

    @Test
    public void userDTOToUserMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
        user.authorities = null;

        UserEntity userEntity = userMapper.userDTOToUser(user);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.authorities).isNotNull();
        assertThat(userEntity.authorities).isEmpty();
    }

    @Test
    public void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    public void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).id).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
