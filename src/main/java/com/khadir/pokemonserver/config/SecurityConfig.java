package com.khadir.pokemonserver.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.khadir.pokemonserver.config.handlers.CustomAuthenticationFailureHandler;
import com.khadir.pokemonserver.config.handlers.CustomAuthenticationSuccessHandler;
import com.khadir.pokemonserver.services.CustomUserDetailsService;

import jakarta.servlet.http.HttpSessionListener;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
		
			.cors(cors -> cors.configurationSource(myWebsiteConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(authorize -> 
			authorize
//					.requestMatchers("/api/auth/register", "/api/users", "/api/auth/login", "/api/**").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().permitAll()
//					.anyRequest().authenticated() // All other requests require authentication
			)
			.formLogin(form -> form
					.loginProcessingUrl("/api/auth/login")
					.successHandler(successHandler)
	                .failureHandler(failureHandler)
					.permitAll())
			.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
					.maximumSessions(1)
					.sessionRegistry(sessionRegistry())
)
			.logout(logout -> logout
		            .deleteCookies("JSESSIONID")
		        );
			;
			
		return http.build();
	}
	
	
	@Bean
	CorsConfigurationSource myWebsiteConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }	
    
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    
}

