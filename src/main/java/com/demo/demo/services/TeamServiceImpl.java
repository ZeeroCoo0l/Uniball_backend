package com.demo.demo.services;

import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.TeamRepo;
import com.demo.demo.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;

    TeamServiceImpl(TeamRepo teamRepo, UserRepo userRepo) {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Iterable<Team> getAllTeams() {
        return teamRepo.findAll();
    }

    @Override
    public String createTeam(Team team) {
        if (team == null) {
            return "Could not add new team, because the team was null";
        }
        // Check if a team with the same name already exists
        if (teamRepo.existsByName(team.getName())) {
            return "Could not add new team, because a team with this name already exists.";
        }

        boolean exists = teamRepo.existsById(team.getId());
        if (exists) {
            return "Could not add new team, because already found team with same ID.";
        }
        teamRepo.save(team);
        return "Successfully added the team.";
    }

    @Override
    public Optional<Team> getTeam(Long id) {
        try {
            System.out.println("Received team ID: " + id);
            return teamRepo.findById(id);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse team ID: " + id);
            e.printStackTrace();
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Unexpected error while fetching team:");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Iterable<UserClient> getPlayersInTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        ArrayList<UserClient> players = new ArrayList<>();
        if (team.isEmpty()) {
            return players;
        }

        return team.get().getPlayers();
    }

    @Override
    public Iterable<UserClient> getAdminsInTeam(Long teamId) {
        Optional<Team> team = teamRepo.findById(teamId);
        if (team.isEmpty()) {
            return new ArrayList<>();
        }
        return team.get().getTeamAdmins();
    }

    @Override
    public String updateTeam(Team updatedTeam) {
        if (updatedTeam == null) {
            return "Team is null";
        }
        boolean exists = teamRepo.existsById(updatedTeam.getId());
        if (!exists) {
            return "Could not find the team when trying to update it.";
        }

        teamRepo.save(updatedTeam);
        return "Successfully updated the team.";
    }

    // Add Player to Team
    @Override
    public String addPlayerToTeam(UserClient user, long team_id) {
        Optional<UserClient> userClient = userRepo.findById(user.getId());

        // FINNS SPELARE?
        if (user == null || userClient.isEmpty()) {
            return "Could not add player because no player was found";
        }
        UserClient userFromDatabase = userClient.get();

        // ÄR SPELAREN REDAN MED I ETT LAG?
        if (userFromDatabase.getTeam() != null) {
            return "Could not add player to team, because user was already in team.";
        }

        // FINNS LAGET?
        Optional<Team> team = teamRepo.findById(team_id);
        System.out.println("IS EMPTY: " + team.isEmpty() + ", TEAM_ID:" + team_id);
        if (team.isEmpty() || getTeam(team.get().getId()).isEmpty()) {
            return "Could not add player, because no team was found";
        }
        // Uppdatera instansen från databasen
        userFromDatabase.setTeam(team.get());

        // Lägg till uppdaterade instansen tillbaka till databasen
        userRepo.save(userFromDatabase);
        return "Successfully added player to team";
    }

    @Override
    public String removePlayerFromTeam(UserClient user, long team_id) {
        Optional<UserClient> userClient = userRepo.findById(user.getId());

        // FINNS SPELARE?
        if (user == null || userClient.isEmpty()) {
            return "Could not remove player because no player was found";
        }
        UserClient userFromDatabase = userClient.get();

        // ÄR SPELAREN REDAN MED I ETT LAG?
        if (userFromDatabase.getTeam() == null) {
            return "Could not remove player from team, because user was not in a team.";
        }

        // FINNS LAGET?
        Optional<Team> team = getTeam(team_id);
        if (team.isEmpty() || getTeam(team.get().getId()).isEmpty()) {
            return "Could not remove player, because no team was found";
        }
        userFromDatabase.setTeam(null);
        userRepo.save(userFromDatabase);
        return "Successfully removed player from team.";
    }

    @Override
    public String addAdminToTeam(UserClient user, long team_id) {
        if (user == null || !userRepo.existsById(user.getId())) {
            return "Could not add admin because no admin was found";
        }
        Optional<Team> team = getTeam(team_id);
        if (team.isEmpty()) {
            return "Could not add admin, because no team was found";
        }

        if (!team.get().getTeamAdmins().contains(user)) {
            List<UserClient> admins = team.get().getTeamAdmins();
            admins.add(user);
            team.get().setTeamAdmins(admins);
            teamRepo.save(team.get());
        } else {
            return "User is already an admin of this team";
        }

        return "Successfully added admin to team";
    }

    /**
     * @param user Admin-player to remove, contains team_id of team to be removed
     *             from.
     * @return statement of the outcome of the method
     *         <p>
     *         This method removes a player as admin, but not as player so player is
     *         still part of team.
     */
    @Override
    public String removeAdminFromTeam(UserClient user, long team_id) {
        if (user == null || !userRepo.existsById(user.getId())) {
            return "Could not remove admin because no admin was found";
        }
        Optional<Team> team = getTeam(team_id);
        if (team.isEmpty()) {
            return "Could not remove admin because no team was found.";
        }
        if(team.get().getTeamAdmins().size() > 1){
            team.get().removeAdminFromTeam(user);
            teamRepo.save(team.get());
            return "Successfully removed admin from team.";
        }
        if(team.get().getTeamAdmins().size() == 1 && team.get().getPlayers().size() == 1){
            team.get().removeAdminFromTeam(user);
            teamRepo.save(team.get());
            return "Successfully removed admin from team, you were the last member, this will delete the team.";
        }
        else{
            return "Could not remove admin from team, because only one player is admin. Make another player admin to successfully remove this player as admin.";
        }
        
    }

    @Override
    public String removeTeam(Team teamToBeRemoved) {
        if (teamToBeRemoved == null) {
            return "Could not remove team, because it was null";
        }
        if (!teamRepo.existsById(teamToBeRemoved.getId())) {
            return "Could not remove team, because could not find it in database.";
        }
        Optional<Team> team = teamRepo.findById(teamToBeRemoved.getId());

        List<UserClient> teamAdmins = team.get().getTeamAdmins();
        if (!teamAdmins.isEmpty()) {
            for (UserClient teamAdmin : teamAdmins) {
                team.get().removeAdminFromTeam(teamAdmin);
            }
        }
        List<UserClient> teamPlayers = team.get().getPlayers();
        if (!teamPlayers.isEmpty()) {
            for (UserClient teamPlayer : teamPlayers) {
                team.get().removePlayer(teamPlayer);
            }
        }

        teamRepo.delete(teamToBeRemoved);
        return "Successfully removed the team.";
    }

    // Check admin in team
    @Override
    public boolean isAdminForTeam(UserClient player, long team_id) {
        // Null safety checks
        if (player == null || player.getId() == null) {
            System.out.println("Player or player ID is null.");
            return false;
        }

        Optional<Team> team = getTeam(team_id);
        if (team.isEmpty()) {
            System.out.println("Could not check admins for team because no team was found.");
            return false;
        }
        Optional<UserClient> team_player = userRepo.findById(player.getId());
        if(team_player.isEmpty()){
            System.out.println("Player was not admin becuase player was not found.");
            return false;
        }
        Team t = team.get();
        UserClient p = team_player.get();
        if(t.getTeamAdmins() != null)
            return t.getTeamAdmins().contains(p);
        else
            return false;
    }
}
