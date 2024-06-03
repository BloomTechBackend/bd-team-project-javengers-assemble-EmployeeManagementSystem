package org.example.exceptions;

public class AccountLockedException extends RuntimeException {
    /**
     * Exception with no message or cause.
     */
    public AccountLockedException() {
        super();
    }

    /**
     * Exception with a message, but no cause.
     * @param message A descriptive message for this exception.
     */
    public AccountLockedException(String message) {
        super(message);
    }

    /**
     * Exception with no message, but with a cause.
     * @param cause The original throwable resulting in this exception.
     */
    public AccountLockedException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception with message and cause.
     * @param message A descriptive message for this exception.
     * @param cause The original throwable resulting in this exception.
     */
    public AccountLockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
