package org.example.exceptions;

public class InvalidInputFormatException extends IllegalArgumentException {

    public InvalidInputFormatException() {
        super("Invalid ID.");
    }

    public InvalidInputFormatException(String message) {
        super(message);
    }

    public InvalidInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFormatException(Throwable cause) {
        super(cause);
    }
}
