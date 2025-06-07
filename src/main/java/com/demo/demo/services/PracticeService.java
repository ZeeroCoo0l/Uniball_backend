package com.demo.demo.services;

import com.demo.demo.entities.Practice;
import com.demo.demo.entities.UserClient;

import java.util.List;
import java.util.Optional;

public interface PracticeService {
    Iterable<Practice> getAllPractices();
    String createPractice(Practice practice);
    Optional<Practice> getPractice(int id);
    List<Practice> getPracticesForTeam(long teamId); // Fixed to return all practices for a team
    String updatePractice(Practice updatedPractice);
    String removePractice(Practice practiceToBeRemoved);

    String addAttendee(int practiceId, UserClient user);

    String removeAttendee(int practiceId, UserClient user);

    String updateInformation(Practice practice);
    String toggleCancelledPractice(Practice practice);
}
