package com.xvitu.transferences.domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String fullName;
    private String document; // todo - unique
    private String email; // todo - unique
    private String password;
}
