package com.khadir.pokemonserver.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    // add more constructors if needed, e.g. include cause of the exception
}

