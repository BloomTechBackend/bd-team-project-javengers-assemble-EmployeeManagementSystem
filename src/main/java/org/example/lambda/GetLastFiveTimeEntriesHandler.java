package org.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dependency.DaggerAppComponent;
import org.example.dynamodb.TimeEntryDao;
import org.example.exceptions.TimeEntriesNotFoundException;
import org.example.model.TimeEntry;
import org.example.model.requests.GetLastFiveTimeEntriesRequest;
import org.example.model.results.GetLastFiveTimeEntriesResult;
import org.example.utils.gson.JsonUtil;

import javax.inject.Inject;
import java.util.List;

/**
 * Handler for retrieving the last five time entries for an employee from DynamoDB.
 * This class implements the AWS Lambda RequestHandler interface to handle requests
 * for retrieving the last five time entries.
 */
public class GetLastFiveTimeEntriesHandler implements RequestHandler<GetLastFiveTimeEntriesRequest, String> {
    private static final Logger log = LogManager.getLogger(GetLastFiveTimeEntriesHandler.class);
    @Inject
    TimeEntryDao timeEntryDao;

    /**
     * Default constructor that initializes the dependencies using Dagger.
     */
    public GetLastFiveTimeEntriesHandler() {
        DaggerAppComponent.create().inject(this);
    }

    /**
     * Handles the incoming request to retrieve the last five time entries for an employee.
     *
     * @param request The request object containing the employee ID for retrieving time entries.
     * @param context The Lambda execution context.
     * @return A JSON string representing the result of the retrieval operation.
     */
    @Override
    public String handleRequest(GetLastFiveTimeEntriesRequest request, Context context) {
        try {
            List<TimeEntry> last5Entries = timeEntryDao.getLastFiveTimeEntries(request.getEmployeeId());

            log.info(String.format("Successfully loaded the last 5 time entries. " +
                    "\nEmployee ID: %s ", request.getEmployeeId()));

            return JsonUtil.createJsonResponse(
                    GetLastFiveTimeEntriesResult.builder()
                            .withLastFiveTimeEntriesRetrieved(true)
                            .withTimeEntryList(last5Entries)
                            .build()
            );

        } catch (TimeEntriesNotFoundException e) {
            log.warn("Time Entries Not Found. ", e);

            return JsonUtil.createJsonResponse(
                    GetLastFiveTimeEntriesResult.builder()
                            .withLastFiveTimeEntriesRetrieved(false)
                            .withError(e.getMessage())
                            .build()
            );

        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving time entries. ", e);

            return JsonUtil.createJsonResponse(
                    GetLastFiveTimeEntriesResult.builder()
                            .withLastFiveTimeEntriesRetrieved(false)
                            .withError(e.getMessage())
                            .build()
            );
        }
    }
}
