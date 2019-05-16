package ru.com.testdribbble.core.exception;

public class FieldInvalidException extends RuntimeException {

    public FieldInvalidException() {
    }

    public FieldInvalidException(String message) {
        super(message);
    }
}
