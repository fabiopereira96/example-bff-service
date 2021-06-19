package com.bff.example.domain.user.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * A DTO representing a password change required data - current and new password.
 */
@RegisterForReflection
public class PasswordChange {
    public String currentPassword;
    public String newPassword;

    public PasswordChange() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChange(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
