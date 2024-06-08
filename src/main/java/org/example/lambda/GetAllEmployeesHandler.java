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
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * Handler for retrieving all employee data from DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle requests
 * for retrieving all employee data.
 */
public class GetAllEmployeesHandler implements RequestHandler<GetAllEmployeesRequest, String> {
    private static final Logger log = LogManager.getLogger(GetAllEmployeesHandler.class);

    @Inject
    EmployeeDao employeeDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public GetAllEmployeesHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to retrieve all employees.
     *
     * @param request The request object containing the details for retrieving all employees.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the retrieval operation.
     */
    @Override
    public String handleRequest(GetAllEmployeesRequest request, Context context) {
        try {

            if (request.getPermissionLevel() != PermissionLevel.ADMIN) {
                throw new UnauthorizedAccessException("User does not have permission to access this resource. " +
                        "Resource: " + GetAllEmployeesHandler.class.getSimpleName());
            }

            List<Employee> employees = employeeDao.getAllEmployees();

            log.info("All employee data successfully loaded. ");

            return JsonUtil.createJsonResponse(
                    GetAllEmployeesResult.builder()
                            .withEmployeesRetrieved(true)
                            .withEmployeesList(employees)
                            .build()
            );

        } catch (UnauthorizedAccessException e) {
          log.error("User does not have sufficient authorization to access this resource. ", e);
            return JsonUtil.createJsonResponse(
                    GetAllEmployeesResult.builder()
                            .withEmployeesRetrieved(false)
                            .withError(e.getMessage())
                            .build()
            );

        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving all employees: ", e);
            return JsonUtil.createJsonResponse(
                    GetAllEmployeesResult.builder()
                            .withEmployeesRetrieved(false)
                            .withError(e.getMessage())
                            .build()
            );

        }
    }
}
