package com.tfg_rm.backend_restaurantmanager.shared.exception;

/**
 * Custom exception thrown when user credentials are invalid during authentication.
 * Extends RuntimeException to allow for unchecked exceptions, which can be handled globally in the application.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
