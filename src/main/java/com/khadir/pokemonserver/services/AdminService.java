package com.khadir.pokemonserver.services;

import java.util.List;

import com.khadir.pokemonserver.dtos.UserDto;
import com.khadir.pokemonserver.models.User;

public interface AdminService {
    User getUserById(Long id);
    User updateUser(UserDto userDto, Long id);
    void deleteUser(Long id);
    void addRoleToUser(String username, String role);
    List<UserDto> findAllUsers();
}
