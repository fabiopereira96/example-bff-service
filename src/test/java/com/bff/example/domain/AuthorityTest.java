package com.bff.example.domain;

import com.bff.example.constants.AuthoritiesConstants;
import com.bff.example.infrastructure.mongo.authority.Authority;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorityTest {

    @Test
    public void testAuthorityEquals() {
        var authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        var authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.name = AuthoritiesConstants.ADMIN;
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.name = AuthoritiesConstants.USER;
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.name = AuthoritiesConstants.USER;
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }
}
