package org.example.model;


import org.example.exceptions.InvalidInputFormatError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    Employee validEmployee;

    @BeforeEach
    public void setUp() {
        validEmployee = Employee.builder()
                .withFirstName("First")
                .withLastName("Last")
                .build();
    }

    @Test
    public void constructor_withoutFirstName_throwsInvalidInputFormatException(){
        Exception exception = assertThrows(InvalidInputFormatError.class, () -> {
            Employee employee = Employee.builder()
                                        .withLastName("Test")
                                        .build();
        });
    }

    @Test
    public void constructor_withoutLastName_throwsInvalidInputFormatException(){
        Exception exception = assertThrows(InvalidInputFormatError.class, () -> {
            Employee employee = Employee.builder()
                    .withFirstName("Test")
                    .build();
        });
    }

    @Test
    public void constructor_constructsValidEmployee() {
        String firstName = "First";
        String lastName = "Last";
        String middleName = "Middle";

        Employee employee = Employee.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withMiddleName(middleName)
                .build();

        assertNotNull(employee.getEmployeeId(), "Employee ID should not be null!");
        assertNotNull(employee.getFirstName(), "First Name Should Not Be Null!");
        assertNotNull(employee.getLastName(), "Last Name Should Not Be Null!");
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(middleName, employee.getMiddleName());
    }

    @Test
    public void setMiddleName_updatesMiddleName() {
        String newMiddleName = "Middle";
        String currentMiddleName = validEmployee.getMiddleName();

        validEmployee.setMiddleName(newMiddleName);

        assertNotNull(validEmployee.getMiddleName());

    }

}
