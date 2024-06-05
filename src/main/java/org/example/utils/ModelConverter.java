package org.example.utils;

import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.dynamodb.model.EmployeeModel;
import org.example.dynamodb.model.TimeEntryModel;
import org.example.exceptions.*;
import org.example.model.Employee;
import org.example.model.EmployeeCredentials;
import org.example.model.PermissionLevel;
import org.example.model.TimeEntry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private ModelConverter() {}

    /**
     * Converts an EmployeeModel object to an Employee object.
     *
     * @param employeeModel the EmployeeModel object to convert
     * @return the converted Employee object
     */
    public static Employee fromEmployeeModel(EmployeeModel employeeModel) {
        if (employeeModel == null) {
            throw new InvalidEmployeeModelException("EmployeeModel cannot be null!");
        }

        return Employee.builder()
                .withEmployeeId(employeeModel.getEmployeeId())
                .withFirstName(employeeModel.getFirstName())
                .withLastName(employeeModel.getLastName())
                .withMiddleName(employeeModel.getMiddleName())
                .withEmail(employeeModel.getEmail())
                .withDepartment(employeeModel.getDepartment())
                .withHireDate(convertStringToLocalDate(employeeModel.getHireDate()))
                .withCurrentlyEmployed(employeeModel.isCurrentlyEmployed())
                .withTerminatedDate(convertStringToLocalDate(employeeModel.getTerminatedDate()))
                .withPhone(employeeModel.getPhone())
                .withAddress(employeeModel.getAddress())
                .withCity(employeeModel.getCity())
                .withState(employeeModel.getState())
                .withZipCode(employeeModel.getZipCode())
                .withPayRate(employeeModel.getPayRate())
                .withPermissionAccess(PermissionLevel.valueOf(employeeModel.getPermissionAccess().toUpperCase()))
                .build();
    }

    /**
     * Converts a list of EmployeeModel objects to a list of Employee objects.
     *
     * @param employeeModelList the list of EmployeeModel objects to convert
     * @return the list of converted Employee objects
     */
    public static List<Employee> fromEmployeeModelList(List<EmployeeModel> employeeModelList) {
        List<Employee> employeeList = new ArrayList<>();

        if (employeeModelList == null) {
            throw new InvalidEmployeeModelException("List of EmployeeModels cannot be null!");
        }

        for (EmployeeModel employeeModel : employeeModelList) {
            employeeList.add(fromEmployeeModel(employeeModel));
        }
        return employeeList;
    }

    /**
     * Converts an Employee object to an EmployeeModel object.
     *
     * @param employee the Employee object to convert
     * @return the converted EmployeeModel object
     */
    public static EmployeeModel fromEmployee(Employee employee) {
        if (employee == null) {
            throw new InvalidEmployeeException("Employee cannot be null!");
        }

        EmployeeModel employeeModel = new EmployeeModel();

        employeeModel.setEmployeeId(employee.getEmployeeId());
        employeeModel.setFirstName(employee.getFirstName());
        employeeModel.setLastName((employee.getLastName()));
        employeeModel.setMiddleName(employee.getMiddleName());
        employeeModel.setEmail(employee.getEmail());
        employeeModel.setDepartment(employee.getDepartment());
        employeeModel.setHireDate(convertFromLocalDateToString(employee.getHireDate()));
        employeeModel.setCurrentlyEmployed(employee.isCurrentlyEmployed());
        employeeModel.setTerminatedDate(convertFromLocalDateToString(employee.getTerminatedDate()));
        employeeModel.setPhone(employee.getPhone());
        employeeModel.setAddress(employee.getAddress());
        employeeModel.setCity(employee.getCity());
        employeeModel.setState(employee.getState());
        employeeModel.setZipCode(employee.getZipCode());
        employeeModel.setPayRate(employee.getPayRate());
        employeeModel.setPermissionAccess(employee.getPermissionAccess().toString());

        return employeeModel;
    }

    /**
     * Converts a list of Employee objects to a list of EmployeeModel objects.
     *
     * @param employeeList the list of Employee objects to convert
     * @return the list of converted EmployeeModel objects
     */
    public static List<EmployeeModel> fromEmployeeList(List<Employee> employeeList) {
        List<EmployeeModel> employeeModelList = new ArrayList<>();

        if (employeeList == null) {
            throw new InvalidEmployeeException("List of Employees cannot be null!");
        }

        for (Employee employee : employeeList) {
            employeeModelList.add(fromEmployee(employee));
        }

        return employeeModelList;
    }

    /**
     * Converts a TimeEntryModel object to a TimeEntry object.
     *
     * @param timeEntryModel the TimeEntryModel object to convert
     * @return the converted TimeEntry object
     */
    public static TimeEntry fromTimeEntryModel(TimeEntryModel timeEntryModel) {
        if (timeEntryModel == null) {
            throw new InvalidTimeEntryModelException("TimeEntryModel cannot be null!");
        }

        LocalDateTime timeIn = convertStringToLocalDateTime(timeEntryModel.getTimeIn());
        LocalDateTime timeOut = convertStringToLocalDateTime(timeEntryModel.getTimeOut());
        return TimeEntry.builder()
                .withEmployeeId(timeEntryModel.getEmployeeId())
                .withEntryId(timeEntryModel.getEntryId())
                .withTimeIn(timeIn)
                .withTimeOut(timeOut)
                .withDuration(timeEntryModel.getDuration())
                .build();
    }

    /**
     * Converts a list of TimeEntryModel objects to a list of TimeEntry objects.
     *
     * @param timeEntryModelList the list of TimeEntryModel objects to convert
     * @return the list of converted TimeEntry objects
     */
    public static List<TimeEntry> fromTimeEntryModelList(List<TimeEntryModel> timeEntryModelList) {
        List<TimeEntry> timeEntryList = new ArrayList<>();

        if (timeEntryModelList == null) {
            throw new InvalidTimeEntryModelException("List of TimeEntryModels cannot be nulL!");
        }

        for (TimeEntryModel timeEntryModel : timeEntryModelList) {
            timeEntryList.add(fromTimeEntryModel(timeEntryModel));
        }

        return timeEntryList;
    }

    /**
     * Converts a TimeEntry object to a TimeEntryModel object.
     *
     * @param timeEntry the TimeEntry object to convert
     * @return the converted TimeEntryModel object
     */
    public static TimeEntryModel fromTimeEntry(TimeEntry timeEntry) {
        if (timeEntry == null) {
            throw new InvalidTimeEntryException("TimeEntry cannot be null!");
        }
        TimeEntryModel timeEntryModel = new TimeEntryModel();
        LocalDateTime timeIn = timeEntry.getTimeIn();
        LocalDateTime timeOut = timeEntry.getTimeOut();

        timeEntryModel.setEmployeeId(timeEntry.getEmployeeId());
        timeEntryModel.setEntryId(timeEntry.getEntryId());
        timeEntryModel.setTimeIn(timeIn != null ? timeIn.toString() : null);
        timeEntryModel.setTimeOut(timeOut != null ? timeOut.toString() : null);
        timeEntryModel.setDuration(timeEntry.getDuration());

        return timeEntryModel;
    }

    /**
     * Converts a list of TimeEntry objects to a list of TimeEntryModel objects.
     *
     * @param timeEntryList the list of TimeEntry objects to convert
     * @return the list of converted TimeEntryModel objects
     */
    public static List<TimeEntryModel> fromTimeEntryList(List<TimeEntry> timeEntryList) {
        List<TimeEntryModel> timeEntryModelList = new ArrayList<>();
        if (timeEntryList == null) {
            throw new InvalidTimeEntryException("List of TimeEntries cannot be null!");
        }

        for (TimeEntry timeEntry : timeEntryList) {
            timeEntryModelList.add(fromTimeEntry(timeEntry));
        }

        return timeEntryModelList;
    }

    /**
     * Converts an {@link EmployeeCredentialsModel} object to an {@link EmployeeCredentials} object.
     * This method takes the data from an {@code EmployeeCredentialsModel} and constructs an equivalent
     * {@code EmployeeCredentials} object, converting the {@code lastUpdated} string to a {@code LocalDateTime}.
     *
     * @param employeeCredentialsModel the {@link EmployeeCredentialsModel} object to be converted
     * @return the converted {@link EmployeeCredentials} object
     */
    public static EmployeeCredentials fromEmployeeCredentialsModel(EmployeeCredentialsModel employeeCredentialsModel) {
        if (employeeCredentialsModel == null) {
            throw new InvalidEmployeeCredentialsModelException("EmployeeCredentialsModel cannot be null!");
        }

        LocalDateTime lastUpdated = convertStringToLocalDateTime(employeeCredentialsModel.getLastUpdated());

        return new EmployeeCredentials(employeeCredentialsModel.getEmployeeId(), employeeCredentialsModel.getUsername(),
                employeeCredentialsModel.getSalt(), employeeCredentialsModel.getPassword(), lastUpdated,
                employeeCredentialsModel.isAccountLocked(), employeeCredentialsModel.isForceChangeAfterLogin(),
                employeeCredentialsModel.getFailedAttempts());
    }

    /**
     * Converts an {@link EmployeeCredentials} object to an {@link EmployeeCredentialsModel} object.
     * This method takes the data from an {@code EmployeeCredentials} and constructs an equivalent
     * {@code EmployeeCredentialsModel} object. If the provided {@code employeeCredentials} is null,
     * an {@code InvalidEmployeeCredentialsException} is thrown.
     *
     * @param employeeCredentials the {@link EmployeeCredentials} object to be converted
     * @return the converted {@link EmployeeCredentialsModel} object
     * @throws InvalidEmployeeCredentialsException if the provided {@link EmployeeCredentials} is null
     */
    public static EmployeeCredentialsModel fromEmployeeCredentials(EmployeeCredentials employeeCredentials) {
        if (employeeCredentials == null) {
            throw new InvalidEmployeeCredentialsException("EmployeeCredentials cannot be null!");
        }

        EmployeeCredentialsModel employeeCredentialsModel = new EmployeeCredentialsModel();

        employeeCredentialsModel.setEmployeeId(employeeCredentials.getEmployeeId());
        employeeCredentialsModel.setUsername(employeeCredentials.getUsername());
        employeeCredentialsModel.setSalt(employeeCredentials.getSalt());
        employeeCredentialsModel.setPassword(employeeCredentials.getPassword());
        employeeCredentialsModel.setLastUpdated(employeeCredentials.getLastUpdated().toString());
        employeeCredentialsModel.setAccountLocked(employeeCredentials.isAccountLocked());
        employeeCredentialsModel.setForceChangeAfterLogin(employeeCredentials.isForceChangeAfterLogin());
        employeeCredentialsModel.setFailedAttempts(employeeCredentials.getFailedAttempts());

        return employeeCredentialsModel;
    }

    /**
     * Converts a String to a LocalDate.
     *
     * @param localDateString the String to convert
     * @return the converted LocalDate, or null if the input string is null
     */
    public static LocalDate convertStringToLocalDate(String localDateString) {
        return localDateString != null ? LocalDate.parse(localDateString, DATE_FORMATTER) : null;
    }

    /**
     * Converts a LocalDate to a String.
     *
     * @param date the LocalDate to convert
     * @return the converted String, or null if the input date is null
     */
    private static String convertFromLocalDateToString(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    /**
     * Converts a String to a LocalDateTime.
     *
     * @param localDateTimeString the String to convert
     * @return the converted LocalDateTime, or null if the input string is null
     */
    public static LocalDateTime convertStringToLocalDateTime(String localDateTimeString) {
        return localDateTimeString != null ? LocalDateTime.parse(localDateTimeString, DATE_TIME_FORMATTER) : null;
    }

}
