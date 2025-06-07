package com.demo.demo.services.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWUtil jwUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String userId = null;
        String jwt = null;

        // Extract JWT from Authorization header
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            try{
                userId = jwUtil.extractUserId(jwt);
            } catch (Exception e) {
                logger.error("Unable to extract user ID from JWT token", e);
            }
        }

        // If we have a valid user ID and no existing authentication
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Simply validate the token - no database lookup needed
            if(jwUtil.validateToken(jwt)){

                // Create a simple authentication with just a basic role
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}






/*
import com.demo.demo.entities.UserClient;
import com.demo.demo.repos.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWUtil jwUtil;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Debug logging
        System.out.println("=== JWT Filter Debug ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());

        final String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + (authorizationHeader != null ? "Present" : "Missing"));

        String userId = null;
        String jwt = null;

        // Extract JWT from Auth-header
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwt = authorizationHeader.substring(7);
            System.out.println("JWT Token extracted: " + jwt.substring(0, Math.min(jwt.length(), 20)) + "...");
            try{
                userId = jwUtil.extractUserId(jwt);
                System.out.println("Extracted User ID: " + userId);
            } catch (Exception e) {
                System.out.println("ERROR extracting user ID: " + e.getMessage());
                logger.error("Unable to get JWT Token or Token expired", e);
            }
        }else {
            System.out.println("No valid Authorization header found");
        }

        // validate token and set auth
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            System.out.println("Attempting to validate token...");
            try {
                if(jwUtil.validateToken(jwt)){
                    System.out.println("Token is valid");

                    Optional<UserClient> userClientOptional = userRepo.findById(UUID.fromString(userId));

                    if(userClientOptional.isPresent()){
                        UserClient user = userClientOptional.get();
                        System.out.println("User found in database: " + user.getId());

                        // Get authorities for user
                        List<GrantedAuthority> authorities = userAuthorityService.getAuthorityForUser(user);
                        System.out.println("User authorities: " + authorities);


                        // Create authenticaiton token with authorities
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        System.out.println("Authentication set successfully");

                    }
                    else{
                        System.out.println("ERROR: User with ID " + userId + " not found in database");
                        logger.warn("User with ID " + userId + " not found in database");
                    }

                }else {
                    System.out.println("Token validation failed");
                }
            } catch (Exception e) {
                System.out.println("ERROR during token validation: " + e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            if (userId == null) {
                System.out.println("No user ID extracted from token");
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("Authentication already exists in context");
            }
        }

        System.out.println("=== End JWT Filter Debug ===");
        filterChain.doFilter(request, response);
    }
}
*/