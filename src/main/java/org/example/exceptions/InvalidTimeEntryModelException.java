package org.example.exceptions;

public class InvalidTimeEntryModelException extends RuntimeException{
    public InvalidTimeEntryModelException() {
        super("Invalid EmployeeModel.");
    }

    public InvalidTimeEntryModelException(String message) {
        super(message);
    }

    public InvalidTimeEntryModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTimeEntryModelException(Throwable cause) {
        super(cause);
    }
}
