package com.xvitu.transferences.infrastructure.persistance.jpa.entity;

import com.xvitu.transferences.domain.enums.UserType;
import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_document", columnNames = "document"),
    @UniqueConstraint(name = "uk_user_email", columnNames = "email")
})
public class UserEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserType type;

    public UserEntity(
          Integer id, String fullName, String document, String email, String password, UserType type
    ) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
