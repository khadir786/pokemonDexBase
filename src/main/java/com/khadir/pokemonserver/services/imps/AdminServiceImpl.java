package com.khadir.pokemonserver.services.imps;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.exceptions.UserNotFoundException;
import com.khadir.pokemonserver.models.Role;
import com.khadir.pokemonserver.models.User;
import com.khadir.pokemonserver.repos.RoleRepository;
import com.khadir.pokemonserver.repos.UserRepository;
import com.khadir.pokemonserver.services.AdminService;

public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
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

}
