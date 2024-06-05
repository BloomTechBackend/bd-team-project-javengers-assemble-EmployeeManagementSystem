package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.EmployeeDao;
import org.example.exceptions.UsernameAlreadyExistsException;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.Employee;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.NewEmployeeRequest;
import org.example.model.results.NewEmployeeResult;

import javax.inject.Inject;

public class NewEmployeeHandler implements RequestHandler<NewEmployeeRequest, NewEmployeeResult> {
    private static final Logger log = LogManager.getLogger(NewEmployeeHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    @Inject
    EmployeeDao employeeDao;

    public NewEmployeeHandler() {
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public NewEmployeeResult handleRequest(final NewEmployeeRequest request, Context context) {
        try {
            String formattedUsername = request.getUsername();
            // Verify Username Doesn't Exist in Table
            if (usernameExists(formattedUsername)) {
                throw new UsernameAlreadyExistsException(String.format("Username \"%s\" is already taken. Please choose another.", request.getUsername()));
            }

            Employee newEmployee = Employee.builder()
                    .withFirstName(request.getFirstName())
                    .withLastName(request.getLastName())
                    .withMiddleName(request.getMiddleName())
                    .withEmail(request.getEmail())
                    .withDepartment(request.getDepartment())
                    .withHireDate(request.getHireDate())
                    .withPhone(request.getPhone())
                    .withAddress(request.getAddress())
                    .withCity(request.getCity())
                    .withState(request.getState())
                    .withZipCode(request.getZipCode())
                    .withPayRate(request.getPayRate())
                    .withPermissionAccess(request.getPermissionAccess())
                    .build();

            EmployeeCredentials employeeCredentials = new EmployeeCredentials(
                    newEmployee.getEmployeeId(),
                    formattedUsername,
                    request.getPassword()
            );

            employeeDao.saveEmployee(newEmployee);
            credentialsDao.saveEmployeeCredentials(employeeCredentials);

            return NewEmployeeResult.build()
                    .withNewEmployeeCreated(true)
                    .withEmployeeId(newEmployee.getEmployeeId())
                    .withFirstName(newEmployee.getFirstName())
                    .withLastName(newEmployee.getLastName())
                    .withMiddleName(newEmployee.getMiddleName())
                    .withEmail(newEmployee.getEmail())
                    .withDepartment(newEmployee.getDepartment())
                    .withHireDate(newEmployee.getHireDate())
                    .withPhone(newEmployee.getPhone())
                    .withCity(newEmployee.getCity())
                    .withState(newEmployee.getState())
                    .withZipCode(newEmployee.getZipCode())
                    .withPayRate(newEmployee.getPayRate())
                    .withPermissionAccess(newEmployee.getPermissionAccess())
                    .withUsername(employeeCredentials.getUsername())
                    .withPassword(request.getPassword())
                    .build();
        } catch (UsernameAlreadyExistsException e) {
            log.warn("Username already exists. ", e);
            return NewEmployeeResult.build()
                    .withNewEmployeeCreated(false)
                    .withFirstName(request.getFirstName())
                    .withLastName(request.getLastName())
                    .withMiddleName(request.getMiddleName())
                    .withEmail(request.getEmail())
                    .withDepartment(request.getDepartment())
                    .withHireDate(request.getHireDate())
                    .withPhone(request.getPhone())
                    .withCity(request.getCity())
                    .withState(request.getState())
                    .withZipCode(request.getZipCode())
                    .withPayRate(request.getPayRate())
                    .withPermissionAccess(request.getPermissionAccess())
                    .withUsername(request.getUsername())
                    .withPassword(request.getPassword())
                    .withError(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("An unexpected error occurred while creating employee. ", e);
            return NewEmployeeResult.build()
                    .withNewEmployeeCreated(false)
                    .withFirstName(request.getFirstName())
                    .withLastName(request.getLastName())
                    .withMiddleName(request.getMiddleName())
                    .withEmail(request.getEmail())
                    .withDepartment(request.getDepartment())
                    .withHireDate(request.getHireDate())
                    .withPhone(request.getPhone())
                    .withCity(request.getCity())
                    .withState(request.getState())
                    .withZipCode(request.getZipCode())
                    .withPayRate(request.getPayRate())
                    .withPermissionAccess(request.getPermissionAccess())
                    .withUsername(request.getUsername())
                    .withPassword(request.getPassword())
                    .withError(e.getMessage())
                    .build();
        }
    }

    private boolean usernameExists(String username) {
        try {
            credentialsDao.getEmployeeCredentials(username);

            return true;
        } catch (UsernameNotFoundException e) {
            return false;
        }
    }
}
