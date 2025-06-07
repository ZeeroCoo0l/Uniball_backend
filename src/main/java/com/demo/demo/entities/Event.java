package com.demo.demo.entities;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Practice.class, name = "Practice")
        // Add other subclasses here if needed
})
@MappedSuperclass
public abstract class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDateTime date;
    private String name;
    private String location;
    private String information;
    private boolean isCancelled;
    private boolean isRead;

//    @ManyToMany(fetch = FetchType.LAZY)
//    private List<UserClient> attendees;


    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){this.date = date;}

    public abstract Set<UserClient> getAttendees();

    public String getName(){
        return name;
    }

    public void setName(String name){this.name = name;}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInformation(){return information;}

    public void setInformation(String information){this.information = information;}

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}
