package com.hivebuddyteam.hivebuddyapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping("/showLogin")
    public String showLogin() {

        return "login_form";
    }


}
