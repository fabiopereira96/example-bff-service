package com.bff.example.controller.rest.vm;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class KeyAndPasswordVM {
    public String key;

    public String newPassword;

    @Override
    public String toString() {
        return "KeyAndPasswordVM{" + "key='" + key + '\'' + ", newPassword='" + newPassword + '\'' + '}';
    }
}
