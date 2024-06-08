package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeDao;
import org.example.dynamodb.model.EmployeeModel;
import org.example.model.Employee;
import org.example.model.requests.UpdateEmployeeRequest;
import org.example.model.results.UpdateEmployeeResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;

/**
 * Lambda function for handling requests to update employee information.
 * This class implements the RequestHandler interface for handling UpdateEmployeeRequest objects.
 */
public class UpdateEmployeeHandler implements RequestHandler<UpdateEmployeeRequest, String> {
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
    public String handleRequest(UpdateEmployeeRequest request, Context context) {
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

            EmployeeModel updatedEmployeeModel = employeeDao.saveEmployee(updatedEmployee);

            log.info(String.format("Employee Updated Successfully. Employee ID: %s.", updatedEmployee.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    UpdateEmployeeResult.builder()
                            .withEmployeeUpdated(true)
                            .withEmployeeId(updatedEmployeeModel.getEmployeeId())
                            .withFirstName(updatedEmployeeModel.getFirstName())
                            .withLastName(updatedEmployeeModel.getLastName())
                            .withMiddleName(updatedEmployeeModel.getMiddleName())
                            .withEmail(updatedEmployeeModel.getEmail())
                            .withDepartment(updatedEmployeeModel.getDepartment())
                            .withHireDate(updatedEmployeeModel.getHireDate())
                            .withCurrentlyEmployed(updatedEmployeeModel.isCurrentlyEmployed())
                            .withTerminatedDate(updatedEmployeeModel.getTerminatedDate())
                            .withPhone(updatedEmployeeModel.getPhone())
                            .withAddress(updatedEmployeeModel.getAddress())
                            .withCity(updatedEmployeeModel.getCity())
                            .withState(updatedEmployeeModel.getState())
                            .withZipCode(updatedEmployeeModel.getZipCode())
                            .withPayRate(updatedEmployeeModel.getPayRate())
                            .withPermissionAccess(updatedEmployeeModel.getPermissionAccess())
                            .build()
            );

        } catch (Exception e) {
            log.error("An error occurred while updating the Employee. ", e);

            return JsonUtil.createJsonResponse(
                    UpdateEmployeeResult.builder()
                            .withEmployeeUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withError(e.getMessage())
                            .build()
            );

        }
    }
}
