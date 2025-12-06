package com.xvitu.transferences.infrastructure.persistance.adapter;

import com.xvitu.transferences.domain.entity.User;
import com.xvitu.transferences.domain.enums.UserType;
import com.xvitu.transferences.infrastructure.persistance.jpa.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserAdapterTest {

    private final UserAdapter adapter = new UserAdapter();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        User user = new User(
                id, "John Doe", "12345678900", "john@doe.com", "pass", UserType.CUSTOMER
        );

        UserEntity entity = adapter.toEntity(user);

        assertEquals(id, entity.getId());
        assertEquals("John Doe", entity.getFullName());
        assertEquals("12345678900", entity.getDocument());
        assertEquals("john@doe.com", entity.getEmail());
        assertEquals("pass", entity.getPassword());
        assertEquals(UserType.CUSTOMER, entity.getType());
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity(
                id, "Mary Jane", "98765432100", "mary@mj.com", "123",UserType.SHOPKEEPER
        );

        User user = adapter.toDomain(entity);

        assertEquals(id, user.getId());
        assertEquals("Mary Jane", user.getFullName());
        assertEquals("98765432100", user.getDocument());
        assertEquals("mary@mj.com", user.getEmail());
        assertEquals("123", user.getPassword());
        assertEquals(UserType.SHOPKEEPER, entity.getType());
    }
}
