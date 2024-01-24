package com.hivebuddyteam.hivebuddyapplication.configuration;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {

    @Bean
    public DataSource dataSource() {

        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://188.166.49.123:5432/beehive")
                .username("admin")
                .password("nimbus")
                .build();
    }
}
