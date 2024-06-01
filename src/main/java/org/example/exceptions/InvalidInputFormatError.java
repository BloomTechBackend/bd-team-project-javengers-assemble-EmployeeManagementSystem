package org.example.exceptions;

public class InvalidInputFormatError extends IllegalArgumentException {

    public InvalidInputFormatError() {
        super("Invalid ID.");
    }

    public InvalidInputFormatError(String message) {
        super(message);
    }

    public InvalidInputFormatError(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputFormatError(Throwable cause) {
        super(cause);
    }
}
