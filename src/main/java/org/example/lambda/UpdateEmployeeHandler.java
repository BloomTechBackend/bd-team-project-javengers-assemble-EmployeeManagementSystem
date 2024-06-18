package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeDao;
import org.example.dynamodb.model.EmployeeModel;
import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.example.model.requests.UpdateEmployeeRequest;
import org.example.model.results.UpdateEmployeeResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;
import java.time.LocalDate;

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
            log.info("UpdateEmployeeRequest Received. Data: {}", request);
            // check if employee ID exists in the database
            Employee employee = employeeDao.getEmployee(request.getEmployeeId());
            log.info("Original Employee values: {}", employee);

            String firstName = request.getFirstName();
            String lastName = request.getLastName();
            String middleName = request.getMiddleName();
            String email = request.getEmail();
            String department = request.getDepartment();
            LocalDate hireDate = request.getHireDate();
            boolean currentlyEmployed = request.isCurrentlyEmployed();
            LocalDate terminatedDate = request.getTerminatedDate();
            String phone = request.getPhone();
            String address = request.getAddress();
            String city = request.getCity();
            String state = request.getState();
            String zipCode = request.getZipCode();
            String payRate = request.getPayRate();
            PermissionLevel permissionAccess = request.getPermissionAccess();

            if (firstName != null && !firstName.equals(employee.getFirstName())) {
                employee.setFirstName(firstName);
            }
            if (lastName != null && !lastName.equals(employee.getLastName())) {
                employee.setLastName(lastName);
            }
            if (middleName != null && !middleName.equals(employee.getMiddleName())) {
                employee.setMiddleName(middleName);
            }
            if (email != null && !email.equals(employee.getEmail())) {
                employee.setEmail(email);
            }
            if (department != null && !department.equals(employee.getDepartment())) {
                employee.setDepartment(department);
            }
            if (hireDate != null && !hireDate.equals(employee.getHireDate())) {
                employee.setHireDate(hireDate);
            }
            if (currentlyEmployed != employee.isCurrentlyEmployed()) {
                employee.setCurrentlyEmployed(currentlyEmployed);
            }
            if (terminatedDate != null && !terminatedDate.equals(employee.getTerminatedDate())) {
                employee.setTerminatedDate(terminatedDate);
            }
            if (phone != null && !phone.equals(employee.getPhone())) {
                employee.setPhone(phone);
            }
            if (address != null && !address.equals(employee.getAddress())) {
                employee.setAddress(address);
            }
            if (city != null && !city.equals(employee.getCity())) {
                employee.setCity(city);
            }
            if (state != null && !state.equals(employee.getState())) {
                employee.setState(state);
            }
            if (zipCode != null && !zipCode.equals(employee.getZipCode())) {
                employee.setZipCode(zipCode);
            }
            if (payRate != null && !payRate.equals(employee.getPayRate())) {
                employee.setPayRate(payRate);
            }
            if (permissionAccess != null && permissionAccess != employee.getPermissionAccess()) {
                employee.setPermissionAccess(permissionAccess);
            }


            EmployeeModel updatedEmployeeModel = employeeDao.saveEmployee(employee);

            log.info("Employee Updated Successfully. Employee {}", updatedEmployeeModel);

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

        } catch (EmployeeNotFoundException e) {
            log.error(String.format("Employee Not Found. Entry for Employee ID \"%s\" should be in the database.",
                    request.getEmployeeId()), e);

            return JsonUtil.createJsonResponse(
                    UpdateEmployeeResult.builder()
                            .withEmployeeUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withFirstName(request.getFirstName())
                            .withLastName(request.getLastName())
                            .withMiddleName(request.getMiddleName())
                            .withEmail(request.getEmail())
                            .withDepartment(request.getDepartment())
                            .withHireDate(request.getHireDate() != null ? request.getHireDate().toString() : null)
                            .withCurrentlyEmployed(request.isCurrentlyEmployed())
                            .withTerminatedDate(request.getTerminatedDate() != null ? request.getTerminatedDate().toString() : null)
                            .withPhone(request.getPhone())
                            .withAddress(request.getAddress())
                            .withCity(request.getCity())
                            .withState(request.getState())
                            .withZipCode(request.getZipCode())
                            .withPayRate(request.getPayRate())
                            .withPermissionAccess(request.getPermissionAccess() != null ? request.getPermissionAccess().name() : null)
                            .withError(e.getMessage())
                            .build()
            );

        } catch (Exception e) {
            log.error("An error occurred while updating the Employee. ", e);

            return JsonUtil.createJsonResponse(
                    UpdateEmployeeResult.builder()
                            .withEmployeeUpdated(false)
                            .withEmployeeId(request.getEmployeeId())
                            .withFirstName(request.getFirstName())
                            .withLastName(request.getLastName())
                            .withMiddleName(request.getMiddleName())
                            .withEmail(request.getEmail())
                            .withDepartment(request.getDepartment())
                            .withHireDate(request.getHireDate() != null ? request.getHireDate().toString() : null)
                            .withCurrentlyEmployed(request.isCurrentlyEmployed())
                            .withTerminatedDate(request.getTerminatedDate() != null ? request.getTerminatedDate().toString() : null)
                            .withPhone(request.getPhone())
                            .withAddress(request.getAddress())
                            .withCity(request.getCity())
                            .withState(request.getState())
                            .withZipCode(request.getZipCode())
                            .withPayRate(request.getPayRate())
                            .withPermissionAccess(request.getPermissionAccess() != null ? request.getPermissionAccess().name() : null)
                            .withError(e.getMessage())
                            .build()
            );

        }
    }
}
