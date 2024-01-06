package com.khadir.pokemonserver.dtos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate dob;
	private String avatar;
    private String partnerPokemon;
}


