package org.example.model;

import java.time.LocalDate;

import org.apache.commons.codec.binary.Base32;
import org.example.exceptions.InvalidInputFormatError;
import org.example.utils.StringFormatValidator;

import java.util.UUID;

public class Employee {
    private static final LocalDate NULL_DATE = LocalDate.of(9999, 1, 1);
    private final String employeeId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String department;
    private LocalDate hireDate;
    private boolean currentlyEmployed;
    private LocalDate terminatedDate;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String payRate;
    private PermissionLevel permissionAccess;

    private Employee(EmployeeBuilder employeeBuilder) {
        this.employeeId = employeeBuilder.employeeId;
        this.firstName = employeeBuilder.firstName;
        this.lastName = employeeBuilder.lastName;
        this.middleName = employeeBuilder.middleName;
        this.email = employeeBuilder.email;
        this.department = employeeBuilder.department;
        this.hireDate = employeeBuilder.hireDate;
        this.currentlyEmployed = employeeBuilder.currentlyEmployed;
        this.terminatedDate = employeeBuilder.terminatedDate;
        this.phone = employeeBuilder.phone;
        this.address = employeeBuilder.address;
        this.city = employeeBuilder.city;
        this.state = employeeBuilder.state;
        this.zipCode = employeeBuilder.zipCode;
        this.payRate = employeeBuilder.payRate;
        this.permissionAccess = employeeBuilder.permissionAccess;
    }

    public static EmployeeBuilder builder() {
        return new EmployeeBuilder();
    }

    public static class EmployeeBuilder {
        private static final Base32 base32 = new Base32();
        private final String employeeId; // auto generated
        private String firstName; // required
        private String lastName; // required
        private String middleName;
        private String email;
        private String department;
        private LocalDate hireDate;
        private boolean currentlyEmployed; // default true
        private LocalDate terminatedDate;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String payRate;
        private PermissionLevel permissionAccess; // default STANDARD

        public EmployeeBuilder() {
            UUID uuid = UUID.randomUUID();
            byte[] uuidBytes = toBytes(uuid);
            this.employeeId = base32.encodeAsString(uuidBytes).substring(0, 8);  // Generate unique employee ID
            this.currentlyEmployed = true;
            this.permissionAccess = PermissionLevel.STANDARD;
        }

        private static byte[] toBytes(UUID uuid) {
            byte[] bytes = new byte[16];
            long msb = uuid.getMostSignificantBits();
            long lsb = uuid.getLeastSignificantBits();

            for (int i = 0; i < 8; i++) {
                bytes[i] = (byte) (msb >>> 8 * (7 - i));
                bytes[8 + i] = (byte) (lsb >>> 8 * (7 - i));
            }
            return bytes;
        }

        public EmployeeBuilder withFirstName(String firstName) {
            if (firstName == null || !StringFormatValidator.validNameFormat(firstName)) {
                if (firstName == null) {
                    throw new InvalidInputFormatError("First name cannot be null.");
                }
                throw new InvalidInputFormatError("Invalid first name format: " + firstName);
            }
            this.firstName = firstName;
            return this;
        }

        public EmployeeBuilder withLastName(String lastName) {
            if (lastName == null || !StringFormatValidator.validNameFormat(lastName)) {
                if (lastName == null) {
                    throw new InvalidInputFormatError("Last name cannot be null.");
                }
                throw new InvalidInputFormatError("Invalid last name format: " + lastName);
            }
            this.lastName = lastName;
            return this;
        }

        public EmployeeBuilder withMiddleName(String middleName) {
            if (middleName != null && !StringFormatValidator.validNameFormat(middleName)) {
                throw new InvalidInputFormatError("Invalid middle name format: " + middleName);
            }
            this.middleName = middleName;
            return this;
        }

        public EmployeeBuilder withEmail(String email) {
            if (email != null && !StringFormatValidator.validEmailFormat(email)) {
                throw new InvalidInputFormatError("Invalid email format: " + email);
            }
            this.email = email;
            return this;
        }

        public EmployeeBuilder withDepartment(String department) {
            this.department = department;
            return this;
        }

        public EmployeeBuilder withHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public EmployeeBuilder withCurrentlyEmployed(boolean currentlyEmployed) {
            this.currentlyEmployed = currentlyEmployed;
            return this;
        }

        public EmployeeBuilder withTerminatedDate(LocalDate terminatedDate) {
            this.terminatedDate = terminatedDate;
            return this;
        }

        public EmployeeBuilder withPhone(String phone) {
            if (phone != null && !StringFormatValidator.validUsPhoneFormat(phone)) {
                throw new InvalidInputFormatError(String.format("Invalid phone format: %s. Expected format: +1-xxx-xxx-xxxx", phone));
            }
            this.phone = phone;
            return this;
        }

        public EmployeeBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EmployeeBuilder withCity(String city) {
            if (city != null && !StringFormatValidator.validNameFormat(city)) {
                throw new InvalidInputFormatError("Invalid city name format: " + city);
            }
            this.city = city;
            return this;
        }

        public EmployeeBuilder withState(String state) {
            if (state != null && !StringFormatValidator.validNameFormat(state)) {
                throw new InvalidInputFormatError("Invalid state name format: " + state);
            }
            this.state = state;
            return this;
        }

        public EmployeeBuilder withZipCode(String zipCode) {
            if (zipCode != null && !StringFormatValidator.validUnitedStatesZipCodeFormat(zipCode)) {
                throw new InvalidInputFormatError("Invalid zip code format: " + zipCode);
            }
            this.zipCode = zipCode;
            return this;
        }

        public EmployeeBuilder withPayRate(String payRate) {
            if (payRate != null && !StringFormatValidator.validPayrateFormat(payRate)) {
                throw new InvalidInputFormatError("Invalid pay rate format: " + payRate);
            }
            this.payRate = payRate;
            return this;
        }

        public EmployeeBuilder withPermissionAccess(PermissionLevel permissionAccess) {
            this.permissionAccess = permissionAccess;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isCurrentlyEmployed() {
        return currentlyEmployed;
    }

    public void setCurrentlyEmployed(boolean currentlyEmployed) {
        this.currentlyEmployed = currentlyEmployed;
    }

    public LocalDate getTerminatedDate() {
        return terminatedDate;
    }

    public void setTerminatedDate(LocalDate terminatedDate) {
        this.terminatedDate = terminatedDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPayRate() {
        return payRate;
    }

    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    public PermissionLevel getPermissionAccess() {
        return permissionAccess;
    }

    public void setPermissionAccess(PermissionLevel permissionAccess) {
        this.permissionAccess = permissionAccess;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "\nemployeeId='" + employeeId + '\'' +
                ", \nfirstName='" + firstName + '\'' +
                ", \nlastName='" + lastName + '\'' +
                ", \nmiddleName='" + (middleName != null ? middleName : "") + '\'' +
                ", \nemail='" + (email != null ? email : "") + '\'' +
                ", \ndepartment='" + (department != null ? department : "") + '\'' +
                ", \nhireDate=" + (hireDate != null ? hireDate : NULL_DATE) +
                ", \ncurrentlyEmployed=" + currentlyEmployed +
                ", \nterminatedDate=" + (terminatedDate != null ? terminatedDate : NULL_DATE) +
                ", \nphone='" + (phone != null ? phone : "") + '\'' +
                ", \naddress='" + (address != null ? address : "") + '\'' +
                ", \ncity='" + (city != null ? city : "") + '\'' +
                ", \nstate='" + (state != null ? state : "") + '\'' +
                ", \nzipCode='" + (zipCode != null ? zipCode : "") + '\'' +
                ", \npayRate='" + (payRate != null ? payRate : "") + '\'' +
                ", \npermissionAccess=" + permissionAccess +
                "\n}";
    }
}
