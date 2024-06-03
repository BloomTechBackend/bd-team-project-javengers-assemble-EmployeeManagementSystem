package org.example.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dynamodb.model.EmployeeModel;
import org.example.exceptions.EmployeeNotFoundException;
import org.example.model.Employee;
import org.example.utils.ModelConverter;

import java.util.List;

public class EmployeeDao {
    private static final Logger log = LogManager.getLogger(EmployeeDao.class);
    private final DynamoDBMapper dynamoDBMapper;

    public EmployeeDao(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Retrieves an employee from the DynamoDB table using the specified employee ID.
     *
     * @param employeeId the ID of the employee to retrieve.
     * @return the Employee object corresponding to the specified employee ID.
     * @throws EmployeeNotFoundException if no employee is found with the specified employee ID.
     */
    public Employee getEmployee(String employeeId) {
        try {
            EmployeeModel employeeModel = this.dynamoDBMapper.load(EmployeeModel.class, employeeId);
            if (employeeModel == null) {
                throw new EmployeeNotFoundException("Could not find employee with Employee ID: " + employeeId);
            }

            Employee employee = ModelConverter.fromEmployeeModel(employeeModel);
            log.info("Successfully retrieved employee with ID: {}", employeeId);
            return employee;
        } catch (EmployeeNotFoundException e) {
            log.warn("Employee ID \"{}\" not found in database.", employeeId);
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred while retrieving Employee ID \"{}\". ", employeeId, e);
            throw e;
        }
    }

    /**
     * Saves an employee to the DynamoDB table.
     *
     * @param employee the Employee object to save.
     * @return the saved EmployeeModel object.
     */
    public EmployeeModel saveEmployee(Employee employee) {
        EmployeeModel employeeModel;
        try {
            employeeModel = ModelConverter.fromEmployee(employee);
            dynamoDBMapper.save(employeeModel);

            log.info("Successfully saved employee with ID: {}", employeeModel.getEmployeeId());
            return employeeModel;
        } catch (Exception e) {
            log.error("There was an error while saving Employee to the database: ", e);
            throw e;
        }
    }

    /**
     * Retrieves all employees from the DynamoDB table.
     *
     * @return a list of all Employee objects in the table.
     */
    public List<Employee> getAllEmployees() {
        try {
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<EmployeeModel> scanResult = dynamoDBMapper.scan(EmployeeModel.class, scanExpression);
            List<Employee> employeeList = ModelConverter.fromEmployeeModelList(scanResult);

            log.info("Successfully retrieved all employees from database.");
            return employeeList;
        } catch (Exception e) {
            log.error("An error occurred while retrieving all employees from database. ", e);
            throw e;
        }
    }
}
