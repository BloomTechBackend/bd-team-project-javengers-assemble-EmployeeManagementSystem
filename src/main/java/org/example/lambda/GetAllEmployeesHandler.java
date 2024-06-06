package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.EmployeeDao;
import org.example.exceptions.UnauthorizedAccessException;
import org.example.model.Employee;
import org.example.model.PermissionLevel;
import org.example.model.requests.GetAllEmployeesRequest;
import org.example.model.results.GetAllEmployeesResult;
import org.example.utils.ModelConverter;

import javax.inject.Inject;
import java.util.List;

public class GetAllEmployeesHandler implements RequestHandler<GetAllEmployeesRequest, GetAllEmployeesResult> {
    private static final Logger log = LogManager.getLogger(GetAllEmployeesHandler.class);

    @Inject
    EmployeeDao employeeDao;

    public GetAllEmployeesHandler() {
        DaggerAppComponent.create().inject(this);
    }

    @Override
    public GetAllEmployeesResult handleRequest(GetAllEmployeesRequest request, Context context) {
        try {

            if (request.getPermissionLevel() != PermissionLevel.ADMIN) {
                throw new UnauthorizedAccessException("User does not have permission to access this resource. " +
                        "Resource: " + GetAllEmployeesHandler.class.getSimpleName());
            }

            List<Employee> employees = employeeDao.getAllEmployees();

            log.info("All employee data successfully loaded. ");

            return GetAllEmployeesResult.builder()
                    .withEmployeesRetrieved(true)
                    .withEmployeesList(ModelConverter.fromEmployeeList(employees))
                    .build();

        } catch (UnauthorizedAccessException e) {
          log.error("User does not have sufficient authorization to access this resource. ", e);
            return GetAllEmployeesResult.builder()
                    .withEmployeesRetrieved(false)
                    .withError(e.getMessage())
                    .build();
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving all employees: ", e);
            return GetAllEmployeesResult.builder()
                    .withEmployeesRetrieved(false)
                    .withError(e.getMessage())
                    .build();

        }
    }
}
