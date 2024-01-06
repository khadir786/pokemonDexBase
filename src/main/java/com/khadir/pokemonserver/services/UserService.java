package com.khadir.pokemonserver.services;

import java.util.List;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.dtos.UserResponseDto;
import com.khadir.pokemonserver.exceptions.UserAlreadyExistsException;
import com.khadir.pokemonserver.models.User;

public interface UserService {
    User registerNewUser(UserDto userDto) throws UserAlreadyExistsException;
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
    List<UserResponseDto> findAllUsers();
    UserResponseDto getUserByUsername(String username);
}

