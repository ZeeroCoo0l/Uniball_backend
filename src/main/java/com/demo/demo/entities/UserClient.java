package com.demo.demo.entities;
import jakarta.persistence.*;
import java.util.*;
import java.util.regex.Pattern;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class UserClient {

    @Id
//    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Position favoritePosition;
    private String description;
    private String profilePic;
    @ManyToOne(optional = true)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @ManyToMany(mappedBy = "attendees")
    @JsonIgnore
    private List<Practice> practices;

    @OneToMany(mappedBy = "player")
    @JsonIgnore
    private List<Award> awards;

    // TODO: Implement error-control for convertion of String-id to UUID.
    public UserClient(String id, String name, String email, String phone, Position position, String description, String profilePic){
        try {
            this.id = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID string: " + id);
        }
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.favoritePosition = Objects.requireNonNullElse(position, Position.NOPOSITION);
        this.description = description;
        this.profilePic = profilePic;

    }

    public UserClient() {
        favoritePosition = Position.NOPOSITION;
    }

    public List<Practice> getPractices() {
        return practices;
    }

    public void setPractices(List<Practice> practices) {
        this.practices = practices;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()){
            this.name = "default";
            return;
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(!isValidEmail(email)){
            return;
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(phone == null || phone.isEmpty()){
            return;
        }
        this.phone = phone;
    }

    public UUID getId() {
        return id;
    }

    public void setId(String i){
        try {
            id = UUID.fromString(i);
        }
        catch (IllegalArgumentException e){
            return;
        }
    }

    public void setID(UUID i ){
        if(i == null){
            return;
        }
        id = i;
    }

    public Position getFavoritePosition() {
        return favoritePosition;
    }

    public void setFavoritePosition(Position favoritePosition) {
        this.favoritePosition = favoritePosition;
    }

    public void setFavoritePosition(String favoritePosition){
        Position pos = convertStringToPosition(favoritePosition);
        setFavoritePosition(pos);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<Award> getAwards() {
        return awards;
    }

    public void setAwards(List<Award> awards) {
        this.awards = awards;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team newTeam) {
        // If it's the same team, or if both current and new team are null, do nothing.
        if (java.util.Objects.equals(this.team, newTeam)) {
            return;
        }

        Team oldTeam = this.team;

        // Remove this user from the old team's collection of players
        if (oldTeam != null && oldTeam.getPlayers() != null) { // Added null check for getPlayers()
            oldTeam.getPlayers().remove(this);
        }

        this.team = newTeam; // Set the new team association (this is the owning side)

        // Add this user to the new team's collection of players
        if (newTeam != null && newTeam.getPlayers() != null) { // Added null check for getPlayers()
            // Ensure the player is added only if not already present,
            if (!newTeam.getPlayers().contains(this)) {
                newTeam.getPlayers().add(this);
            }
        }
    }

    @JsonProperty("team_id")
    public void setTeamId(long teamId) {
        if (this.team == null) {
            return;
        }
        this.team.setId(teamId);
    }

    @JsonProperty("team_id")
    public long getTeamId(){
        if(team == null){
            return -1;
        }
        return team.getId();
    }



    private Position convertStringToPosition(String string){
        return switch (string.toUpperCase()) {
            case ("GOALKEEPER") -> Position.GOALKEEPER;
            case ("DEFENDER") -> Position.DEFENDER;
            case ("MIDFIELDER") -> Position.MIDFIELDER;
            case ("FORWARD") -> Position.FORWARD;
            default -> Position.NOPOSITION;
        };
    }


    public enum Position {
        GOALKEEPER,
        DEFENDER,
        MIDFIELDER,
        FORWARD,
        NOPOSITION
    }
//omgång2
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof UserClient){
            return this.id.equals(((UserClient) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
