package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dynamodb.model.EmployeeCredentialsModel;
import org.example.exceptions.UsernameNotFoundException;
import org.example.model.EmployeeCredentials;
import org.example.utils.ModelConverter;

public class EmployeeCredentialsDao {
    private static final Logger log = LogManager.getLogger(EmployeeCredentialsDao.class);
    private final DynamoDBMapper dynamoDBMapper;

    public EmployeeCredentialsDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }


    /**
     * Retrieves employee credentials from the DynamoDB table using the specified username.
     *
     * @param username the username of the employee whose credentials are to be retrieved.
     * @return the {@link EmployeeCredentials} object corresponding to the specified username.
     * @throws UsernameNotFoundException if no employee credentials are found with the specified username.
     * @throws Exception if any other error occurs while retrieving the employee credentials.
     */
    public EmployeeCredentials getEmployeeCredentials(String username) {
        try {
            EmployeeCredentialsModel employeeCredentialsModel = this.dynamoDBMapper.load(EmployeeCredentialsModel.class, username);
            if (employeeCredentialsModel == null) {
                throw new UsernameNotFoundException(String.format("Username \"%s\" not found!", username));
            }

            EmployeeCredentials employeeCredentials = ModelConverter.fromEmployeeCredentialsModel(employeeCredentialsModel);

            log.info("Successfully retrieved employee credentials.");
            return employeeCredentials;
        } catch (UsernameNotFoundException e) {
            log.warn(String.format("Username \"%s\" not found!", username));
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred. ", e);
            throw e;
        }
    }

    /**
     * Saves the employee credentials to the DynamoDB table.
     *
     * @param employeeCredentials the {@link EmployeeCredentials} object to be saved.
     * @return the saved {@link EmployeeCredentialsModel} object.
     * @throws Exception if any error occurs while saving the employee credentials.
     */
    public EmployeeCredentialsModel saveEmployeeCredentials(EmployeeCredentials employeeCredentials) {
        EmployeeCredentialsModel employeeCredentialsModel;
        try {
            employeeCredentialsModel = ModelConverter.fromEmployeeCredentials(employeeCredentials);
            dynamoDBMapper.save(employeeCredentialsModel);

            log.info("Successfully saved the employee credentials for username \"{}\". ", employeeCredentialsModel.getUsername());
            return employeeCredentialsModel;
        } catch (Exception e) {
            log.error("There was an error while saving the employee credentials to the database: ", e);
            throw e;
        }
    }
}
