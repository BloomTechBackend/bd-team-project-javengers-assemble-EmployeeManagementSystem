document.addEventListener('DOMContentLoaded', async function () {
    const API_STAGE = "Prod";
    const employeeId = sessionStorage.getItem('employeeId');
    const username = sessionStorage.getItem('username');
    const permissionLevel = sessionStorage.getItem("permissionLevel");

    if (!employeeId || !username) {
        alert('Session expired. Please log in again.');
        window.location.href = 'login.html';
        return;
    }

    if (permissionLevel === "ADMIN") {
        const navBar = document.querySelector('.navbar');
        const employeeManagementButton = document.createElement('a');
        employeeManagementButton.href = 'employee_management.html';
        employeeManagementButton.textContent = 'Employee Management';
        navBar.insertBefore(employeeManagementButton, navBar.children[navBar.children.length - 1]);
    }

    // Populate state dropdown
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

    // Fetch and populate profile data
    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/${employeeId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch profile data');
        }
        let profileData = await parseResponse(response);
        populateProfile(profileData);
    } catch (error) {
        console.error('Error fetching profile data:', error);
    }

    function populateProfile(data) {
        document.getElementById('employeeId').value = data.employeeId || '';
        document.getElementById('firstName').value = data.firstName || '';
        document.getElementById('lastName').value = data.lastName || '';
        document.getElementById('middleName').value = data.middleName || '';
        document.getElementById('email').value = data.email || '';
        document.getElementById('phone').value = data.phone || '';
        document.getElementById('department').value = data.department || '';
        document.getElementById('hireDate').value = data.hireDate || '';
        document.getElementById('address').value = data.address || '';
        document.getElementById('city').value = data.city || '';
        document.getElementById('state').value = data.state || '';
        document.getElementById('zipCode').value = data.zipCode || '';
        document.getElementById('payRate').value = data.payRate || '';
        document.getElementById('permissionAccess').value = data.permissionAccess.charAt(0) + data.permissionAccess.slice(1).toLowerCase() || '';
    }

    window.toggleEdit = function (isEditing) {
        const mutableFields = ["phone", "address", "city", "state", "zipCode"];
        mutableFields.forEach(fieldId => {
            document.getElementById(fieldId).readOnly = !isEditing;
            document.getElementById(fieldId).disabled = !isEditing;
        });

        document.getElementById('edit-button').style.display = isEditing ? 'none' : 'block';
        document.getElementById('cancel-button').style.display = isEditing ? 'block' : 'none';
        document.getElementById('save-button').style.display = isEditing ? 'block' : 'none';
    }

    window.saveProfile = async function () {
        const profileData = {
            employeeId: document.getElementById('employeeId').value,
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            middleName: document.getElementById('middleName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            department: document.getElementById('department').value,
            hireDate: document.getElementById('hireDate').value,
            currentlyEmployed: null,
            terminatedDate: null,
            address: document.getElementById('address').value,
            city: document.getElementById('city').value,
            state: document.getElementById('state').value,
            zipCode: document.getElementById('zipCode').value,
            payRate: document.getElementById('payRate').value,
            permissionAccess: document.getElementById('permissionAccess').value.toUpperCase()
        };

        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/${employeeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(profileData),
            });
            if (!response.ok) {
                throw new Error('Failed to save profile data');
            }
            alert('Profile updated successfully!');
            toggleEdit(false);
        } catch (error) {
            console.error('Error saving profile data:', error);
            alert('Failed to save profile data. Please try again.');
        }
    }

    window.showPasswordModal = function () {
        document.getElementById('password-modal').style.display = 'block';
    }

    window.closePasswordModal = function () {
        document.getElementById('password-modal').style.display = 'none';
    }

    window.submitPasswordChange = async function () {
        const newPassword = document.getElementById('new-password').value;
        const verifyPassword = document.getElementById('verify-password').value;

        if (newPassword !== verifyPassword) {
            alert('Passwords do not match!');
            return;
        }

        const passwordRequirements = [
            { id: 'requirement-lowercase', regex: /(?=.*[a-z])/ },
            { id: 'requirement-uppercase', regex: /(?=.*[A-Z])/ },
            { id: 'requirement-digit', regex: /(?=.*[0-9])/ },
            { id: 'requirement-special', regex: /(?=.*[!{}()\-._?\[\]~;:@#$%^&*+=])/ },
            { id: 'requirement-length', regex: /.{8,}/ }
        ];

        const allRequirementsMet = passwordRequirements.every(req => req.regex.test(newPassword));

        if (!allRequirementsMet) {
            alert('Password does not meet all requirements!');
            return;
        }

        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/${employeeId}/credentials/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ password: newPassword, username: username }),
            });
            if (!response.ok) {
                throw new Error('Failed to change password');
            }
            alert('Password changed successfully!');
            closePasswordModal();
        } catch (error) {
            console.error('Error changing password:', error);
            alert('Failed to change password. Please try again.');
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

    document.getElementById('new-password').addEventListener('input', function(event) {
        const password = event.target.value;

        const requirements = [
            { id: 'requirement-lowercase', regex: /(?=.*[a-z])/ },
            { id: 'requirement-uppercase', regex: /(?=.*[A-Z])/ },
            { id: 'requirement-digit', regex: /(?=.*[0-9])/ },
            { id: 'requirement-special', regex: /(?=.*[!{}()\-._?\[\]~;:@#$%^&*+=])/ },
            { id: 'requirement-length', regex: /.{8,}/ }
        ];

        requirements.forEach(requirement => {
            const element = document.getElementById(requirement.id);
            const status = element.querySelector('.status');
            if (requirement.regex.test(password)) {
                status.textContent = '✔';
                element.classList.add('met');
            } else {
                status.textContent = '✘';
                element.classList.remove('met');
            }
        });

        // Check if the passwords match while typing
        const verifyPassword = document.getElementById('verify-password').value;
        const errorMessage = document.getElementById('error-message');
        if (password !== verifyPassword) {
            errorMessage.textContent = 'Passwords do not match!';
        } else {
            errorMessage.textContent = '';
        }
    });

    document.getElementById('verify-password').addEventListener('input', function(event) {
        const verifyPassword = event.target.value;
        const newPassword = document.getElementById('new-password').value;
        const errorMessage = document.getElementById('error-message');
        if (newPassword !== verifyPassword) {
            errorMessage.textContent = 'Passwords do not match!';
        } else {
            errorMessage.textContent = '';
        }
    });
});
