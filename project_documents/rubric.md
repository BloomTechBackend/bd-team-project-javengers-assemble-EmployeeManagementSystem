# Javengers Assemble Project Rubric

## Background

*This captures the expectations that we have for your team during the unit.
This is how we will evaluate whether you have demonstrated these expectations.*

## Instructions

*As a team, complete this rubric (everything except for the Appendix) by
answering the questions below. Each question should usually only require one or two sentences, so please be brief. The remainder of expectations will be evaluated by instructors, so you don’t need to write anything in the Appendix.
We want you to see the full set of expectations for transparency’s sake.*

## Deliverables

*Provide links to the following project deliverables:*

| Deliverable                                                      | Due Date                   | Date Completed | URL                                                                                             |
|------------------------------------------------------------------|----------------------------|----------------|-------------------------------------------------------------------------------------------------|
| Team name                                                        | Sprint 1 Module 1          |                | name: Javengers Assemble                                                                        |
| [Design Document - problem statement](design_document.md)        | Sprint 1 Module 2          | 05/31/2024     | [design document](design_document.md)                                                           |
| [Team Charter](team_charter.md)                                  | Sprint 1 Module 3          | N/A            | Completed Project alone. Charter not created.                                                   |
| [Design Document](design_document.md)                            | Sprint 1 REQUIRED TO GO ON | 05/31/2024     | [design document](design_document.md)                                                           |
| Project Completion (Feature Complete)                            | Sprint 3                   | 06/21/2024     | https://github.com/BloomTechBackend/bd-team-project-javengers-assemble-EmployeeManagementSystem |
| [Team Reflection](reflection.md)                                 | Sprint 3                   | N/A            |                                                                                                 |
| [Accomplishment Tracking (person 1)](accomplishment_tracking.md) | Sprint 3                   | N/A            |                                                                                                 |
| Self Reflection                                                  | Sprint 3                   |                | n/a (will be submitted via Canvas - "Wrap-up" section)                                          |

## Technical Learning Objectives

### API Design

**Design an API to meet the needs of your application.** Provide a link to the definition for your endpoints (can be code/configuration, or can be documentation). List one thing that your team learned about designing a good API.

*Endpoint definition location:* [API Documentation](../src/main/resources/API_Documentation/employee-management-api-docs.html)  
*What we learned:* Ensuring clear, consistent, and secure endpoint definitions is crucial for a robust API.

**Develop a service endpoint definition that uses complex inputs and outputs.**
Select one of your endpoints and list the operation’s input and output objects.
Under each, list its attributes.

*Endpoint:* `/employees/{employeeId}/credentials/update`  
*Input object(s):*
- `username`
- `password`

*Output object(s):*
- `credentialsUpdated`
- `employeeId`
- `username`
- `lastUpdated`
- `accountLocked`
- `forceChangeAfterLogin`
- `failedAttempts`

**Given a user story that requires a user to provide values to a service
endpoint, design a service endpoint including inputs, outputs, and errors.**
Select one of your endpoints that accepts input values from a client. List the
error cases you identified and how the service responds in each case. Provide at
most 3 error cases.

| **Endpoint:**           | `/employees`                                                            |
|-------------------------|-------------------------------------------------------------------------|
| **Error case**          | **Service response**                                                    |
| Employee already exists | `400 Bad Request` with a message indicating the employee exists         |
| Unauthorized Access     | `403 Forbidden` with a message indicating the insufficient access.      |
| Server error            | `500 Internal Server Error` with a message indicating an error occurred |

**Develop a service endpoint definition that uses query parameters to determine
how results are sorted or filtered.** List at least one endpoint that allows
sorting or filtering of results. Which attribute(s) can be sorted/filtered on?

*Endpoint:* `/employee_time_entry/all`  
*Attribute(s):* `employee_id`, `time_in`, `time_out`

**Determine whether a given error condition should result in a client or server
exception.** List one client exception and one server exception that your
service code throws. List one condition in which this exception is thrown.

|                        | **Exception**                 | **One case in which it is thrown**                              |
|------------------------|-------------------------------|-----------------------------------------------------------------|
| **Client exception:**  | `InvalidInputFormatException` | When the password format is invalid                             |
| **Service exception:** | `AccountLockedException`      | When an account is locked due to too many failed login attempts |

### DynamoDB Table Design

**Decompose a given set of use cases into a set of DynamoDB tables that provides
efficient data access.** List the DynamoDB tables in your project:

1. employee
2. employee_time_entries
3. employee_credentials

**Design a DynamoDB table key schema that allows items to be uniquely
identified.** Describe the primary key structure for your DynamoDB table with the most interesting primary key. In a sentence or two, explain your choice of partition/sort key(s).

1. **Employee Table**: The primary key is the `employee_id` (Partition Key). This allows for efficient querying of employees by their unique ID.

**Design the attributes of a DynamoDB table given a set of use cases.** List a DynamoDB table with at least 3 attributes. List one relevant use case that uses the attribute. In one sentence, describe why the attribute is included.

**Table name:** Employee

**Attributes:**

| Attribute name | (One) relevant use case              | attribute purpose                                               |
|----------------|--------------------------------------|-----------------------------------------------------------------|
| employee_id    | Identifying a unique employee record | Primary key to uniquely identify an employee                    |
| hire_date      | Tracking employee hire dates         | To determine employee tenure and eligibility for benefits       |
| department     | Grouping employees by department     | For filtering and managing employees within the same department |

### DynamoDB Indexes

**Design a GSI key schema and attribute projection that optimizes queries not supported by a provided DynamoDB table.** In one or two sentences, explain why you created one of the GSIs that your project uses, including one use case that uses that index.

N/A - No GSI was created for this project.

**Implement functionality that uses `query()` to retrieve items from a provided
DynamoDB's GSI.** List one of your use cases that uses `query()` on a GSI.

N/A - No GSI was used for this project.

## Soft(er) Outcomes

**Learn a new technology.** For each team member, list something new that that team member learned:

| Team Member | Something new the team member learned            |   
|-------------|--------------------------------------------------|
| Person 1    | AWS Lambda for backend development               |   
| Person 1    | DynamoDB table design and indexing               |     
| Person 1    | Using Rapidoc for API documentation              |     
| Person 1    | Implementing secure password hashing with Argon2 |     

**Identify keywords to research to accomplish a technical goal | Use sources
like sage and stack overflow to solve issues encountered while programming.**
Give an example of a search term that your team might have used to find an answer to a technical question/obstacle that your team ran into. List the resource that you found that was helpful.

**Search terms:** `AWS Lambda deployment with API Gateway`  
**Helpful resource:** AWS official documentation and tutorials on deploying Lambda functions.

**Find material online to learn new technical topics.** List one resource that
your team found on your own that helped you on your project.

**Topic:** Secure Password Storage  
**Resource:** OWASP guidelines and BouncyCastle library documentation

**Share what was worked on yesterday, the plan for today, and any blockers as a
part of a scrum standup.** In one or two sentences, describe what your team
found to be the most useful outcome from holding daily standups.

1. Daily stand-ups helped the team stay synchronized, identify blockers early, and adjust priorities to ensure smooth progress.
