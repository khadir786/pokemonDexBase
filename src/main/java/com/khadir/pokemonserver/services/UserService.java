package com.khadir.pokemonserver.services;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.models.User;

public interface UserService {
    User registerNewUserAccount(UserDto userDto);
    User getUserById(Long id);
    User updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
    
    // Other user-related methods...
}

