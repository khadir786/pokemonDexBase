package com.khadir.pokemonserver.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
	private Long id;
    private String username;
	private String password;
}


