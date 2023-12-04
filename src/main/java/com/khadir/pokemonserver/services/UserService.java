package com.khadir.pokemonserver.services;

import java.util.List;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.exceptions.UserAlreadyExistsException;
import com.khadir.pokemonserver.models.User;

public interface UserService {
    User registerNewUser(UserDto userDto) throws UserAlreadyExistsException;
    User getUserById(Long id);
    User updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
    // Other user-related methods...
    List<UserDto> findAllUsers();
}

