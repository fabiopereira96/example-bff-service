package com.bff.example.security;

import javax.ws.rs.NotAuthorizedException;

public class UsernameNotFoundException extends NotAuthorizedException {

    public UsernameNotFoundException(String message) {
        super(message);
    }
}
