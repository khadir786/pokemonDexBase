package com.khadir.pokemonserver.controllers;

import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khadir.pokemonserver.config.UserConfigs.ActiveUserListener;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {
	@GetMapping("/active-sessions")
	@PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> getActiveSessions() {
        return ActiveUserListener.getActiveSessions();
    }
}
