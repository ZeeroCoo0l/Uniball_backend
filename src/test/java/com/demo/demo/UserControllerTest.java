package com.demo.demo;

import com.demo.demo.controllers.UserController;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional  // Optional: ensures test data is rolled back after each test
public class UserControllerTest {
    @Autowired
    private UserController controller;
    @Autowired
    private UserRepo userRepo;

    @Test
    void testGreeting(){
        assertEquals("Hello, welcome to the Uniball backend-server!", controller.hello());
    }

    @Test
    void addPlayer(){
        UserClient user = new UserClient();
        user.setID(UUID.randomUUID());
        user.setName("test1");
        user.setEmail("test1@gmail.com");
        user.setPhone("123456789");
        user.setFavoritePosition(UserClient.Position.FORWARD);

        String response = controller.addPlayer(user);
        assertEquals("Added user.", response);
    }

    @Test
    void removePlayer(){
        UserClient user = new UserClient();
        user.setID(UUID.randomUUID());
        user.setName("test1");
        user.setEmail("test1@gmail.com");
        user.setPhone("123456789");
        user.setFavoritePosition(UserClient.Position.FORWARD);
        controller.addPlayer(user);

        String response = controller.removePlayer(user);
        assertEquals("Deleted user.", response, "Tried to delete the user but did not succeed.");
    }

    @Test
    void addSamePlayerMultipleTimes(){
        UserClient user = new UserClient();
        user.setID(UUID.randomUUID());
        user.setName("test1");
        user.setEmail("test1@gmail.com");
        user.setPhone("123456789");
        user.setFavoritePosition(UserClient.Position.FORWARD);
        String response1 = controller.addPlayer(user);
        String response2 = controller.addPlayer(user);

        assertEquals("Added user.", response1);
        assertEquals("There is already an player with same ID.", response2);
    }

    @Test
    void createPlayerWithNull(){
        UserClient user = null;
        String response = controller.addPlayer(user);
        assertEquals("Failed to add player, because player was null.", response, "Tried to create a player with null.");
    }

    @Test
    void updatePlayerWithNull(){
        UserClient noUser = null;
        String response = controller.updatePlayer(noUser);

        assertEquals("Failed to update player, because player was null.", response, "Tried to update a player with null");

    }

    @Test
    void deletePlayerWithNull(){
        String response = controller.removePlayer(null);
        assertEquals("Failed to delete user, because it was null", response, "Tried to delete a player with null");
    }

    @Test
    void createPlayerWithSameIdAsExisitingPlayer(){
        UserClient user1 = new UserClient();
        UUID id = UUID.randomUUID();
        user1.setID(id);
        user1.setName("user1");
        user1.setEmail("user1@example.com");
        user1.setFavoritePosition(UserClient.Position.FORWARD);
        controller.addPlayer(user1);

        UserClient user2 = new UserClient();
        user2.setID(id);
        user2.setName("user2");
        user2.setEmail("user2@example.com");
        user2.setFavoritePosition(UserClient.Position.DEFENDER);
        String response2 = controller.addPlayer(user2);
        assertEquals("There is already an player with same ID.", response2);
    }

    @Test
    void updatePlayerThatDoesNotExist(){
        UserClient user = new UserClient();
        UUID id = UUID.randomUUID();
        user.setID(id);
        user.setName("user3");
        user.setEmail("user3@example.com");
        user.setFavoritePosition(UserClient.Position.FORWARD);
        String response = controller.updatePlayer(user);

        assertEquals("Cant update a player that doesnt exist in the database.", response);
    }

    @Test
    void gettingUserWithInvalidUUID(){
        String invalidID = "INVALID_UUID";
        Optional<UserClient> user = controller.getUser(invalidID);

        assertEquals(Optional.empty(), user);
    }

    @Test
    void createPlayerWithInvalidUUID(){
        UserClient user = new UserClient();
        String invalidID = "INVALID_UUID";
        user.setId(invalidID);
        user.setName("user2");
        user.setEmail("user2@example.com");
        user.setFavoritePosition(UserClient.Position.DEFENDER);
        String response = controller.addPlayer(user);

        assertEquals("Failed to add player, because player-id was null.", response,"Failed to add player, because player-id was null.");


    }

    @Test
    void updatePlayerInvalidUUID(){
        UserClient user = new UserClient();
        String invalidID = "INVALID_UUID";
        user.setId(invalidID);
        user.setName("user2");
        user.setEmail("user2@example.com");
        user.setFavoritePosition(UserClient.Position.DEFENDER);
        String response = controller.updatePlayer(user);

        assertEquals("Failed to update player, because player-id was null.", response);

    }

    @Test
    void removePlayerInvalidUUID(){
        UserClient user = new UserClient();
        String invalidID = "INVALID_UUID";
        user.setId(invalidID);
        user.setName("user2");
        user.setEmail("user2@example.com");
        user.setFavoritePosition(UserClient.Position.DEFENDER);
        String response = controller.removePlayer(user);

        assertEquals("Failed to delete player, because player-id was null.", response);
    }

}
