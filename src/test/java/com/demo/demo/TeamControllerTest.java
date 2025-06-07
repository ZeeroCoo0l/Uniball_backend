package com.demo.demo;

import com.demo.demo.controllers.TeamController;
import com.demo.demo.entities.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//@WithMockUser(roles = "ADMIN")
public class TeamControllerTest {

    @Autowired
    private TeamController teamController;

//    @Test
//    void testCreateTeamInDatabase() {
//        String uniqueName = "Team Test " + System.currentTimeMillis();
//        Team team = new Team(uniqueName);
//        String response = teamController.createTeam(team);
//
//        assertTrue(response.contains("Successfully"));
//        assertTrue(team.getId() > 0); // ID should be set after saving
//    }

    @Test
    void testCreateDuplicateTeamFails() {
        String name = "Unique Test Team";
        Team firstTeam = new Team(name);
        teamController.createTeam(firstTeam);

        Team duplicateTeam = new Team(name);
        String response = teamController.createTeam(duplicateTeam);

        assertTrue(response.contains("already exists"));
    }

}
