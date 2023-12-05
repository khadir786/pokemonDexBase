package com.khadir.pokemonserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
                .requestMatchers("/api/auth/register", "api/users").permitAll()  // Permit all requests to register and users endpoint
                .anyRequest().authenticated()  // All other requests require authentication
            .and()
            // other configurations, e.g., formLogin, httpBasic, csrf, etc.
            ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

