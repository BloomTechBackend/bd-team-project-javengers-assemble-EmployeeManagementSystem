document.addEventListener('DOMContentLoaded', async function () {
    const API_STAGE = "Gamma";
    const employeeId = sessionStorage.getItem('employeeId');
    const username = sessionStorage.getItem('username');
    let lastEntries = [];

    if (!employeeId || !username) {
        document.getElementById('employee-name').textContent = 'Session expired. Please log in again.';
        window.location.href = 'login.html';
        return;
    }

    // Set the employee name from session storage
    document.getElementById('employee-name').textContent = username;

    // Update the current time every second
    setInterval(() => {
        document.getElementById('current-time').textContent = new Date().toLocaleTimeString();
    }, 1000);

    // Fetch and update time entries
    await fetchAndUpdateTimeEntries();

    async function fetchAndUpdateTimeEntries() {
        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${employeeId}/last5`);
            if (!response.ok) {
                throw new Error('Failed to fetch time entries');
            }
            let result = await parseResponse(response);
            lastEntries = result.timeEntryList;
            console.log('Time entries received:', lastEntries); // Log the entries for debugging
            updateTimeEntries(lastEntries);
        } catch (error) {
            console.error('Error fetching time entries:', error);
        }
    }

    function updateTimeEntries(entries) {
        const timeEntriesTableBody = document.getElementById('time-entries');
        timeEntriesTableBody.innerHTML = entries.map(entry => `
            <tr>
                <td>${convertToLocaleDate(entry.timeIn)}</td>
                <td>${convertToLocaleTime(entry.timeIn)}</td>
                <td>${entry.timeOut ? convertToLocaleTime(entry.timeOut) : '---'}</td>
                <td>${entry.duration ? formatDuration(entry.duration) : '---'}</td>
            </tr>
        `).join('');

        const lastEntry = entries[0];
        if (lastEntry && !lastEntry.timeOut && new Date() - new Date(lastEntry.timeIn) < 17 * 60 * 60 * 1000) {
            document.getElementById('last-punch-in').textContent = convertToLocaleTime(lastEntry.timeIn);
            document.getElementById('last-punch-out').textContent = '---';
        } else {
            document.getElementById('last-punch-in').textContent = '---';
            document.getElementById('last-punch-out').textContent = '---';
        }
    }

    function formatDuration(duration) {
        return `${duration.toFixed(2)} hrs`;
    }

    function convertToLocaleDate(utcDateString) {
        return new Date(utcDateString).toLocaleDateString(undefined, { timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone });
    }

    function convertToLocaleTime(utcDateString) {
        return new Date(utcDateString).toLocaleTimeString(undefined, { timeZone: Intl.DateTimeFormat().resolvedOptions().timeZone });
    }

    window.punchIn = async function () {
        try {
            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${employeeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ employeeId }),
            });
            if (!response.ok) {
                throw new Error('Failed to punch in');
            }
            alert('Punch in successful!');
            await fetchAndUpdateTimeEntries();  // Reload and update the time entries table
        } catch (error) {
            console.error('Punch in error:', error);
            alert('Punch in failed. Please try again.');
        }
    };

    window.punchOut = async function () {
        try {
            const lastEntry = lastEntries[0];
            
            if (!lastEntry || lastEntry.timeOut) {
                alert('No active punch-in found.');
                return;
            }

            const requestBody = {
                employeeId: employeeId,
                entryId: lastEntry.entryId,
                timeIn: lastEntry.timeIn,  // Keep in UTC
                employeeClockOut: true
            };

            console.log('Sending punch out request with body:', requestBody); // Log request body

            const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${employeeId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBody),
            });

            const responseData = await parseResponse(response);
            console.log('Punch out response data:', responseData); // Log response data

            if (!response.ok) {
                console.error('Punch out failed:', responseData);
                throw new Error('Failed to punch out');
            }

            alert('Punch out successful!');
            await fetchAndUpdateTimeEntries();  // Reload and update the time entries table

            // Update the last punch in/out box
            const updatedLastEntry = lastEntries[0];
            document.getElementById('last-punch-in').textContent = convertToLocaleTime(updatedLastEntry.timeIn);
            document.getElementById('last-punch-out').textContent = updatedLastEntry.timeOut ? convertToLocaleTime(updatedLastEntry.timeOut) : '---';
        } catch (error) {
            console.error('Punch out error:', error);
            alert('Punch out failed. Please try again.');
        }
    };

    window.logout = function () {
        sessionStorage.clear();
        window.location.href = 'login.html';
    };

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
