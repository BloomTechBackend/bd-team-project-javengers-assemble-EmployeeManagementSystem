package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeDao;
import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.model.requests.GetEmployeeRequest;
import org.example.model.results.GetEmployeeResult;

import javax.inject.Inject;

/**
 * Lambda function for handling requests to retrieve employee information.
 * This class implements the RequestHandler interface for handling GetEmployeeRequest objects.
 */
public class GetEmployeeHandler implements RequestHandler<GetEmployeeRequest, GetEmployeeResult> {
    private static final Logger log = LogManager.getLogger(GetEmployeeRequest.class);

    @Inject
    EmployeeDao employeeDao;

    /**
     * Constructor for GetEmployeeHandler.
     * Initializes dependencies using DaggerAppComponent.
     */
    public GetEmployeeHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the get employee request.
     *
     * @param request The request containing the ID of the employee to retrieve.
     * @param context The Lambda context.
     * @return A GetEmployeeResult containing the retrieved employee information if successful,
     *         or an error message if unsuccessful.
     */
    @Override
    public GetEmployeeResult handleRequest(final GetEmployeeRequest request, Context context) {
        try {

            Employee employee = employeeDao.getEmployee(request.getEmployeeId());

            return GetEmployeeResult.builder()
                    .withEmployeeRetrieved(true)
                    .withEmployeeId(employee.getEmployeeId())
                    .withFirstName(employee.getFirstName())
                    .withLastName(employee.getLastName())
                    .withMiddleName(employee.getMiddleName())
                    .withEmail(employee.getEmail())
                    .withDepartment(employee.getDepartment())
                    .withHireDate(employee.getHireDate())
                    .withCurrentlyEmployed(employee.isCurrentlyEmployed())
                    .withTerminatedDate(employee.getTerminatedDate())
                    .withPhone(employee.getPhone())
                    .withAddress(employee.getAddress())
                    .withCity(employee.getCity())
                    .withState(employee.getState())
                    .withZipCode(employee.getZipCode())
                    .withPayRate(employee.getPayRate())
                    .withPermissionAccess(employee.getPermissionAccess())
                    .build();

        } catch (EmployeeNotFoundException e) {
            log.error(String.format("Employee not found. Employee ID \"%s\" not in employee database, but the " +
                    "credentials for this ID exist in the employee_credentials table.", request.getEmployeeId()), e);
            return GetEmployeeResult.builder()
                    .withEmployeeRetrieved(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withError(e.getMessage())
                    .build();

        } catch (Exception e) {
            log.error("An unexpected error occured while retrieving employee ID: " + request.getEmployeeId(), e);
            return GetEmployeeResult.builder()
                    .withEmployeeRetrieved(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withError(e.getMessage())
                    .build();

        }
    }
}
