package com.khadir.pokemonserver.services.imps;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.exceptions.UserAlreadyExistsException;
import com.khadir.pokemonserver.models.User;
import com.khadir.pokemonserver.repos.UserRepository;
import com.khadir.pokemonserver.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User updateUser(UserDto userDto, Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(Long id) {
		// TODO Auto-generated method stub
		
	}

    // Implement other methods...
}
