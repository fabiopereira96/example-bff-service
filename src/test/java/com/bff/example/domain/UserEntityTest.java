package com.bff.example.domain;

import com.bff.example.TestUtil;
import com.bff.example.infrastructure.mongo.user.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    public void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(UserEntity.class);
        var user1 = new UserEntity();
        user1.id = "1";
        var user2 = new UserEntity();
        user2.id = user1.id;
        assertThat(user1).isEqualTo(user2);
        user2.id = "2";
        assertThat(user1).isNotEqualTo(user2);
        user1.id = null;
        assertThat(user1).isNotEqualTo(user2);
    }
}
