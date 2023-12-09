package com.khadir.pokemonserver.config.handlers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khadir.pokemonserver.dtos.UserResponseDto;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Create a response DTO
        UserResponseDto responseDto = UserResponseDto.builder()
                .username(userDetails.getUsername())
                .build();

        // Write the response
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}
