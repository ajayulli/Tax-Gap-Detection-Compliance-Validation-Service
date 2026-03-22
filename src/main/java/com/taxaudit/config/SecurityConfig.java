package com.taxaudit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disabled for stateless REST API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/transactions/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/reports/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // Enables Basic Auth for Postman

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // Wires Spring Security directly to our MySQL database using the injected DataSource
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        
        // Custom queries to match the 'users' table structure we defined
        manager.setUsersByUsernameQuery("SELECT username, password, 'true' as enabled FROM users WHERE username=?");
        manager.setAuthoritiesByUsernameQuery("SELECT username, role FROM users WHERE username=?");
        
        return manager;
    }
}
