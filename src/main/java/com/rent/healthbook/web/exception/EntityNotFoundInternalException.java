package com.rent.healthbook.web.exception;

public class EntityNotFoundInternalException extends RuntimeException {
    public EntityNotFoundInternalException(String message) {
        super(message);
    }
}
