package com.demo.demo.services;

import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.TeamRepo;
import com.demo.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    UserRepo userRepo;
    TeamRepo teamRepo;

    @Autowired
    UserServiceImpl(UserRepo userRepo, TeamRepo teamRepo){
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
    }

    @Override
    public String greeting() {
        return "Hello, welcome to the Uniball backend-server!";
    }

    @Override
    public Iterable<UserClient> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Optional<UserClient> getUserById(String id) {
        UUID uuid;
        try{
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }

        return userRepo.findById(uuid);
    }

    @Override
    public String addUser(UserClient player) {
        if(player == null){
            return "Failed to add player, because player was null.";
        }
        if(player.getId() == null){
            return "Failed to add player, because player-id was null.";
        }
        if(player.getFavoritePosition() == null){
            player.setFavoritePosition(UserClient.Position.NOPOSITION);
        }

        //Check if userRepo contains player with same id.
        UUID id = player.getId();
        Optional<UserClient> exisitingPLayer = userRepo.findById(id);
        if(exisitingPLayer.isPresent()){
            return "There is already an player with same ID.";
        }

        userRepo.save(player);
        return "Added user.";
    }

    ///
    /// this method can't be used to remove player from team. Use teamService for that instead
    ///
    @Override
    public String updateUser(UserClient player) {
        if(player == null){
            return "Failed to update player, because player was null.";
        }
        if(player.getId() == null){
            return "Failed to update player, because player-id was null.";
        }
        if(!userRepo.existsById(player.getId())){
            return "Cant update a player that doesnt exist in the database.";
        }
        Team team = player.getTeam();
        if(team != null && !teamRepo.existsById(team.getId())){
            return "Players team does not exist in database";
        }

        Optional<UserClient> oldUser = userRepo.findById(player.getId());
        if(oldUser.isEmpty()){
            return "Cant update a player that doesnt exist in the database.";
        }
        else{
            if(team == null){
                player.setTeam(oldUser.get().getTeam());
            }
        }
        userRepo.save(player);
        return "Updated user";
    }

    @Override
    public String deleteUser(UserClient player) {
        if(player == null){
            return "Failed to delete user, because it was null";
        }
        if(player.getId() == null){
            return "Failed to delete player, because player-id was null.";
        }
        userRepo.delete(player);
        return "Deleted user.";
    }

    @Override
    public String addAllUsers(Iterable<UserClient> players) {
        for (UserClient player : players) {
            addUser(player);
        };
        return "Added all players";
    }

    @Override
    public String deleteUsers(Iterable<UserClient> players) {
        StringBuilder result = new StringBuilder();
        for (UserClient player : players) {
            result.append(deleteUser(player)).append("\n");
        }
        return result.toString();
    }
}
