package com.bff.example.controller.rest.exception;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    public EmailAlreadyUsedException() {
        super("Email is already in use!", "userManagement", "emailexists");
    }
}
