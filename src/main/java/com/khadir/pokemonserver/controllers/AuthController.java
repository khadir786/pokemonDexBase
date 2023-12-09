package com.khadir.pokemonserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.exceptions.UserAlreadyExistsException;
import com.khadir.pokemonserver.models.User;
import com.khadir.pokemonserver.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

	@PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            User user = userService.registerNewUser(userDto);
            UserDto responseDto = convertToDto(user);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        // Include more exception handling as needed
    }
	
	private UserDto convertToDto(User user) {
		UserDto userDto = UserDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.build();
        return userDto;
    }
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserDto userDto, Authentication authentication) {
	    // Can use 'authentication' to access the currently authenticated user's details
	    // Implement any additional logic needed post-authentication
	    return ResponseEntity.ok().body("User logged in successfully");
	}

}
