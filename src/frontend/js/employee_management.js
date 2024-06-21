document.addEventListener('DOMContentLoaded', async function () {
    const API_STAGE = "Prod";
    const employeeId = sessionStorage.getItem('employeeId');
    const permissionLevel = sessionStorage.getItem('permissionLevel');
    let employeeDataList = [];
    let timeEntriesDataList = [];

    if (!employeeId || !permissionLevel) {
        alert('Session expired. Please log in again.');
        window.location.href = 'login.html';
        return;
    }

    if (permissionLevel === 'ADMIN') {
        const navBar = document.querySelector('.navbar');
        const empManagementLink = document.createElement('a');
        empManagementLink.href = 'employee_management.html';
        empManagementLink.textContent = 'Employee Management';
        navBar.insertBefore(empManagementLink, navBar.querySelector('button'));
    }

    const states = [
        "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
        "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
        "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
        "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
        "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
    ];

    const stateSelects = document.querySelectorAll('select[id$="state"]');
    stateSelects.forEach(select => {
        states.forEach(state => {
            const option = document.createElement('option');
            option.value = state;
            option.text = state;
            select.appendChild(option);
        });
    });

    // Fetch and populate employee table
    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees?employeeId=${employeeId}&permissionLevel=${permissionLevel}`);
        const result = await parseResponse(response);

        if (result.employeesRetrieved) {
            employeeDataList = result.employeeList;
            populateEmployeeTable(employeeDataList);
        } else {
            throw new Error('Failed to retrieve employees');
        }
    } catch (error) {
        console.error('Error fetching employees:', error);
    }

    function populateEmployeeTable(data) {
        const employeeTableBody = document.getElementById('employee-table-body');
        employeeTableBody.innerHTML = data.map(employee => `
            <tr>
                <td>${employee.employeeId}</td>
                <td>${employee.firstName}</td>
                <td>${employee.lastName}</td>
                <td>${employee.currentlyEmployed ? 'Yes' : 'No'}</td>
                <td>
                    <button onclick="viewEmployee('${employee.employeeId}')">View Employee</button>
                    <button onclick="viewTimeEntries('${employee.employeeId}')">View Time Entries</button>
                </td>
            </tr>
        `).join('');
    }

    window.viewEmployee = function (id) {
        const employee = employeeDataList.find(emp => emp.employeeId === id);
        if (employee) {
            populateEmployeeModal(employee);
            openModal();
        } else {
            console.error('Employee not found:', id);
        }
    }

    function populateEmployeeModal(employee) {
        document.getElementById('emp-employeeId').value = employee.employeeId || '';
        document.getElementById('emp-firstName').value = employee.firstName || '';
        document.getElementById('emp-lastName').value = employee.lastName || '';
        document.getElementById('emp-middleName').value = employee.middleName || '';
        document.getElementById('emp-email').value = employee.email || '';
        document.getElementById('emp-phone').value = employee.phone || '';
        document.getElementById('emp-department').value = employee.department || '';
        document.getElementById('emp-hireDate').value = employee.hireDate || '';
        document.getElementById('emp-terminatedDate').value = employee.terminatedDate || '';
        document.getElementById('emp-address').value = employee.address || '';
        document.getElementById('emp-city').value = employee.city || '';
        document.getElementById('emp-state').value = employee.state || '';
        document.getElementById('emp-zipCode').value = employee.zipCode || '';
        const payRateParts = employee.payRate ? employee.payRate.split('/') : ['', 'hr'];
        document.getElementById('emp-payRateAmount').value = payRateParts[0] || '';
        document.getElementById('emp-payRateFrequency').value = payRateParts[1] || 'hr';
        document.getElementById('emp-permissionAccess').value = employee.permissionAccess || 'STANDARD';
        document.getElementById('emp-currentlyEmployed').checked = employee.currentlyEmployed || false;
    }
    
    function openModal() {
        toggleEdit(false); // Ensure fields are locked initially
        document.getElementById('employee-modal').style.display = 'block';
    }
    
    window.closeModal = function () {
        document.getElementById('employee-modal').style.display = 'none';
        toggleEdit(false);
    }
    
    window.toggleEdit = function (isEditing) {
        const fields = [
            "emp-firstName", "emp-lastName", "emp-middleName", "emp-email", "emp-phone",
            "emp-department", "emp-hireDate", "emp-terminatedDate", "emp-address", "emp-city", "emp-state",
            "emp-zipCode", "emp-payRateAmount", "emp-payRateFrequency", "emp-permissionAccess", "emp-currentlyEmployed"
        ];
        fields.forEach(field => {
            const element = document.getElementById(field);
            if (element) {
                element.readOnly = !isEditing;
                element.disabled = !isEditing;
            }
        });
    
        document.getElementById('edit-button').style.display = isEditing ? 'none' : 'block';
        document.getElementById('cancel-button').style.display = isEditing ? 'block' : 'none';
        document.getElementById('save-button').style.display = isEditing ? 'block' : 'none';
    }    

    // Update Employee
    window.saveEmployee = async function () {
        const employeeData = {
            employeeId: document.getElementById('emp-employeeId').value,
            firstName: document.getElementById('emp-firstName').value,
            lastName: document.getElementById('emp-lastName').value,
            middleName: document.getElementById('emp-middleName').value || null,
            email: document.getElementById('emp-email').value || null,
            department: document.getElementById('emp-department').value || null,
            hireDate: document.getElementById('emp-hireDate').value || null,
            currentlyEmployed: document.getElementById('emp-currentlyEmployed').checked,
            terminatedDate: document.getElementById('emp-terminatedDate').value || null,
            phone: document.getElementById('emp-phone').value || null,
            address: document.getElementById('emp-address').value || null,
            city: document.getElementById('emp-city').value || null,
            state: document.getElementById('emp-state').value || null,
            zipCode: document.getElementById('emp-zipCode').value || null,
            payRate: `${document.getElementById('emp-payRateAmount').value}/${document.getElementById('emp-payRateFrequency').value}`,
            permissionAccess: document.getElementById('emp-permissionAccess').value.toUpperCase()
        };

        console.log('Employee Data to be updated:', employeeData); // Debugging statement

        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/${employeeData.employeeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(employeeData),
            });

            const responseData = await parseResponse(response);

            if (!response.ok) {
                throw new Error(responseData.error || 'Failed to save employee data');
            }

            alert('Employee updated successfully!');
            toggleEdit(false); // Exit edit mode but keep the modal open
        } catch (error) {
            console.error('Error saving employee data:', error);
            alert(`Failed to save employee data: ${error.message}`);
        }
    }

    window.viewTimeEntries = async function (employeeId) {
        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${employeeId}/all`);
            const result = await parseResponse(response);

            if (!response.ok) {
                throw new Error('Failed to fetch time entries');
            }

            timeEntriesDataList = result.timeEntryList; // Store entries in the variable
            populateTimeEntriesTable(timeEntriesDataList);
            openTimeEntriesModal();
        } catch (error) {
            console.error('Error fetching time entries:', error);
        }
    }
    
    function populateTimeEntriesTable(data) {
        const timeEntriesBody = document.getElementById('time-entries-body');
        timeEntriesBody.innerHTML = data.map(entry => `
            <tr data-id="${entry.entryId}">
                <td>${convertToLocaleDate(entry.timeIn)}</td>
                <td>${convertToLocaleTime(entry.timeIn)}</td>
                <td>${entry.timeOut ? convertToLocaleTime(entry.timeOut) : '---'}</td>
                <td>${entry.duration ? formatDuration(entry.duration) : '---'}</td>
                <td class="action-buttons">
                    <button onclick="editTimeEntry('${entry.entryId}')">Edit</button>
                    <button onclick="deleteTimeEntry('${entry.entryId}')">Delete</button>
                </td>
            </tr>
        `).join('');
    }

    function openTimeEntriesModal() {
        document.getElementById('time-entries-modal').style.display = 'block';
    }

    window.closeTimeEntriesModal = function () {
        document.getElementById('time-entries-modal').style.display = 'none';
    }

    window.editTimeEntry = function (entryId) {
        const row = document.querySelector(`tr[data-id="${entryId}"]`);
        console.log("Editing row with ID:", entryId); // Debugging
    
        if (!row) {
            console.error('Row not found for ID:', entryId);
            return;
        }
    
        // Fetch the entry from timeEntriesDataList
        const entry = timeEntriesDataList.find(entry => entry.entryId === entryId);
        if (!entry) {
            console.error('Entry not found:', entryId);
            return;
        }
    
        const cells = row.getElementsByTagName('td');
        const editMode = row.classList.contains('edit-mode');
    
        if (editMode) {
            // Collecting data from input fields
            const timeInDateTime = cells[1].getElementsByTagName('input')[0].value;
            const timeOutDateTime = cells[2].getElementsByTagName('input')[0].value;
            const duration = parseFloat(cells[3].getElementsByTagName('input')[0].value);
    
            const timeIn = new Date(timeInDateTime).toISOString().slice(0, 19);
            const timeOut = timeOutDateTime ? new Date(timeOutDateTime).toISOString().slice(0, 19) : null;
    
            const timeEntry = {
                entryId,
                employeeId: entry.employeeId, // Fetch employeeId from entry
                timeIn,
                timeOut,
                duration
            };
    
            console.log("Saving time entry:", timeEntry); // Debugging
    
            // Save updated time entry
            saveTimeEntry(timeEntry);
        } else {
            row.classList.add('edit-mode');
    
            // Parse existing date and time from entry
            const timeIn = new Date(entry.timeIn);
            const timeOut = entry.timeOut ? new Date(entry.timeOut) : null;
    
            console.log("Time In:", timeIn); // Debugging
            console.log("Time Out:", timeOut); // Debugging
    
            cells[1].innerHTML = `<input type="datetime-local" value="${timeIn.toISOString().slice(0, 19)}">`;
            cells[2].innerHTML = `<input type="datetime-local" value="${timeOut ? timeOut.toISOString().slice(0, 19) : ''}">`;
            cells[3].innerHTML = `<input type="number" step="0.01" value="${entry.duration}">`;
    
            cells[4].innerHTML = `
                <button onclick="editTimeEntry('${entryId}')">Save</button>
                <button onclick="cancelEditTimeEntry('${entryId}')">Cancel</button>
            `;
        }
    };

    async function saveTimeEntry(entry) {
        try {
            console.log("Sending request to save entry:", entry); // Debugging
    
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${entry.employeeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    entryId: entry.entryId,
                    timeIn: entry.timeIn,
                    timeOut: entry.timeOut,
                    employeeClockOut: false
                }),
            });
    
            if (!response.ok) {
                throw new Error('Failed to save time entry');
            }
    
            alert('Time entry updated successfully!');
            closeTimeEntriesModal();
            window.viewTimeEntries(entry.employeeId); // Refresh the time entries list
        } catch (error) {
            console.error('Error saving time entry:', error);
            alert('Failed to save time entry. Please try again.');
        }
    }

    window.cancelEditTimeEntry = function (entryId) {
        const row = document.querySelector(`tr[data-id="${entryId}"]`);
        if (row) {
            row.classList.remove('edit-mode');
            populateTimeEntriesTable(timeEntriesDataList);
        }
    };
    
    window.deleteTimeEntry = async function (id) {
        if (!confirm('Are you sure you want to delete this time entry?')) {
            return;
        }

        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok) {
                throw new Error('Failed to delete time entry');
            }
            alert('Time entry deleted successfully!');
            closeTimeEntriesModal();
            window.viewTimeEntries(sessionStorage.getItem('employeeId')); // Refresh the time entries list
        } catch (error) {
            console.error('Error deleting time entry:', error);
            alert('Failed to delete time entry. Please try again.');
        }
    }

    window.logout = function () {
        sessionStorage.clear();
        window.location.href = 'login.html';
    }

    async function parseResponse(response) {
        try {
            let result = await response.json();
            if (typeof result === 'string') {
                result = JSON.parse(result);
            }
            return result;
        } catch (error) {
            console.error('Error parsing JSON:', error);
            throw new Error('Error parsing server response. Please try again.');
        }
    }

    function formatDuration(duration) {
        return `${duration.toFixed(2)} hrs`;
    }

    function convertToLocaleDate(utcDateString) {
        return new Date(utcDateString + "Z").toLocaleDateString();
    }

    function convertToLocaleTime(utcDateString) {
        return new Date(utcDateString + "Z").toLocaleTimeString();
    }

    // Add New Employee functionality
    window.openAddEmployeeModal = function () {
        document.getElementById('add-employee-modal').style.display = 'block';
    }

    window.closeAddEmployeeModal = function () {
        document.getElementById('add-employee-modal').style.display = 'none';
    }

    document.getElementById('add-employee-form').addEventListener('submit', async function (event) {
        event.preventDefault();

        console.log('Submit event triggered'); // Debugging

        const newEmployeeData = {
            firstName: document.getElementById('new-firstName').value,
            lastName: document.getElementById('new-lastName').value,
            middleName: document.getElementById('new-middleName').value || null,
            email: document.getElementById('new-email').value || null,
            department: document.getElementById('new-department').value || null,
            hireDate: document.getElementById('new-hireDate').value || null,
            phone: document.getElementById('new-phone').value || null,
            address: document.getElementById('new-address').value || null,
            city: document.getElementById('new-city').value || null,
            state: document.getElementById('new-state').value || null,
            zipCode: document.getElementById('new-zipCode').value || null,
            payRate: `${document.getElementById('new-payRateAmount').value}/${document.getElementById('new-payRateFrequency').value}`,
            permissionAccess: document.getElementById('new-permissionAccess').value.toUpperCase(),
            username: document.getElementById('new-username').value,
            password: document.getElementById('new-password').value
        };

        console.log('New Employee Data:', newEmployeeData); // Debugging

        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newEmployeeData),
            });

            const responseData = await parseResponse(response); // Parsing response data using parseResponse function

            console.log('Response Data:', responseData); // Debugging

            if (response.ok && responseData.newEmployeeCreated) {
                alert('New employee created successfully!');
                closeAddEmployeeModal();
                document.getElementById('add-employee-form').reset();
                refreshEmployeeTable();
            } else {
                if (responseData.error && responseData.error.includes("is already taken. Please choose another.")) {
                    alert(`Failed to create new employee: ${responseData.error}`);
                } else {
                    alert('Failed to create new employee.');
                }

                // Repopulate form fields with the response data
                document.getElementById('new-firstName').value = responseData.firstName || '';
                document.getElementById('new-lastName').value = responseData.lastName || '';
                document.getElementById('new-middleName').value = responseData.middleName || '';
                document.getElementById('new-email').value = responseData.email || '';
                document.getElementById('new-department').value = responseData.department || '';
                document.getElementById('new-hireDate').value = responseData.hireDate || '';
                document.getElementById('new-phone').value = responseData.phone || '';
                document.getElementById('new-address').value = responseData.address || '';
                document.getElementById('new-city').value = responseData.city || '';
                document.getElementById('new-state').value = responseData.state || '';
                document.getElementById('new-zipCode').value = responseData.zipCode || '';
                const payRateParts = responseData.payRate ? responseData.payRate.split('/') : ['', 'hr'];
                document.getElementById('new-payRateAmount').value = payRateParts[0] || '';
                document.getElementById('new-payRateFrequency').value = payRateParts[1] || 'hr';
                document.getElementById('new-permissionAccess').value = responseData.permissionAccess || 'STANDARD';
                document.getElementById('new-username').value = responseData.username || '';
                document.getElementById('new-password').value = responseData.password || '';
            }
        } catch (error) {
            console.error('Error creating new employee:', error);
            alert(`Failed to create new employee: ${error.message}`);
        }
    });

    async function refreshEmployeeTable() {
        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees?employeeId=${employeeId}&permissionLevel=${permissionLevel}`);
            const result = await parseResponse(response);

            if (result.employeesRetrieved) {
                employeeDataList = result.employeeList;
                populateEmployeeTable(employeeDataList);
            } else {
                throw new Error('Failed to retrieve employees');
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
        }
    }
});
