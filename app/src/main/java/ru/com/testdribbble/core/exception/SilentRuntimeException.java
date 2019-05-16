package ru.com.testdribbble.core.exception;

public class SilentRuntimeException extends RuntimeException {

    public SilentRuntimeException() {
    }

    public SilentRuntimeException(String message) {
        super(message);
    }

    public SilentRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SilentRuntimeException(Throwable cause) {
        super(cause);
    }

    public SilentRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
