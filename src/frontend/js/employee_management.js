document.addEventListener('DOMContentLoaded', async function () {
    const API_STAGE = "Gamma";
    const employeeId = sessionStorage.getItem('employeeId');
    const permissionLevel = sessionStorage.getItem('permissionLevel');
    let employeeDataList = [];

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

    const stateSelect = document.getElementById('state');
    states.forEach(state => {
        const option = document.createElement('option');
        option.value = state;
        option.text = state;
        stateSelect.add(option);
    });

    // Fetch and populate employee table
    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees?employeeId=${employeeId}&permissionLevel=${permissionLevel}`);
        if (!response.ok) {
            throw new Error('Failed to fetch employees');
        }
        let result = await parseResponse(response);
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
        document.getElementById('employeeId').value = employee.employeeId || '';
        document.getElementById('firstName').value = employee.firstName || '';
        document.getElementById('lastName').value = employee.lastName || '';
        document.getElementById('middleName').value = employee.middleName || '';
        document.getElementById('email').value = employee.email || '';
        document.getElementById('phone').value = employee.phone || '';
        document.getElementById('department').value = employee.department || '';
        document.getElementById('hireDate').value = employee.hireDate || '';
        document.getElementById('terminatedDate').value = employee.terminatedDate || '';
        document.getElementById('address').value = employee.address || '';
        document.getElementById('city').value = employee.city || '';
        document.getElementById('state').value = employee.state || '';
        document.getElementById('zipCode').value = employee.zipCode || '';
        const payRateParts = employee.payRate ? employee.payRate.split('/') : ['', 'hr'];
        document.getElementById('payRateAmount').value = payRateParts[0] || '';
        document.getElementById('payRateFrequency').value = payRateParts[1] || 'hr';
        document.getElementById('permissionAccess').value = employee.permissionAccess || 'STANDARD';
        document.getElementById('currentlyEmployed').checked = employee.currentlyEmployed || false;
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
            "firstName", "lastName", "middleName", "email", "phone",
            "department", "hireDate", "terminatedDate", "address", "city", "state",
            "zipCode", "payRateAmount", "payRateFrequency", "permissionAccess", "currentlyEmployed"
        ];
        fields.forEach(field => {
            document.getElementById(field).readOnly = !isEditing;
            document.getElementById(field).disabled = !isEditing;
        });

        document.getElementById('edit-button').style.display = isEditing ? 'none' : 'block';
        document.getElementById('cancel-button').style.display = isEditing ? 'block' : 'none';
        document.getElementById('save-button').style.display = isEditing ? 'block' : 'none';
    }

    window.saveEmployee = async function () {
        const employeeData = {
            employeeId: document.getElementById('employeeId').value,
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            middleName: document.getElementById('middleName').value || null,
            email: document.getElementById('email').value || null,
            department: document.getElementById('department').value || null,
            hireDate: document.getElementById('hireDate').value || null,
            currentlyEmployed: document.getElementById('currentlyEmployed').checked,
            terminatedDate: document.getElementById('terminatedDate').value || null,
            phone: document.getElementById('phone').value || null,
            address: document.getElementById('address').value || null,
            city: document.getElementById('city').value || null,
            state: document.getElementById('state').value || null,
            zipCode: document.getElementById('zipCode').value || null,
            payRate: `${document.getElementById('payRateAmount').value}/${document.getElementById('payRateFrequency').value}`,
            permissionAccess: document.getElementById('permissionAccess').value.toUpperCase()
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
            if (!response.ok) {
                throw new Error('Failed to save employee data');
            }
            alert('Employee updated successfully!');
            toggleEdit(false); // Exit edit mode but keep the modal open
        } catch (error) {
            console.error('Error saving employee data:', error);
            alert('Failed to save employee data. Please try again.');
        }
    }

    window.viewTimeEntries = function (employeeId) {
        window.location.href = `time_entries.html?employeeId=${employeeId}`;
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
});
