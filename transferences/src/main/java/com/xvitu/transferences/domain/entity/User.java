package com.xvitu.transferences.domain.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String fullName;
    private final String document; // todo - unique
    private final String email; // todo - unique
    private final String password;

    public User(UUID id, String fullName, String document, String email, String password) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.password = password;
    }

    public UUID getId() { return id; }

    public String getDocument() { return document; }

    public String getEmail() { return email; }

    public String getFullName() { return fullName; }

    public String getPassword() { return password; }
}
