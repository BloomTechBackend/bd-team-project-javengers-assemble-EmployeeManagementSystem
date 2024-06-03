package org.example.exceptions;

public class InvalidEmployeeException extends RuntimeException {
    public InvalidEmployeeException() {
        super("Invalid Employee.");
    }

    public InvalidEmployeeException(String message) {
        super(message);
    }

    public InvalidEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmployeeException(Throwable cause) {
        super(cause);
    }
}
