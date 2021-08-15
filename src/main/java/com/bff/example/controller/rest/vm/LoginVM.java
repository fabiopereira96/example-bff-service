package com.bff.example.controller.rest.vm;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RegisterForReflection
public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    public String username;

    @NotNull
    @Size(min = 4, max = 100)
    public String password;

    public Boolean rememberMe;

    @Override
    public String toString() {
        return "LoginVM{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
    }
}
