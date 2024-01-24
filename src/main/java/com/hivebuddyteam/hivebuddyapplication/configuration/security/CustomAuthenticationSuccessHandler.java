package com.hivebuddyteam.hivebuddyapplication.configuration.security;

import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;

import java.io.IOException;

 /*
    Important!
    A special interface that is processed by Spring
    Security once login/authentication is successful
*/

@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {


        System.out.println("In AuthenticationSuccessHandler");

        String username = authentication.getName();

        System.out.println("Username = " + username);

        User user = userService.finByUsername(username); // looking for user

        HttpSession session = request.getSession(); // taking session

        session.setAttribute("user", user);

        // TODO: should be changed to redirect to personalPage

        response.sendRedirect(request.getContextPath() + "/personalHome"); // redirect to internal page




    }
}
