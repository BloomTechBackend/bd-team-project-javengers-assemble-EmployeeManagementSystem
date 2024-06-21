const API_STAGE = "Prod";

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

document.getElementById('change-password-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    // Clear any previous error messages
    document.getElementById('error-message').textContent = '';

    const newPassword = document.getElementById('new-password').value;
    const verifyPassword = document.getElementById('verify-password').value;
    const employeeId = sessionStorage.getItem('employeeId');  // Retrieve the employeeId from session storage
    const username = sessionStorage.getItem('username');  // Retrieve the username from session storage

    if (!employeeId || !username) {
        document.getElementById('error-message').textContent = 'Session expired. Please log in again.';
        window.location.href = 'login.html';
        return;
    }

    if (newPassword !== verifyPassword) {
        document.getElementById('error-message').textContent = 'Passwords do not match!';
        return;
    }

    //console.log('Attempting to update password for employeeId:', employeeId);

    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/${employeeId}/credentials/update`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ password: newPassword, username: username }),
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        let result = await parseResponse(response);

        //console.log('Response received (parsed):', result);

        if (result.credentialsUpdated) {
            //console.log('Password update successful, redirecting to home page');
            window.location.href = 'home.html';
        } else {
            //console.log('Password update failed, result:', result);
            document.getElementById('error-message').textContent = result.error || 'Password change failed. Please try again.';
        }
    } catch (error) {
        console.error('Password change error:', error);
        document.getElementById('error-message').textContent = 'An error occurred. Please try again.';
    }
});

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
