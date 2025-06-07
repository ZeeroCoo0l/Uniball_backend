package com.demo.demo.controllers;

import com.demo.demo.entities.Award;
import com.demo.demo.entities.Practice;
import com.demo.demo.entities.UserClient;
import com.demo.demo.services.AwardService;
import com.demo.demo.services.PracticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/practice")
@CrossOrigin
public class PracticeController {

    private final PracticeService practiceService;
    private final AwardService awardService;

    @Autowired
    PracticeController(PracticeService ps, AwardService awardService){
        this.practiceService = ps;
        this.awardService = awardService;
    }


    @GetMapping("/all")
    public @ResponseBody Iterable<Practice> getAllPractices(){
        return practiceService.getAllPractices();
    }

    // CREATE EVENT
    @PostMapping("/add")
    public @ResponseBody String createPractice(@RequestBody Practice practice){
        return practiceService.createPractice(practice);
    }

    //Get awards
    @GetMapping("/award/{practiceId}")
    public @ResponseBody Iterable<Award> getAwardsFromPractice(@PathVariable int practiceId){
        return awardService.getAwardsFromPractice(practiceId);
    }


    // READ/GET EVENT
    @GetMapping("/all/{id}")
    public @ResponseBody Optional<Practice> getPractice(@PathVariable int id){
        return practiceService.getPractice(id);
    }

    @GetMapping("/team/{teamId}")
    public  @ResponseBody List<Practice> getPracticeForTeam(@PathVariable long teamId){
        return practiceService.getPracticesForTeam(teamId);
    }


    // UPDATE EVENT
    @PostMapping("/updateEvent")
    public @ResponseBody String updatePractice(@RequestBody Practice updatedPractice){
        return practiceService.updatePractice(updatedPractice);
    }

    @PostMapping("/updateInformation")
    public @ResponseBody String updateInformation(@RequestBody Practice updatedPractice){
        return practiceService.updateInformation(updatedPractice);
    }

    // Add attendee
    @PostMapping("/addAttendee/{practice_id}")
    public @ResponseBody String addAttendee(@PathVariable int practice_id, @RequestBody UserClient user){
        return practiceService.addAttendee(practice_id, user);
    }

    @PostMapping("/removeAttendee/{practice_id}")
    public @ResponseBody String removeAttendee(@PathVariable int practice_id, @RequestBody UserClient user){
        return practiceService.removeAttendee(practice_id, user);
    }


    // DELETE EVENT
    @PostMapping("/remove")
    public @ResponseBody String removePractice(@RequestBody Practice practiceToBeRemoved){
        return practiceService.removePractice(practiceToBeRemoved);
    }

    // Cancel practice
    @PostMapping("/toggleCancel")
    public @ResponseBody String toggleCancelledPractice(@RequestBody Practice practiceToCancel){
        return practiceService.toggleCancelledPractice(practiceToCancel);
    }

}
