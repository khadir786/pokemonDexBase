package com.khadir.pokemonserver.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khadir.pokemonserver.config.handlers.CustomAuthenticationSuccessHandler;

@RestController
@RequestMapping("/api/auth/")
public class AdminController {
	
	@Autowired
	private SessionRegistry sessionRegistry;	
	
	@GetMapping("/active-sessions")
//	@PreAuthorize("hasRole('ADMIN')")
	public List<String> getActiveSessions() {
	    List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
	    List<String> activeUsers = new ArrayList<>();

	    for (Object principal : allPrincipals) {
	        if (principal instanceof UserDetails) {
	            UserDetails user = (UserDetails) principal;
	            boolean isNonExpired = sessionRegistry.getAllSessions(principal, false)
	                    .stream()
	                    .anyMatch(session -> !session.isExpired());
	            
	            if (isNonExpired) {
	                activeUsers.add(user.getUsername() + " - Session Active");
	            }
	        }
	    }
	    
//	    activeUsers.add(CustomAuthenticationSuccessHandler.getActiveSessions().toString()); // keep track of each indivdual's instances
	    return activeUsers;
	}

}
