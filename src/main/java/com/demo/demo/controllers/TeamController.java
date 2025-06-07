package com.demo.demo.controllers;

import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;
import com.demo.demo.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/team")
@CrossOrigin
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<Team> getAllTeams(){
        return teamService.getAllTeams();
    }

    // CREATE TEAM
    @PostMapping("/add")
    public @ResponseBody String createTeam(@RequestBody Team team){
        return teamService.createTeam(team);

    }

    // READ/GET TEAM
    @GetMapping("/all/{id}")
    public @ResponseBody Optional<Team> getTeam(@PathVariable Long id) {
        try {
            if (id == null) {
                return Optional.empty();
            }
            return teamService.getTeam(id);
        } catch (NumberFormatException e) {
            // log error here if needed
            return Optional.empty();
        }
    }

    // Get players in Team
    @GetMapping("/getPlayers/{team_id}")
    public @ResponseBody Iterable<UserClient> getPlayersInTeam(@PathVariable Long team_id){
        Optional<Team> team = teamService.getTeam(team_id);
        if(team.isEmpty()){
            return new ArrayList<>();
        }
        return teamService.getPlayersInTeam(team_id);

    }

    @GetMapping("/getAdmins/{team_id}")
    public @ResponseBody Iterable<UserClient> getAdminsInTeam(@PathVariable Long team_id){
        Optional<Team> team = teamService.getTeam(team_id);
        if(team.isEmpty()){
            return new ArrayList<>();
        }
        return teamService.getAdminsInTeam(team_id);
    }


    // UPDATE TEAM
    @PostMapping("/updateTeam")
    public @ResponseBody String updateTeam(@RequestBody Team updatedTeam){
        return teamService.updateTeam(updatedTeam);
    }


    ///
    /// ADD PLAYER TO TEAM
    ///
    /// @param user is user with the new team_id.
    /// @return String
    @PostMapping("/addPlayer/{team_id}")
    public @ResponseBody String addPlayerToTeam(@RequestBody UserClient user, @PathVariable long team_id){
        return teamService.addPlayerToTeam(user, team_id);
    }

    // Remove player from Team
    @PostMapping("/removePlayer/{team_id}")
    public @ResponseBody String removePlayerFromTeam(@RequestBody UserClient user, @PathVariable long team_id){
        return teamService.removePlayerFromTeam(user, team_id);
    }

    @PostMapping("/removeAdmin/{team_id}")
    public @ResponseBody String removeAdminFromTeam(@RequestBody UserClient user, @PathVariable long team_id){
        return teamService.removeAdminFromTeam(user, team_id);
    }

    @PostMapping("/addAdmin/{team_id}")
    public @ResponseBody String addAdminToTeam(@RequestBody UserClient user, @PathVariable long team_id){
        return teamService.addAdminToTeam(user, team_id);
    }

    // DELETE TEAM
    @PostMapping("/remove")
    public @ResponseBody String removeTeam(@RequestBody Team teamToBeRemoved){
        return teamService.removeTeam(teamToBeRemoved);
    }

    @PostMapping("/isAdminOfTeam/{team_id}")
    public @ResponseBody boolean isAdminOfTeam(@RequestBody UserClient player, @PathVariable long team_id){
        return teamService.isAdminForTeam(player, team_id);
    }

}
