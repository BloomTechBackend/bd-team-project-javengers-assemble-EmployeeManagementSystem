package org.example.exceptions;

public class InvalidEmployeeModelException extends RuntimeException {
    public InvalidEmployeeModelException() {
        super("Invalid EmployeeModel.");
    }

    public InvalidEmployeeModelException(String message) {
        super(message);
    }

    public InvalidEmployeeModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmployeeModelException(Throwable cause) {
        super(cause);
    }
}
