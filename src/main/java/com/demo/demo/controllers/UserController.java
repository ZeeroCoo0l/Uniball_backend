package com.demo.demo.controllers;
import com.demo.demo.entities.UserClient;
import com.demo.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Denna klassen används för att hantera UserClient i backend. CRUD och andra metoder.
 */
@RestController
@RequestMapping(path="/user")
@CrossOrigin
public class UserController {
    //@Autowired
    private final UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/Hello")
    public String hello() {
        return userService.greeting();
    }

    @GetMapping("/all")
    public @ResponseBody Iterable<? extends UserClient> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/all/{id}")
    public @ResponseBody Optional<UserClient> getUser(@PathVariable String id){
        return userService.getUserById(id);
    }

    @PostMapping("/addUser")
    public @ResponseBody String addPlayer(@RequestBody UserClient player){
        return userService.addUser(player);
    }

    @PostMapping("/updateUser")
    public @ResponseBody String updatePlayer(@RequestBody UserClient player){
        return userService.updateUser(player);
    }

    @PostMapping("/deleteUser")
    public @ResponseBody String removePlayer(@RequestBody UserClient player){
        return userService.deleteUser(player);
    }

    @PostMapping("/addAll")
    public @ResponseBody String addAllPlayers(@RequestBody Iterable<UserClient> players){
        return userService.addAllUsers(players);
    }

    @PostMapping("/deleteUsers")
    public  @ResponseBody String removeplayers(@RequestBody Iterable<UserClient> players){
        return userService.deleteUsers(players);
    }

}
