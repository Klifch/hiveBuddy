package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.configuration.security.securityDTO.WebUser;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAll();

    void save(WebUser webUser);

    User finByUsername(String name);
}
