package com.hivebuddyteam.hivebuddyapplication.configuration.security;

import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    // bcrypt bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // authernticationProvider bean
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();

        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder()); // we use bcrypt as encoder

        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationSuccessHandler customAuthenticationSuccessHandler
    ) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/", "/showLogin").permitAll() // basic pages
                        .requestMatchers("/assets/**").permitAll() // static resources for interface
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/ws-message/**").permitAll()
                        .requestMatchers("/showDashboard").permitAll()
                        .requestMatchers("/personalHome").hasRole("REGULAR")
                        .requestMatchers(HttpMethod.POST, "/sensorapi/sensors").permitAll() // remote devices api
                        .requestMatchers(HttpMethod.GET, "/sensorapi/sensors").hasRole("ADMIN") // ONLY ADMIN as exposes passwords and usernames
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showLogin") // our custom login page
                                .loginProcessingUrl("/authenticateTheUser") // free processor from Spring security
                                .successHandler(customAuthenticationSuccessHandler) // execute successHandler for each successful login
                                .permitAll()
                        )
                .logout(logout ->
                        logout.permitAll()
                )
                .csrf(csrf ->
                        csrf.disable()
                );

        return http.build();
    }


}
