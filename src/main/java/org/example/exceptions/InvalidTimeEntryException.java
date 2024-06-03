package org.example.exceptions;

public class InvalidTimeEntryException extends RuntimeException {
    public InvalidTimeEntryException() {
        super("Invalid EmployeeModel.");
    }

    public InvalidTimeEntryException(String message) {
        super(message);
    }

    public InvalidTimeEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTimeEntryException(Throwable cause) {
        super(cause);
    }
}
