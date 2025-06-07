package com.demo.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.demo.demo.entities.UserClient.Position.NOPOSITION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserClientTest {
    UserClient user;

    @BeforeEach
    void setUp(){
        user = new UserClient();
    }

    @Test
    void getName() {
        String name = user.getName();
        assertNull(name);
    }

    @Test
    void setName() {
        String name = "test";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    void setNameWithNull(){
        user.setName(null);
        assertEquals(user.getName(), "default");
        //assertNull(user.getName());
    }

    @Test
    void setAndGetName(){
        String n = "testing name";
        user.setName(n);
        String name = user.getName();

        assertEquals(n, name);
    }

    @Test
    void getEmailWhenInit() {
        String email = user.getEmail();
        assertNull(email);
    }

    @Test
    void setEmail() {
        String mail = "test@gmail.com";
        user.setEmail(mail);
        assertEquals(mail, user.getEmail());
    }

    @Test
    void setEmailWithInvalidEmail(){
        String invalidEmail = "skdjfhskjdfh";
        user.setEmail(invalidEmail);
        assertNull(user.getEmail());
    }

    @Test
    void setAndGetEmail(){
        String e = "test@gmail.com";
        user.setEmail(e);

        String email = user.getEmail();
        assertEquals(e, email);
    }

    @Test
    void getPhone() {
        assertNull(user.getPhone());
    }

    @Test
    void setPhone() {
        String number = "0728491232";
        user.setPhone(number);
        assertEquals(number, user.getPhone());
    }

    @Test
    void setPhoneWithEmpty(){
        String n1 = "";
        user.setPhone(n1);
        assertNull(user.getPhone());
    }

    @Test
    void setPhoneWithNull(){
        String n = "123456789";
        user.setPhone(n);

        user.setPhone(null);
        assertEquals(n, user.getPhone());
    }
    @Test
    void getId() {
        UUID id = user.getId();
        assertNull(id);
    }

    @Test
    void setIdWithString() {
        UUID uuid = UUID.randomUUID();
        user.setId(uuid.toString());
        assertNotNull(user.getId());
        assertEquals(uuid.toString(), user.getId().toString());
    }

    @Test
    void setIdWithInvalidUUIDString(){
        String id = "273423g4jhg";
        user.setId(id);
        assertNull(user.getId());
    }

    @Test
    void setIDWithUUID() {
        UUID uuid = UUID.randomUUID();
        user.setID(uuid);
        assertEquals(uuid, user.getId());
    }

    @Test
    void getFavoritePositionWhenInit() {
        UserClient.Position pos = user.getFavoritePosition();
        assertEquals(NOPOSITION, pos);
    }

    @Test
    void setFavoritePosition() {
        UserClient.Position pos = UserClient.Position.GOALKEEPER;
        user.setFavoritePosition(pos);
        assertEquals(pos, user.getFavoritePosition());
    }

    @Test
    void setAndGetFavoritePosition(){
        UserClient.Position pos = UserClient.Position.FORWARD;
        user.setFavoritePosition(pos);

        UserClient.Position userPos = user.getFavoritePosition();
        assertEquals(pos, userPos);
    }

    @Test
    void testIfSameIdIsEqual() {
        UserClient u1 = new UserClient();
        UserClient u2 = new UserClient();

        UUID uuid = UUID.randomUUID();
        u1.setID(uuid);
        u1.setName("u1");

        u2.setID(uuid);
        u2.setName("u2");

        assertEquals(u1, u2);
    }
}