package com.hivebuddyteam.hivebuddyapplication.configuration.security.securityDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WebUser {

    @NotNull(message = "Required field")
    @Size(min = 1, message = "Required field")
    private String username;

    @NotNull(message = "Required field")
    @Size(min = 5, message = "Required field, Longer > 5")
    private String password;


    public WebUser() {
    }

    public WebUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
