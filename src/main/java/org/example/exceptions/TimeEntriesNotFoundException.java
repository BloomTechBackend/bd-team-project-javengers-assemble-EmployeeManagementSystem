package org.example.exceptions;

public class TimeEntriesNotFoundException extends RuntimeException {
    /**
     * Exception with no message or cause.
     */
    public TimeEntriesNotFoundException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public TimeEntriesNotFoundException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public TimeEntriesNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public TimeEntriesNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
