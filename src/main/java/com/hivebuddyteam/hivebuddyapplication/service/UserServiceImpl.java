package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.configuration.security.securityDTO.WebUser;
import com.hivebuddyteam.hivebuddyapplication.domain.Role;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.repository.RoleRepository;
import com.hivebuddyteam.hivebuddyapplication.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(WebUser webUser) {

        User user = new User();

        // configure new User object
        user.setUsername(webUser.getUsername());
        user.setPassword(passwordEncoder.encode((webUser.getPassword())));
        user.setRoles(
                roleRepository
                        .findAll()
                        .stream()
                        .filter(role -> role.getId() == 1L)
                        .collect(Collectors.toList())
        );
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public User finByUsername(String name) {
        return userRepository.findUserByUsername(name);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);


        if (user == null) {
            throw new UsernameNotFoundException("Invalid Username Or Password.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                        .stream()
                        .map(role ->
                                new SimpleGrantedAuthority(role.getName())
                        )
                        .collect(Collectors.toList())
        );
    }
}
