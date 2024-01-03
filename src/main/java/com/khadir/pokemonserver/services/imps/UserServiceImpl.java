package com.khadir.pokemonserver.services.imps;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.dtos.UserResponseDto;
import com.khadir.pokemonserver.exceptions.UserAlreadyExistsException;
import com.khadir.pokemonserver.exceptions.UserNotFoundException;
import com.khadir.pokemonserver.models.Role;
import com.khadir.pokemonserver.models.User;
import com.khadir.pokemonserver.repos.RoleRepository;
import com.khadir.pokemonserver.repos.UserRepository;
import com.khadir.pokemonserver.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User registerNewUser(UserDto userDto) throws UserAlreadyExistsException {
		String username = userDto.getUsername();
		if (userRepository.findByUsername(username).isPresent()) {
			throw new UserAlreadyExistsException("There is already a user with the username: " + userDto.getUsername());
		}

		// Creating a new user instance
		String encodedPassword = passwordEncoder.encode(userDto.getPassword());
		User newUser = User.builder().username(username).password(encodedPassword).build();

		// Additional fields can be set based on User class and UserDto
		Role role = roleRepository.findByName("ROLE_USER").get();
		newUser.setRoles(Arrays.asList(role));	
		return userRepository.save(newUser);
	}

	@Override
	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

		UserResponseDto userDto = responseMapToUser(user);
		// Convert User to UserDto
		return userDto;
	}

	@Override
	public UserResponseDto updateUser(UserDto newUser, Long id) {
	    User existingUser = userRepository.findById(id)
	            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

	    		// Fetch the potentially existing user with the new username
	    Optional<User> userWithNewUsername = userRepository.findByUsername(newUser.getUsername());

	   			// Check if the new username already exists and does not belong to the current user
	    if (userWithNewUsername.isPresent() && !userWithNewUsername.get().getId().equals(id)) {
	        throw new UserAlreadyExistsException("There is already a user with the username: " + newUser.getUsername());
	    }

	    		// Update the existing user's fields
	    		// Check if field isn't null and isn't empty
	    if (newUser.getUsername() != null && !newUser.getUsername().isEmpty()) {
	        existingUser.setUsername(newUser.getUsername());
	    }
	    if (newUser.getAvatar() != null && !newUser.getAvatar().isEmpty()) {
	    	existingUser.setAvatar(newUser.getAvatar());
	    }
	    if (newUser.getPartnerPokemon() != null && !newUser.getPartnerPokemon().isEmpty()) {
	    	existingUser.setPartnerPokemon(newUser.getPartnerPokemon());
	    }
	    if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
	        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
	        existingUser.setPassword(encodedPassword);
	    }

	    userRepository.save(existingUser);
	    return responseMapToUser(existingUser);
	}


	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with: " + id));

		// manually clear the roles to avoid constraint violation
		user.getRoles().clear();
		userRepository.save(user);

		userRepository.deleteById(id);
	}

	@Override
	public List<UserResponseDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> responseMapToUser(user)).collect(Collectors.toList());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
		
		UserResponseDto userDto = responseMapToUser(user);
		return userDto;
	}
	
	private UserResponseDto responseMapToUser(User user) {
		UserResponseDto userDto = null;
		userDto = UserResponseDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.avatar(user.getAvatar())		
				.partnerPokemon(user.getPartnerPokemon())
				.build();
		return userDto;
		
	}

	
}
