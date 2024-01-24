package com.hivebuddyteam.hivebuddyapplication.controller;

import com.hivebuddyteam.hivebuddyapplication.configuration.security.securityDTO.WebUser;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationPageController {

    Logger logger = LoggerFactory.getLogger(RegistrationPageController.class);

    private final UserService userService;

    public RegistrationPageController(UserService userService) {
        logger.info("Injecting UserService instance in RegistrationPageController ...");
        this.userService = userService;
    }

    // adding initBinder preprocessor for trimming input strings
    // it will remove leading and trailing whitespaces
    // called for every incoming request
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        logger.info("InitBinder is working inside RegistrationPageController ...");

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/showRegistration")
    public String showRegistration(Model model) {

        logger.info("Received call to /showRegistration ...");
        logger.info("Adding new WebUser to the model ...");

        model.addAttribute("webUser", new WebUser());

        logger.info("Returning registration_form ...");

        return "registration_form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(
            @Valid @ModelAttribute("webUser") WebUser webUser,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {

        logger.info("Received POST http request to /processRegistrationForm ...");

        String username = webUser.getUsername();

        logger.info("Retrieved username -> {} from Model attribute webUser in /processRegistrationForm ...", username);
        logger.info("Processing registration form for new user -> {} in /processRegistrationForm ...", username);


        // validation part
        // check form for errors
        if (bindingResult.hasErrors()) {
            logger.warn("Errors found in registration form: {}", bindingResult.getErrorCount());
            return "registration_form";
        }

        // check if user already exists
        User existingUser = userService.finByUsername(username);
        if (existingUser != null) {
            model.addAttribute("webUser", new WebUser());
            model.addAttribute("registrationError", "User already exists!");

            logger.warn("Username -> {} already exists!", username);

            return "registration_form";
        }

        // creating user account and saving it in database
        userService.save(webUser);

        logger.info("SUCCESS -> Crated new User -> {}", username);

        // placing user to the web http session
        // session.setAttribute("user", webUser); // it could be security leak, change in the future

        return "redirect:/showLogin";


    }


}
