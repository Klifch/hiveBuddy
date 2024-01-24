package com.hivebuddyteam.hivebuddyapplication.controller;

import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Controller
//@RequestMapping("/user")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/show")
    public List<User> showData() {
        return userService.getAll();
    }
}
