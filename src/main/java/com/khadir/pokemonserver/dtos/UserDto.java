package com.khadir.pokemonserver.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
	private Long id;
	@NotEmpty
    private String username;
	@NotEmpty
	private String password;
}


