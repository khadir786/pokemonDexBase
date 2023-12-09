package com.khadir.pokemonserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.khadir.pokemonserver.config.handlers.CustomAuthenticationFailureHandler;
import com.khadir.pokemonserver.config.handlers.CustomAuthenticationSuccessHandler;
import com.khadir.pokemonserver.services.CustomUserDetailsService;



@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private CustomUserDetailsService userDetailsService;
	
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    private CustomAuthenticationFailureHandler failureHandler;
	
	@Autowired
	public SecurityConfig(CustomUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	@Bean
	public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> 
			authorize
					.requestMatchers("/api/auth/register", "/api/users", "/api/auth/login").permitAll()
					.anyRequest().authenticated() // All other requests require authentication
			)
			.formLogin(form -> form
					.loginProcessingUrl("/api/auth/login")
					.successHandler(successHandler)
	                .failureHandler(failureHandler)
					.permitAll());
		return http.build();
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addCorsMappings(CorsRegistry registry) {
	            registry.addMapping("/api/**").allowedOrigins("http://localhost:3000");
	        }
	    };
	}

	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }	
    
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    
}

