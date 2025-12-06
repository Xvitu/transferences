package com.xvitu.transferences.domain.entity;

import com.xvitu.transferences.domain.enums.UserType;

import java.util.UUID;

public class User {
    private final Integer id;
    private final String fullName;
    private final String document;
    private final String email;
    private final String password;
    private final UserType type;

    public User(Integer id, String fullName, String document, String email, String password, UserType type) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public Boolean isShopKeeper() { return type == UserType.SHOPKEEPER; }

    public UserType getType() { return type;}

    public Integer getId() { return id; }

    public String getDocument() { return document; }

    public String getEmail() { return email; }

    public String getFullName() { return fullName; }

    public String getPassword() { return password; }
}
