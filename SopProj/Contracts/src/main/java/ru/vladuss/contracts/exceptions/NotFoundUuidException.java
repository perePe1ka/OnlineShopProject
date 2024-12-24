package ru.vladuss.contracts.exceptions;

public class NotFoundUuidException extends RuntimeException {

    public NotFoundUuidException(String message) {
        super(message);
    }

    public NotFoundUuidException(String message, Throwable cause) {
        super(message, cause);
    }
}
