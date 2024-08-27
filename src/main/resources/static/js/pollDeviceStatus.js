// function checkDeviceStatus(serialNumber) {
//     fetch(`/deviceapi/status/${serialNumber}`)
//         .then(response => response.json())
//         .then(online => {
//             const statusElement = document.getElementById(`status-${serialNumber}`);
//             if (online) {
//                 statusElement.textContent = 'Online';
//                 statusElement.style.color = 'green';
//             } else {
//                 statusElement.textContent = 'Offline';
//                 statusElement.style.color = 'red';
//             }
//         })
//         .catch(error => console.error('Error checking device status:', error));
// }
//
// function pollDeviceStatuses() {
//     const devices = document.querySelectorAll('.status-text');
//     devices.forEach(device => {
//         const serialNumber = device.id.replace('status-', '');
//         checkDeviceStatus(serialNumber);
//     });
// }
//
// // Initial poll when the page loads
// document.addEventListener('DOMContentLoaded', function() {
//     pollDeviceStatuses();
//     // Poll every minute
//     setInterval(pollDeviceStatuses, 60000);
// });

// Object to store all notifications for each device
const deviceNotifications = {};

// Function to check device status
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

// Function to check notifications for a device
function checkDeviceNotifications(serialNumber) {
    fetch(`/deviceapi/getNotifications/${serialNumber}`)
        .then(response => response.json())
        .then(notifications => {
            const bellElement = document.querySelector(`#bell-${serialNumber}`);
            deviceNotifications[serialNumber] = notifications;

            // If there are notifications, show a red dot
            if (notifications.length > 0) {
                bellElement.classList.add('has-notifications');
            } else {
                bellElement.classList.remove('has-notifications');
            }
        })
        .catch(error => console.error('Error checking device notifications:', error));
}

// Function to poll device statuses and notifications
function pollDeviceStatusesAndNotifications() {
    const devices = document.querySelectorAll('.status-text');
    devices.forEach(device => {
        const serialNumber = device.id.replace('status-', '');
        checkDeviceStatus(serialNumber);
        checkDeviceNotifications(serialNumber);
    });
}

// Function to show notifications in a Bootstrap modal
// function showNotificationsPopup(serialNumber) {
//     const notifications = deviceNotifications[serialNumber] || [];
//     // const modalBodyContent = notifications.map(notification => `<li>${notification.message}</li>`).join('');
//     const modalBodyContent = notifications.map(notification => `
//         <li class="d-flex justify-content-between align-items-center">
//             <span>${notification.message}</span>
//             <button class="btn btn-success btn-sm" onclick="checkNotification(${notification.id}, '${serialNumber}')">
//                 &#10003;
//             </button>
//         </li>
//     `).join('');
//
//     const modal = document.getElementById('notificationModal');
//     const modalBody = modal.querySelector('.modal-body');
//
//     modalBody.innerHTML = `<ul>${modalBodyContent}</ul>`;
//     const bootstrapModal = new bootstrap.Modal(modal);
//     bootstrapModal.show();
// }

// Function to show notifications in a Bootstrap modal
function showNotificationsPopup(serialNumber) {
    const modal = new bootstrap.Modal(document.getElementById('notificationModal'));
    refreshModal(serialNumber);
    modal.show();
}

// Function to refresh modal content
function refreshModal(serialNumber) {
    const modal = document.getElementById('notificationModal');
    const modalBody = modal.querySelector('.modal-body');
    const bellElement = document.querySelector(`#bell-${serialNumber}`);

    const notifications = deviceNotifications[serialNumber] || [];
    const modalBodyContent = notifications.map(notification => `
        <li class="d-flex justify-content-between align-items-center">
            <span>${notification.message}</span>
            <button class="btn btn-success btn-sm" onclick="checkNotification(${notification.id}, '${serialNumber}')">
                &#10003;
            </button>
        </li>
    `).join('');

    if (notifications.length === 0 && bellElement.classList.contains('has-notifications')) {
        bellElement.classList.remove('has-notifications');
    }

    modalBody.innerHTML = `<ul>${modalBodyContent}</ul>`;
}

// Function to handle notification check
function checkNotification(notificationId, serialNumber) {
    fetch(`/deviceapi/checkNotification/${notificationId}`, {
        method: 'POST'
    })
        .then(response => response.json())
        .then(success => {
            if (success) {
                // Remove the notification from the list in the JavaScript object
                deviceNotifications[serialNumber] = deviceNotifications[serialNumber].filter(
                    notification => notification.id !== notificationId
                );

                // Refresh the notification list in the modal
                refreshModal(serialNumber);
            }
        })
        .catch(error => console.error('Error checking notification:', error));
}

// Add event listeners for bell icons to show notifications
function addBellIconClickListeners() {
    const bellIcons = document.querySelectorAll('.bell-icon');
    bellIcons.forEach(bell => {
        const serialNumber = bell.id.replace('bell-', '');
        bell.addEventListener('click', (event) => {
            event.preventDefault();
            showNotificationsPopup(serialNumber);
        });
    });
}

// Initial poll when the page loads
document.addEventListener('DOMContentLoaded', function() {
    pollDeviceStatusesAndNotifications();
    addBellIconClickListeners();
    // Poll every minute
    setInterval(pollDeviceStatusesAndNotifications, 60000);
});