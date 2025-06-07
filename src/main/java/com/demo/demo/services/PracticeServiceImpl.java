package com.demo.demo.services;

import com.demo.demo.entities.Award;
import com.demo.demo.entities.Practice;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.AwardRepo;
import com.demo.demo.repos.PracticeRepo;
import com.demo.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PracticeServiceImpl implements PracticeService{

    private final PracticeRepo practiceRepo;
    private final UserRepo userRepo;
    private final AwardRepo awardRepo;

    @Autowired
    PracticeServiceImpl(PracticeRepo practiceRepo, UserRepo userRepo, AwardRepo awardRepo){

        this.practiceRepo = practiceRepo;
        this.userRepo = userRepo;
        this.awardRepo = awardRepo;

    }

    @Override
    public Iterable<Practice> getAllPractices() {
        return practiceRepo.findAll();
    }

    @Override
    public String createPractice(Practice practice) {
        if(practice == null){
            return "Could add new event, because the event was null";
        }
        boolean exists = practiceRepo.existsById(practice.getId());
        if(exists){
            return "Could not add new event, because already found event with same ID.";
        }



        practiceRepo.save(practice);
        createAwardsForPractice(practice);
        return "Successfully added the event.";
    }

    private void createAwardsForPractice(Practice practice) {
        Award MVP = new Award();
        MVP.setType(Award.Type.MVP);
        MVP.setPractice(practice);


        Award GOAL = new Award();
        GOAL.setType(Award.Type.GOAL);
        GOAL.setPractice(practice);



        Award POTE = new Award();
        POTE.setType(Award.Type.PLAYER_OF_THE_EVENING);
        POTE.setPractice(practice);
        awardRepo.save(MVP);
        awardRepo.save(GOAL);
        awardRepo.save(POTE);
    }

    @Override
    public Optional<Practice> getPractice(int id) {
        return practiceRepo.findById(id);
    }

    @Override
    public List<Practice> getPracticesForTeam(long teamId) {
        List<Practice> practices = new ArrayList<>();
        if(teamId <= 0){
            return practices;
        }
        for (Practice practice : practiceRepo.findAll()) {
            if(practice.getTeam() != null && practice.getTeam().getId() == teamId){
                practices.add(practice);
            }

        }

        return practices;
    }

    @Override
    public String updatePractice(Practice updatedPractice) {
        if(updatedPractice == null){
            return "Event is null";
        }
        Optional<Practice> optionalPractice= practiceRepo.findById(updatedPractice.getId());
        if(optionalPractice.isEmpty()){
            return "Could not find the event when trying to update it.";
        }

        // Hämtar attandees från databas och lägger in i updatedPractice
        Practice practice = optionalPractice.get();
        Set<UserClient> attendessFromDatabase = new HashSet<>();

        attendessFromDatabase.addAll(practice.getAttendees());

        updatedPractice.getAttendees().clear();
        updatedPractice.getAttendees().addAll(attendessFromDatabase);

        practiceRepo.save(updatedPractice);
        return "Successfully updated the event.";
    }

    @Override
    public String updateInformation(Practice updatedPractice) {
        if(updatedPractice == null){
            return "Event is null";
        }
        Optional<Practice> optionalPractice = practiceRepo.findById(updatedPractice.getId());
        if(optionalPractice.isEmpty()){
            return "Could not find the practice when trying to update information.";
        }

        optionalPractice.get().setInformation(updatedPractice.getInformation());
        practiceRepo.save(optionalPractice.get());
        return "Successfully updated the information for the practice.";
    }



    @Override
    public String removePractice(Practice practiceToBeRemoved) {
        if(practiceToBeRemoved == null){
            return "Could not remove event, because it was null";
        }
        if(!practiceRepo.existsById(practiceToBeRemoved.getId())){
            return "Could not remove event, because could not find it in database.";
        }

        for (Award award : awardRepo.findAll()) {
            if(award.getPractice().getId() == practiceToBeRemoved.getId()){
                awardRepo.delete(award);
            }
        }

        practiceRepo.delete(practiceToBeRemoved);

        return "Successfully removed the event.";
    }

    // Add attendee
    @Override
    public String addAttendee(int practiceId, UserClient user) {
        Optional<Practice> optionalPractice = practiceRepo.findById(practiceId);
        if (optionalPractice.isEmpty()) {
            return "No practice found with id: " + practiceId;
        }
        if (user == null || !userRepo.existsById(user.getId())) {
            return "No user found with id: " + user.getId();
        }

        Practice practice = optionalPractice.get();

        // Load managed user from the database
        UserClient managedUser = userRepo.findById(user.getId()).get();

        if (!practice.getAttendees().contains(managedUser)) {
            practice.addAttendee(managedUser);
            practiceRepo.save(practice);
        }

        return "Successfully added player to practice";
    }

    @Override
    public String removeAttendee(int practiceId, UserClient user) {
        Optional<Practice> optionalPractice = practiceRepo.findById(practiceId);
        if(optionalPractice.isEmpty()){
            return "No practice found with id: " + practiceId;
        }
        if(user == null || !userRepo.existsById(user.getId())){
            return "No user found with id: " + user.getId();
        }
        Practice practice = optionalPractice.get();


        UserClient dbUser = userRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("No user found with id: " + user.getId()));
        practice.removeAttendee(dbUser);

        practiceRepo.save(practice);
        return "Successfully removed player to practice";
    }

    @Override
    public String toggleCancelledPractice(Practice practice) {
        if(practice == null){
            return "Practice is null when trying to cancel it.";
        }
        Optional<Practice> optionalPractice= practiceRepo.findById(practice.getId());
        if(optionalPractice.isEmpty()){
            return "Could not find the practice when trying to cancel it.";
        }

        Practice practiceToCancel = optionalPractice.get();

        boolean isCancelled = practiceToCancel.isCancelled();
        practiceToCancel.setCancelled(!isCancelled);

        practiceRepo.save(practiceToCancel);
        return "Successfully cancelled practice with id:" + practiceToCancel.getId();
    }
}
