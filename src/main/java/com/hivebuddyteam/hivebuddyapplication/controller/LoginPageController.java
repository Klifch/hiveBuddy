package com.hivebuddyteam.hivebuddyapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginPageController {

    @GetMapping
    public String showLogin() {

        return "login_form";
    }


}
