package org.example.exceptions;

public class InvalidEmployeeCredentialsModelException extends RuntimeException {
    /**
     * Exception with no message or cause.
     */
    public InvalidEmployeeCredentialsModelException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public InvalidEmployeeCredentialsModelException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidEmployeeCredentialsModelException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public InvalidEmployeeCredentialsModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
