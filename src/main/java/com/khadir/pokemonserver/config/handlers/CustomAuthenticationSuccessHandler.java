package com.khadir.pokemonserver.config.handlers;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khadir.pokemonserver.dtos.UserResponseDto;
import com.khadir.pokemonserver.models.User;
import com.khadir.pokemonserver.repos.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Set username in the session and add to active sessions
        String username = authentication.getName();
        request.getSession().setAttribute("username", username);
        activeSessions.put(request.getSession().getId(), username);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        // Create a response DTO for response
        UserResponseDto responseDto = UserResponseDto.builder()
        		.id(user.getId())
                .username(userDetails.getUsername())
                .build();

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }

    public static ConcurrentHashMap<String, String> getActiveSessions() {
        return activeSessions;
    }
    
    public static void removeActiveSessions(String sessionId) {
        String usernameToRemove = activeSessions.get(sessionId);
        
        // Check if the sessionId exists in the map
        if (usernameToRemove != null) {
            // Iterate through the entries and remove matching entries
            activeSessions.forEach((key, value) -> {
                if (value.equals(usernameToRemove)) {
                    activeSessions.remove(key);
                }
            });
        }
    }



}

