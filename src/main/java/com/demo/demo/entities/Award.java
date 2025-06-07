package com.demo.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.*;

@Entity
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "practice_Id")
    private Practice practice;
    private String description;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Value value;
    @Transient // sparas lokalt men inte i databasen
    @JsonProperty("user_id")
    private UUID playerId;
    @JoinColumn(name = "user_id")
    @ManyToOne()
    @JsonIgnore
    private UserClient player; // Winner

    @ElementCollection
    @CollectionTable(
            name = "award_voters",
            joinColumns = @JoinColumn(name = "award_id")
    )
    @Column(name = "voter_uuid")
    @JsonIgnore
    private Set<UUID> alreadyVoted = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "award_user_scores",
            joinColumns = @JoinColumn(name = "award_id")
    )
    @MapKeyColumn(name = "user_uuid")
    @Column(name = "score")
    @JsonIgnore
    private Map<UUID, Integer> votes = new HashMap<>();
    public Award(){
    }

    public UserClient getPlayer() {
        return player;
    }

    public void setPlayer(UserClient player) {
        this.player = player;
    }

    public Map<UUID, Integer> getVotes() {
        return votes;
    }

    public void setVotes(Map<UUID, Integer> votes) {
        this.votes = votes;
    }


    public Set<UUID> getAlreadyVoted() {
        return alreadyVoted;
    }

    public void setAlreadyVoted(Set<UUID> voters) {
        this.alreadyVoted = voters;
    }

    @JsonProperty("user_id")
    public UUID getPlayerId() {
        if (player != null) {
            return player.getId();
        }
        return playerId; // fallback if not resolved yet
    }

    @JsonProperty("user_id")
    public void setPlayerId(UUID id) {
        this.playerId = id;
    }



    public Practice getPractice() {
        return practice;
    }

    public void setPractice(Practice practice) {
        this.practice = practice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public enum Type{
        GOAL,
        MVP,
        PLAYER_OF_THE_EVENING,
        NO_VALUE
    }

    // OBS! Just nu kan man bara få guld!
    public enum Value{
        GOLD,
        SILVER,
        BRONZE,
        NO_VALUE
    }
}

