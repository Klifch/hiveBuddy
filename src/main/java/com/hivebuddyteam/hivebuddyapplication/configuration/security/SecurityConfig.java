package com.hivebuddyteam.hivebuddyapplication.configuration.security;

import com.hivebuddyteam.hivebuddyapplication.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // bcrypt bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // authenticationProvider bean
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

        http.authorizeHttpRequests(configurer -> {
            configurer
                    .requestMatchers("/").permitAll() // basic pages
                    .requestMatchers("/assets/**").permitAll() // static resources for ui
                    .requestMatchers("/register/**").permitAll()
                    .requestMatchers("/ws-message/**").permitAll()
                    .requestMatchers("/showDashboard").permitAll()
                    .requestMatchers("/personalHome").hasRole("REGULAR")
                    .requestMatchers(HttpMethod.POST, "/deviceapi/auth").permitAll() // register device
                    .requestMatchers(HttpMethod.POST, "/sensorapi/sensors").permitAll() // remote devices api
                    .requestMatchers(HttpMethod.GET, "/sensorapi/sensors").hasRole("ADMIN") // ONLY ADMIN as exposes passwords and usernames
                    .anyRequest().authenticated();
        });

        http.formLogin((login) -> login
                .loginPage("/login") // our custom login page
//                .loginProcessingUrl("/authenticateTheUser") // free processor from Spring security -> we can use it to configure UsernamePasswordAuthenticationFilter
                .successHandler(customAuthenticationSuccessHandler) // execute successHandler for each successful login
                .permitAll()
        );

        http.logout(logout ->
                logout.logoutSuccessUrl("/")
        );

        http.csrf(csrf ->
                csrf.disable()
        );

        return http.build();
    }


}
