package com.demo.demo.services.security;

import com.demo.demo.entities.Team;
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.TeamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAuthorityService {

    @Autowired
    private TeamRepo teamRepo;



    public List<GrantedAuthority> getAuthorityForUser(UserClient user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add base role
        authorities.add(new SimpleGrantedAuthority(("ROLE_PLAYER")));

        if(user.getTeam() != null){
            Team team = user.getTeam();

            // Add team membership
            authorities.add(new SimpleGrantedAuthority("TEAM_MEMBER_" + team.getId())); // Ends with team_id

            // Add team-admin membership if user is admin in team.
            if(team.getTeamAdmins() != null && team.getTeamAdmins().contains(user)){
                authorities.add(new SimpleGrantedAuthority("TEAM_ADMIN_"+team.getId()));
            }

        }
        return authorities;
    }
}
