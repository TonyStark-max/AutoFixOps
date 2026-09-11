package com.autofixops.targetapp.controller;

import com.autofixops.targetapp.entity.User;
import com.autofixops.targetapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Triggers INCIDENT 1 if user ID doesn't exist
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // Triggers INCIDENT 3
    @GetMapping("/search")
    public User searchUser(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }
}
