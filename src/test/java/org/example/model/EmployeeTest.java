package org.example.model;


import org.example.exceptions.InvalidInputFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    /**
     * Contains the bare minimum requirements for a valid employee.
     */
    Employee validBaseEmployee;

    /**
     * Employee with all fields populated.
     */
    Employee validCompleteEmployee;

    @BeforeEach
    public void setUp() {
        validBaseEmployee = Employee.builder()
                .withFirstName("First")
                .withLastName("Last")
                .build();

        validCompleteEmployee = Employee.builder()
                .withFirstName("First")
                .withLastName("Last")
                .withMiddleName("Middle")
                .withEmail("email@email.com")
                .withDepartment("Department")
                .withHireDate(LocalDate.of(2024, 01, 01))
                .withCurrentlyEmployed(false)
                .withTerminatedDate(LocalDate.of(2024, 2, 1))
                .withPhone("+1-999-999-9999")
                .withAddress("123 Address Dr")
                .withCity("City")
                .withState("State")
                .withZipCode("12345-1234")
                .withPayRate("19.25/hr")
                .build();
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
    public void constructor_withoutFirstName_throwsInvalidInputFormatException(){
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee employee = new Employee.EmployeeBuilder()
                    .withLastName("Test")
                    .build();
        });
    }

    @Test
    public void constructor_withoutLastName_throwsInvalidInputFormatException(){
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee employee = Employee.builder()
                    .withFirstName("Test")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidMiddleName_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withMiddleName("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidEmail_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withEmail("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidPhoneNumber_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withPhone("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidCityName_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withCity("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidStateName_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withState("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidZipCode_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withZipCode("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void constructor_WithInvalidPayRate_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            Employee.builder()
                    .withFirstName("Name")
                    .withLastName("Name")
                    .withPayRate("Invalid12354!")
                    .build();
        });
    }

    @Test
    public void getFirstName_returnsCorrectFirstName() {
        String expected = "First";

        assertEquals(expected, validBaseEmployee.getFirstName());
    }

    @Test
    public void setFirstName_withValidFormat_updatesFirstName() {
        String currentFirstName = validBaseEmployee.getFirstName();

        validBaseEmployee.setFirstName("NewFirstName");

        assertNotEquals(validBaseEmployee.getFirstName(), currentFirstName);
    }

    @Test
    public void setFirstName_withNullValue_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setFirstName(null);
        });
    }

    @Test
    public void setFirstName_withInvalidFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setFirstName("Name123!");
        });
    }

    @Test
    public void getLastName_returnsCorrectFirstName() {
        String expected = "Last";

        assertEquals(expected, validBaseEmployee.getLastName());
    }

    @Test
    public void setLastName_withValidFormat_updatesFirstName() {
        String currentLastName = validBaseEmployee.getLastName();

        validBaseEmployee.setLastName("NewLastName");

        assertNotEquals(validBaseEmployee.getLastName(), currentLastName);
    }

    @Test
    public void setLastName_withNullValue_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setLastName(null);
        });
    }

    @Test
    public void setLastName_withInvalidFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setLastName("Name123!");
        });
    }


    @Test
    public void setMiddleName_updatesMiddleName() {
        String newMiddleName = "Middle";
        String currentMiddleName = validBaseEmployee.getMiddleName();

        validBaseEmployee.setMiddleName(newMiddleName);

        assertNotNull(validBaseEmployee.getMiddleName());

    }

    @Test
    public void setMiddleName_withInvalidFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setMiddleName("Name123!");
        });
    }

    @Test
    public void getMiddleName_returnsCorrectMiddleName() {
        String expected = "Middle";

        assertEquals(expected, validCompleteEmployee.getMiddleName());
    }

    @Test
    public void getEmail_returnsCorrectEmail() {
        String expected = "email@email.com";

        assertEquals(expected, validCompleteEmployee.getEmail());
    }

    @Test
    public void setEmail_withInvalidEmailFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setEmail("email#email.com");
        });
    }

    @Test
    public void setEmail_withValidEmailFormat_updatesEmail() {
        String originalEmail = validCompleteEmployee.getEmail();
        String newEmail = "new_email@email.com";

        validCompleteEmployee.setEmail(newEmail);

        assertNotEquals(originalEmail, validCompleteEmployee.getEmail());
        assertEquals(newEmail, validCompleteEmployee.getEmail());

    }

    @Test
    public void setPhone_withValidPhoneFormat_updatesPhone() {
        String originalPhone = validCompleteEmployee.getPhone();
        String newPhone = "+1-543-210-1234";

        validCompleteEmployee.setPhone(newPhone);

        assertNotEquals(originalPhone, validCompleteEmployee.getPhone());
        assertEquals(newPhone, validCompleteEmployee.getPhone());
    }

    @Test
    public void setPhone_withInvalidPhoneFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setPhone("+2 (123) 459-1235");
        });
    }

    @Test
    public void setCity_withValidCityFormat_updatesCity() {
        String originalCity = validCompleteEmployee.getCity();
        String newCity = "New City";

        validCompleteEmployee.setCity(newCity);

        assertNotEquals(originalCity, validCompleteEmployee.getCity());
        assertEquals(newCity, validCompleteEmployee.getCity());
    }

    @Test
    public void setCity_withInvalidCityFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setCity("Invalid  City Name");
        });
    }

    @Test
    public void setState_withValidStateFormat_updatesState() {
        String originalState = validCompleteEmployee.getState();
        String newState = "New State";

        validCompleteEmployee.setState(newState);

        assertNotEquals(originalState, validCompleteEmployee.getState());
        assertEquals(newState, validCompleteEmployee.getState());
    }

    @Test
    public void setState_withInvalidStateFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setState("Invalid-- State Name");
        });
    }

    @Test
    public void setZipCode_withValidZipCodeFormat_updatesZipCode() {
        String originalZipCode = validCompleteEmployee.getZipCode();
        String newZipCode = "54321";

        validCompleteEmployee.setZipCode(newZipCode);

        assertNotEquals(originalZipCode, validCompleteEmployee.getZipCode());
        assertEquals(newZipCode, validCompleteEmployee.getZipCode());
    }

    @Test
    public void setZipCode_withInvalidZipCodeFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setZipCode("Inval-Zip1");
        });
    }

    @Test
    public void setPayRate_withValidPayRateFormat_updatesPayRate() {
        String originalPayRate = validCompleteEmployee.getPayRate();
        String newPayRate = "6250/bw";

        validCompleteEmployee.setPayRate(newPayRate);

        assertNotEquals(originalPayRate, validCompleteEmployee.getPayRate());
        assertEquals(newPayRate, validCompleteEmployee.getPayRate());
    }

    @Test
    public void setPayRate_withInvalidPayRateFormat_throwsInvalidInputFormatException() {
        assertThrows(InvalidInputFormatException.class, () -> {
            validBaseEmployee.setPayRate("6250/mon");
        });
    }

    @Test
    public void toString_returnsCorrectStringRepresentation() {
        String expectedString = "Employee{" +
                "\nemployeeId='" + validBaseEmployee.getEmployeeId() + '\'' +
                ", \nfirstName='First'" +
                ", \nlastName='Last'" +
                ", \nmiddleName=''" +
                ", \nemail=''" +
                ", \ndepartment=''" +
                ", \nhireDate=9999-01-01" +
                ", \ncurrentlyEmployed=true" +
                ", \nterminatedDate=9999-01-01" +
                ", \nphone=''" +
                ", \naddress=''" +
                ", \ncity=''" +
                ", \nstate=''" +
                ", \nzipCode=''" +
                ", \npayRate=''" +
                ", \npermissionAccess=Standard" +
                "\n}";

        assertEquals(expectedString, validBaseEmployee.toString());
    }

}
