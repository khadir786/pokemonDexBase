package com.khadir.pokemonserver.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String avatar;
    private String partnerPokemon;
    // Add other fields that you want to include in the response
}
