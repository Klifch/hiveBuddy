function checkDeviceStatus(serialNumber) {
    fetch(`/deviceapi/status/${serialNumber}`)
        .then(response => response.json())
        .then(online => {
            const statusElement = document.getElementById(`status-${serialNumber}`);
            if (online) {
                statusElement.textContent = 'Online';
                statusElement.style.color = 'green';
            } else {
                statusElement.textContent = 'Offline';
                statusElement.style.color = 'red';
            }
        })
        .catch(error => console.error('Error checking device status:', error));
}

function pollDeviceStatuses() {
    const devices = document.querySelectorAll('.status-text');
    devices.forEach(device => {
        const serialNumber = device.id.replace('status-', '');
        checkDeviceStatus(serialNumber);
    });
}

// Initial poll when the page loads
document.addEventListener('DOMContentLoaded', function() {
    pollDeviceStatuses();
    // Poll every minute
    setInterval(pollDeviceStatuses, 60000);
});