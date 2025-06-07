package com.demo.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@WithMockUser(roles = "ADMIN")
class TeamTest{

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team("Wolves");
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Wolves", team.getName());
        assertTrue(team.getPlayers().isEmpty());
        assertTrue(team.getPractices().isEmpty());
        assertTrue(team.getTeamAdmins().isEmpty());
    }

    @Test
    void testAddAndRemovePlayer() {
        UserClient p = new UserClient(UUID.randomUUID().toString(),"Zack","example@dsv.su.se","075-658 568", UserClient.Position.FORWARD, "","");
        team.addPlayer(p);
        assertEquals(1, team.getPlayers().size());

        team.removePlayer(p);
        assertTrue(team.getPlayers().isEmpty());
    }

    @Test
    void testAddAndRemovePractice() {
        Practice e = new Practice("Training");
        team.addPractice(e);
        assertEquals(1, team.getPractices().size());

        team.removePractice(e);
        assertTrue(team.getPractices().isEmpty());
    }

    @Test
    void testAddAndRemoveTeamAdmin() {
        UserClient u = new UserClient(UUID.randomUUID().toString(),"Sebastian","example@mail.com", "070754846", UserClient.Position.GOALKEEPER,"",""); // Assuming a no-arg constructor
        team.addAdminToTeam(u);
        assertEquals(1, team.getTeamAdmins().size());

        team.removeAdminFromTeam(u);
        assertTrue(team.getTeamAdmins().isEmpty());
    }

    @Test
    void testChangeName() {
        team.changeName("Falcons");
        assertEquals("Falcons", team.getName());
    }
}
