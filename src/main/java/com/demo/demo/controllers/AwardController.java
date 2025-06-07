package com.demo.demo.controllers;

import com.demo.demo.entities.Award;
import com.demo.demo.entities.UserClient;
import com.demo.demo.services.AwardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/awards")
public class AwardController {
    private final AwardService awardService;

    @Autowired
    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    // CREATE
    @PostMapping("/add")
    public @ResponseBody String addAward(@RequestBody Award award){
        return awardService.addAward(award);
    }


    // READ
    @GetMapping("/all/{id}")
    public @ResponseBody Optional<Award> getAward(@PathVariable int id){
        return awardService.getAward(id);
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Award> getAll(){
        return awardService.getAllAwards();
    }

    // UPDATE
    @PostMapping("/update")
    public @ResponseBody String updateAward(@RequestBody Award updatedAward){
        return awardService.updateAward(updatedAward);
    }

    // DELETE
    @PostMapping("/remove")
    public @ResponseBody String removeAward(@RequestBody Award award){
        return awardService.removeAward(award);
    }


    // VOTING
    @GetMapping("/hasVoted/{award_id}/{user_id}")
    public @ResponseBody boolean hasVoted(@PathVariable int award_id,@PathVariable UUID user_id){
        return awardService.hasVoted(award_id, user_id);
    }

    @GetMapping("/getCurrentWinners/{award_id}")
    public @ResponseBody Iterable<UserClient> getCurrentWinners(@PathVariable int award_id){
        return awardService.getCurrentWinners(award_id);
    }

    @GetMapping("/getCurrentRank/{award_id}")
    public @ResponseBody Iterable<UserClient> getCurrentRank(@PathVariable int award_id){
        return awardService.getCurrentRank(award_id);
    }

    @PostMapping("/addToAlreadyVoted/{award_id}")
    public @ResponseBody String addToAlreadyVoted(@RequestBody UserClient user, @PathVariable int award_id){
        return awardService.addToAlreadyVoted(award_id, user);
    }

    @PostMapping("/removeFromAlreadyVoted/{award_id}")
    public @ResponseBody String removeFromAlreadyVoted(@RequestBody UserClient user, @PathVariable int award_id){
        return awardService.removeFromAlreadyVoted(award_id, user);
    }

    @PostMapping("/updateVote/{award_id}/{votedFor}")
    public @ResponseBody String updateVote(@RequestBody UserClient playerToVoteOn,@PathVariable int award_id, @PathVariable boolean votedFor){
        return awardService.updateVote(playerToVoteOn,award_id, votedFor);
    }

    @GetMapping("/getTop3/{award_type}/{team_id}")
    public @ResponseBody Iterable<UserClient> getTop3InTeam(@PathVariable long team_id, @PathVariable String award_type){
        return awardService.getTop3InTeam(team_id, award_type);
    }

    @GetMapping("/closeVote/{award_id}")
    public @ResponseBody boolean closeVote(@PathVariable int award_id){
        return awardService.closeVote(award_id);
    }

}
