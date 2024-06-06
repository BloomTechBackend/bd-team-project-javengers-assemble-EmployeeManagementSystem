package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeDao;
import org.example.model.Employee;
import org.example.model.requests.UpdateEmployeeRequest;
import org.example.model.results.UpdateEmployeeResult;

import javax.inject.Inject;

/**
 * Lambda function for handling requests to update employee information.
 * This class implements the RequestHandler interface for handling UpdateEmployeeRequest objects.
 */
public class UpdateEmployeeHandler implements RequestHandler<UpdateEmployeeRequest, UpdateEmployeeResult> {
    private static final Logger log = LogManager.getLogger(UpdateEmployeeHandler.class);
    @Inject
    EmployeeDao employeeDao;

    /**
     * Constructor for UpdateEmployeeHandler.
     * Initializes dependencies using DaggerAppComponent.
     */
    public UpdateEmployeeHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the update employee request.
     *
     * @param request The request containing updated employee information.
     * @param context The Lambda context.
     * @return An UpdateEmployeeResult indicating the outcome of the update operation.
     */
    @Override
    public UpdateEmployeeResult handleRequest(UpdateEmployeeRequest request, Context context) {
        try {
            Employee updatedEmployee = Employee.builder()
                    .withEmployeeId(request.getEmployeeId())
                    .withFirstName(request.getFirstName())
                    .withLastName(request.getLastName())
                    .withMiddleName(request.getMiddleName())
                    .withEmail(request.getEmail())
                    .withDepartment(request.getDepartment())
                    .withHireDate(request.getHireDate())
                    .withCurrentlyEmployed(request.isCurrentlyEmployed())
                    .withTerminatedDate(request.getTerminatedDate())
                    .withPhone(request.getPhone())
                    .withAddress(request.getAddress())
                    .withCity(request.getCity())
                    .withState(request.getState())
                    .withZipCode(request.getZipCode())
                    .withPayRate(request.getPayRate())
                    .withPermissionAccess(request.getPermissionAccess())
                    .build();

            employeeDao.saveEmployee(updatedEmployee);

            log.info(String.format("Employee Updated Successfully. Employee ID: %s.", updatedEmployee.getEmployeeId()));

            return UpdateEmployeeResult.builder()
                    .withEmployeeUpdated(true)
                    .withEmployeeId(updatedEmployee.getEmployeeId())
                    .withFirstName(updatedEmployee.getFirstName())
                    .withLastName(updatedEmployee.getLastName())
                    .withMiddleName(updatedEmployee.getMiddleName())
                    .withEmail(updatedEmployee.getEmail())
                    .withDepartment(updatedEmployee.getDepartment())
                    .withHireDate(updatedEmployee.getHireDate())
                    .withCurrentlyEmployed(updatedEmployee.isCurrentlyEmployed())
                    .withTerminatedDate(updatedEmployee.getTerminatedDate())
                    .withPhone(updatedEmployee.getPhone())
                    .withAddress(updatedEmployee.getAddress())
                    .withCity(updatedEmployee.getCity())
                    .withState(updatedEmployee.getState())
                    .withZipCode(updatedEmployee.getZipCode())
                    .withPayRate(updatedEmployee.getPayRate())
                    .withPermissionAccess(updatedEmployee.getPermissionAccess())
                    .build();

        } catch (Exception e) {
            log.error("An error occurred while updating the Employee. ", e);

            return UpdateEmployeeResult.builder()
                    .withEmployeeUpdated(false)
                    .withEmployeeId(request.getEmployeeId())
                    .withError(e.getMessage())
                    .build();

        }
    }
}
