package com.demo.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AwardTest {

    private Award award;
    private UUID userId;
    private UserClient userClient;
    private Practice practice;

    @BeforeEach
    public void setUp() {
        award = new Award();
        userId = UUID.randomUUID();
        userClient = new UserClient();
        userClient.setId(String.valueOf(userId));
        practice = new Practice();
    }

    @Test
    public void testSetAndGetId() {
        award.setId(42);
        assertEquals(42, award.getId());
    }

    @Test
    public void testSetAndGetPractice() {
        award.setPractice(practice);
        assertEquals(practice, award.getPractice());
    }

    @Test
    public void testSetAndGetDescription() {
        award.setDescription("Top Scorer");
        assertEquals("Top Scorer", award.getDescription());
    }

    @Test
    public void testSetAndGetType() {
        award.setType(Award.Type.MVP);
        assertEquals(Award.Type.MVP, award.getType());
    }

    @Test
    public void testSetAndGetValue() {
        award.setValue(Award.Value.GOLD);
        assertEquals(Award.Value.GOLD, award.getValue());
    }

    @Test
    public void testSetAndGetPlayer() {
        award.setPlayer(userClient);
        assertEquals(userClient, award.getPlayer());
    }

    @Test
    public void testPlayerIdFromPlayerObject() {
        award.setPlayer(userClient);
        assertEquals(userId, award.getPlayerId());
    }

    @Test
    public void testSetAndGetPlayerIdFallback() {
        award.setPlayerId(userId);
        assertEquals(userId, award.getPlayerId());
    }

    @Test
    public void testSetAndGetAlreadyVoted() {
        Set<UUID> voters = new HashSet<>(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));
        award.setAlreadyVoted(voters);
        assertEquals(voters, award.getAlreadyVoted());
    }

    @Test
    public void testSetAndGetVotes() {
        Map<UUID, Integer> voteMap = new HashMap<>();
        voteMap.put(UUID.randomUUID(), 3);
        voteMap.put(UUID.randomUUID(), 1);
        award.setVotes(voteMap);
        assertEquals(voteMap, award.getVotes());
    }
}
