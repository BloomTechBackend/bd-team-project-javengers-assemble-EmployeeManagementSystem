package org.example.exceptions;

public class InvalidEmployeeCredentialsException extends RuntimeException {
    /**
     * Exception with no message or cause.
     */
    public InvalidEmployeeCredentialsException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public InvalidEmployeeCredentialsException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidEmployeeCredentialsException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidEmployeeCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
