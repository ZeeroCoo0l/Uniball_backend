package com.demo.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "practice")
public class Practice extends Event{

    @ManyToMany
    private List<Exercise> exercises;
    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    Team team;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "practice_attendees",
            joinColumns = @JoinColumn(name = "practice_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserClient> attendees = new HashSet<>();



    public Practice(String name){
        setName(name);
        setDate(LocalDateTime.now());
        exercises = new ArrayList<>();
    }

    public Practice() {
    }

    @Override
    public Set<UserClient> getAttendees() {
        return attendees;
    }

    public void addAttendee(UserClient userClient) {
        attendees.add(userClient);
        userClient.getPractices().add(this);  // maintain both sides
    }

    public void removeAttendee(UserClient userClient) {
        attendees.remove(userClient);
        userClient.getPractices().remove(this);  // maintain both sides
    }



    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void addExercise(Exercise exercise){
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise){
        exercises.remove(exercise);
    }

    @JsonProperty("team_id")
    public void setTeamId(Long teamId) {
        if (this.team == null) {
            this.team = new Team();
        }
        this.team.setId(teamId);
    }

    @JsonProperty("team_id")
    public Long getTeamId(){
        if(team == null){
            return null;
        }
        return team.getId();
    }

}




