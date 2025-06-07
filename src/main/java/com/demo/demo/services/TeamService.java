package com.demo.demo.services;

import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;

import java.util.Optional;

public interface TeamService {

    Iterable<Team> getAllTeams();
    String createTeam(Team team);
    Optional<Team> getTeam(Long id);
    String updateTeam(Team updatedTeam);
    String removeTeam(Team teamToBeRemoved);
    public String addPlayerToTeam(UserClient user, long team_id);
    public String removePlayerFromTeam(UserClient user, long team_id);

    Iterable<UserClient> getPlayersInTeam(Long teamId);

    String removeAdminFromTeam(UserClient user, long team_id);

    String addAdminToTeam(UserClient user, long team_id);

    Iterable<UserClient> getAdminsInTeam(Long teamId);

    boolean isAdminForTeam(UserClient player, long team_id);
}

