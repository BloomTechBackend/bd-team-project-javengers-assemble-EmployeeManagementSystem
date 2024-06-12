document.getElementById('login-form').addEventListener('submit', async function(event) {
    const API_STAGE = "Gamma";
    event.preventDefault();

    // Clear any previous error messages
    document.getElementById('error-message').textContent = '';

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    //console.log('Attempting login with:', username, password);

    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        });

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        let result = await parseResponse(response);

        //console.log('Response received (parsed):', result);

        if (result.loginSuccess) {
            console.log('Login successful, redirecting to appropriate page');
            
            // Store employeeId and username in session storage
            sessionStorage.setItem('employeeId', result.employeeId);
            sessionStorage.setItem('username', result.username);

            if (result.forceChangeAfterLogin) {
                window.location.href = 'change_password.html';
            } else {
                window.location.href = 'home.html';
            }
        } else {
            //console.log('Login failed, result:', result);
            document.getElementById('error-message').textContent = result.error || 'Login failed. Please try again.';
        }
    } catch (error) {
        console.error('Login error:', error);
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

function clearForm() {
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
    document.getElementById('error-message').textContent = '';
}
