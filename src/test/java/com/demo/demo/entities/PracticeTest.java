package com.demo.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PracticeTest {

    private Practice practice;
    private UserClient user1;
    private UserClient user2;
    private Team team;
    private Exercise exercise;

    @BeforeEach
    void setUp() {
        practice = new Practice("Evening Practice");

        user1 = new UserClient();
        user1.setID(UUID.randomUUID());
        // Manually initialize practices list
        user1.setAwards(new ArrayList<>());  // Required for safety
        user1.setPractices(new ArrayList<>());

        user2 = new UserClient();
        user2.setID(UUID.randomUUID());
        user2.setAwards(new ArrayList<>());
        user2.setPractices(new ArrayList<>());

        team = new Team();
        team.setId(99L);

        exercise = new Exercise();
    }

    @Test
    void testConstructorSetsNameAndDate() {
        Practice newPractice = new Practice("Morning Session");
        assertEquals("Morning Session", newPractice.getName());
        assertNotNull(newPractice.getDate());
        assertTrue(newPractice.getExercises().isEmpty());
    }

    @Test
    void testSetAndGetTeam() {
        practice.setTeam(team);
        assertEquals(team, practice.getTeam());
    }

    @Test
    void testSetAndGetTeamId() {
        practice.setTeam(team);
        assertEquals(99L, practice.getTeamId());

        practice.setTeamId(42L);
        assertEquals(42L, team.getId()); // Should update the same instance
    }

    @Test
    void testGetTeamIdWhenTeamIsNull() {
        Practice p = new Practice();
        assertNull(p.getTeamId());
    }

    @Test
    void testAddAndRemoveExercise() {
        practice.addExercise(exercise);
        assertTrue(practice.getExercises().contains(exercise));

        practice.removeExercise(exercise);
        assertFalse(practice.getExercises().contains(exercise));
    }

    @Test
    void testAddAttendeeMaintainsBidirectionalRelation() {
        practice.addAttendee(user1);
        assertTrue(practice.getAttendees().contains(user1));
        assertTrue(user1.getPractices().contains(practice));
    }

    @Test
    void testRemoveAttendeeMaintainsBidirectionalRelation() {
        practice.addAttendee(user1);
        practice.removeAttendee(user1);
        assertFalse(practice.getAttendees().contains(user1));
        assertFalse(user1.getPractices().contains(practice));
    }

    @Test
    void testMultipleAttendees() {
        practice.addAttendee(user1);
        practice.addAttendee(user2);

        Set<UserClient> attendees = practice.getAttendees();
        assertEquals(2, attendees.size());
        assertTrue(attendees.contains(user1));
        assertTrue(attendees.contains(user2));
    }
}
