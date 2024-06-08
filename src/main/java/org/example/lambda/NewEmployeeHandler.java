package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeCredentialsDao;
import org.example.dynamodb.EmployeeDao;
import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.dynamodb.model.EmployeeModel;
import org.example.exceptions.UsernameAlreadyExistsException;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.Employee;
import org.example.model.EmployeeCredentials;
import org.example.model.requests.NewEmployeeRequest;
import org.example.model.results.NewEmployeeResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Handler for creating a new employee and stores the data to DynamoDB.
 * A new employee object is created, which generates a new employee ID.
 * The temporary credentials set by the administrator are stored in an EmployeeCredentials object.
 * This class implements the AWS Lambda RequestHandler interface to handle new employee creation requests.
 */
public class NewEmployeeHandler implements RequestHandler<NewEmployeeRequest, String> {
    private static final Logger log = LogManager.getLogger(NewEmployeeHandler.class);

    @Inject
    EmployeeCredentialsDao credentialsDao;

    @Inject
    EmployeeDao employeeDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public NewEmployeeHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to create a new an employee.
     *
     * @param request The request object containing the new employee details.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the new employee operation.
     */
    @Override
    public String handleRequest(final NewEmployeeRequest request, Context context) {
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

            EmployeeModel savedEmployee = employeeDao.saveEmployee(newEmployee);
            EmployeeCredentialsModel savedCredentials = credentialsDao.saveEmployeeCredentials(employeeCredentials);

            log.info(String.format("Successfully created new employee. " +
                    "\nEmployee ID: %s ", savedEmployee.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    NewEmployeeResult.build()
                            .withNewEmployeeCreated(true)
                            .withEmployeeId(savedEmployee.getEmployeeId())
                            .withFirstName(savedEmployee.getFirstName())
                            .withLastName(savedEmployee.getLastName())
                            .withMiddleName(savedEmployee.getMiddleName())
                            .withEmail(savedEmployee.getEmail())
                            .withDepartment(savedEmployee.getDepartment())
                            .withHireDate(savedEmployee.getHireDate())
                            .withPhone(savedEmployee.getPhone())
                            .withAddress(savedEmployee.getAddress())
                            .withCity(savedEmployee.getCity())
                            .withState(savedEmployee.getState())
                            .withZipCode(savedEmployee.getZipCode())
                            .withPayRate(savedEmployee.getPayRate())
                            .withPermissionAccess(savedEmployee.getPermissionAccess())
                            .withUsername(savedCredentials.getUsername())
                            .withPassword(request.getPassword())
                            .build()
            );
        } catch (UsernameAlreadyExistsException e) {
            log.warn("Username already exists. ", e);
            return JsonUtil.createJsonResponse(
                    NewEmployeeResult.build()
                            .withNewEmployeeCreated(false)
                            .withFirstName(request.getFirstName())
                            .withLastName(request.getLastName())
                            .withMiddleName(request.getMiddleName())
                            .withEmail(request.getEmail())
                            .withDepartment(request.getDepartment())
                            .withHireDate(request.getHireDate().toString())
                            .withPhone(request.getPhone())
                            .withAddress(request.getAddress())
                            .withCity(request.getCity())
                            .withState(request.getState())
                            .withZipCode(request.getZipCode())
                            .withPayRate(request.getPayRate())
                            .withPermissionAccess(request.getPermissionAccess().name())
                            .withUsername(request.getUsername())
                            .withPassword(request.getPassword())
                            .withError(e.getMessage())
                            .build()
            );
        } catch (Exception e) {
            log.error("An unexpected error occurred while creating employee. ", e);
            return JsonUtil.createJsonResponse(
                    NewEmployeeResult.build()
                            .withNewEmployeeCreated(false)
                            .withFirstName(request.getFirstName())
                            .withLastName(request.getLastName())
                            .withMiddleName(request.getMiddleName())
                            .withEmail(request.getEmail())
                            .withDepartment(request.getDepartment())
                            .withHireDate(request.getHireDate().toString())
                            .withPhone(request.getPhone())
                            .withAddress(request.getAddress())
                            .withCity(request.getCity())
                            .withState(request.getState())
                            .withZipCode(request.getZipCode())
                            .withPayRate(request.getPayRate())
                            .withPermissionAccess(request.getPermissionAccess().name())
                            .withUsername(request.getUsername())
                            .withPassword(request.getPassword())
                            .withError(e.getMessage())
                            .build()
            );
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
