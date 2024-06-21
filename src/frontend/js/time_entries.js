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

    // Fetch and populate time entries
    try {
        const response = await fetch(`https://qjnhlsg7ge.execute-api.us-west-2.amazonaws.com/${API_STAGE}/employees/time_entries/${employeeId}/all`);
        if (!response.ok) {
            throw new Error('Failed to fetch time entries');
        }
        let result = await parseResponse(response);
        let timeEntries = result.timeEntryList; // Access the nested timeEntryList
        console.log('Time entries received:', timeEntries); // Debugging statement
        populateTimeEntries(timeEntries);
    } catch (error) {
        console.error('Error fetching time entries:', error);
    }

    function populateTimeEntries(entries) {
        const timeEntriesBody = document.getElementById('time-entries-body');
        timeEntriesBody.innerHTML = entries.map(entry => `
            <tr>
                <td>${convertToLocaleDate(entry.timeIn)}</td>
                <td>${convertToLocaleTime(entry.timeIn)}</td>
                <td>${entry.timeOut ? convertToLocaleTime(entry.timeOut) : '---'}</td>
                <td>${entry.duration ? formatDuration(entry.duration) : '---'}</td>
                <td class="hidden">${entry.timeIn}</td>
                <td class="hidden">${entry.timeOut}</td>
            </tr>
        `).join('');
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

    window.sortTable = function(n) {
        const table = document.getElementById("time-entries-table");
        let rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        switching = true;
        dir = "asc"; 
        while (switching) {
            switching = false;
            rows = table.rows;
            for (i = 1; i < (rows.length - 1); i++) {
                shouldSwitch = false;
                x = rows[i].getElementsByTagName("TD")[n];
                y = rows[i + 1].getElementsByTagName("TD")[n];

                let xContent = x.textContent || x.innerText;
                let yContent = y.textContent || y.innerText;

                if (dir === "asc") {
                    if (n === 0 || n === 1 || n === 2) { // Date, Punch In or Punch Out columns
                        xContent = rows[i].getElementsByTagName("TD")[n + 4].textContent; // Use hidden column for sorting
                        yContent = rows[i + 1].getElementsByTagName("TD")[n + 4].textContent; // Use hidden column for sorting
                    }
                    if (xContent > yContent) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (dir === "desc") {
                    if (n === 0 || n === 1 || n === 2) { // Date, Punch In or Punch Out columns
                        xContent = rows[i].getElementsByTagName("TD")[n + 4].textContent; // Use hidden column for sorting
                        yContent = rows[i + 1].getElementsByTagName("TD")[n + 4].textContent; // Use hidden column for sorting
                    }
                    if (xContent < yContent) {
                        shouldSwitch = true;
                        break;
                    }
                }
            }
            if (shouldSwitch) {
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
                switchcount++;      
            } else {
                if (switchcount === 0 && dir === "asc") {
                    dir = "desc";
                    switching = true;
                }
            }
        }
    }
});
