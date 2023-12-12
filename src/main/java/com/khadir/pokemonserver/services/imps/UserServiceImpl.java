package com.khadir.pokemonserver.services.imps;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khadir.pokemonserver.dtos.UserDto;
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
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
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
	    User newUser = User.builder()
	    		.username(username)
	    		.password(encodedPassword)
	    		.build();

	    // Additional fields can be set based on User class and UserDto
	    Role role = roleRepository.findByName("ROLE_USER").get();
	    newUser.setRoles(Arrays.asList(role));
	    return userRepository.save(newUser);
    }

    @Override
    public List<UserDto> findAllUsers() {
    	List<User> users = userRepository.findAll();
    	return users.stream().map((user) -> mapToUser(user)).collect(Collectors.toList());
    }
    
    private UserDto mapToUser(User user) {
    	UserDto userDto = UserDto.builder()
    			.id(user.getId())
    			.username(user.getUsername())
    			.build();
    	return userDto;
    	
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }


    @Override
    public User updateUser(UserDto userDto, Long id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        
        // Update the existing user's fields
        existingUser.setUsername(userDto.getUsername());
        
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userDto.getPassword());
            existingUser.setPassword(encodedPassword);
        }
        // Other fields from userDto can be updated similarly
        return userRepository.save(existingUser);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with: " + id));
        userRepository.deleteById(id);
    }

	@Override
	public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        userRepository.save(user);
    }


    // Implement other methods...
}
