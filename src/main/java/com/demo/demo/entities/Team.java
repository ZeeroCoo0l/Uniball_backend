package com.demo.demo.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "team")
    private List<UserClient> players;

   @OrderBy("date ASC")
    @OneToMany (mappedBy = "team")
    private List<Practice> practices;

    @OneToMany(fetch = FetchType.EAGER)
    private List<UserClient> teamAdmins;

    public Team(String name){
        this.name = name;
        this.createdAt = LocalDateTime.now();
        this.players = new ArrayList<>();
        this.practices = new ArrayList<>();
        this.teamAdmins = new ArrayList<>();
    }

    public Team() {
        this.createdAt = LocalDateTime.now();
        this.players = new ArrayList<>();
        this.practices = new ArrayList<>();
        this.teamAdmins = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void setPlayers(List<UserClient> players) {
        this.players = players;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setTeamAdmins(List<UserClient> teamAdmins) {
        this.teamAdmins = teamAdmins;
    }

    public void setPractices(List<Practice> practices) {
        this.practices = practices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserClient> getPlayers(){
        return players;
    }

    public List<Practice> getPractices(){
        return practices;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addPlayer(UserClient player) {
        if (player == null) {
            return;
        }
        if (this.players == null) {
            this.players = new ArrayList<>();
        }
        if (!this.players.contains(player)) {
            this.players.add(player);
        }
        if (player.getTeam() != null && !player.getTeam().equals(this)) {
            player.setTeam(this);
        }
    }

    public void addPractice(Practice event){
        practices.add(event);
    }

    public void removePlayer(UserClient player) {
        if (player == null) {
            return;
        }
        if (this.players != null && this.players.contains(player)) {
            this.players.remove(player);
            if (player.getTeam() == this) {
                player.setTeam(null);
            }
        }
    }

    public void removePractice(Practice practice){
        practices.remove(practice);
    }

    public void changeName(String newName){
        name = newName;
    }

    public List<UserClient> getTeamAdmins(){
        return teamAdmins;
    }

    public void addAdminToTeam(UserClient admin){
        teamAdmins.add(admin);
    }

    public void removeAdminFromTeam(UserClient admin){
        teamAdmins.remove(admin);
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Team)){
            return false;
        }
        return this.id == ((Team) obj).id;
    }
}
