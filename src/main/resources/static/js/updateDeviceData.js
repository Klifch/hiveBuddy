let source;
// const dataBuffer = [];
// const labelsBuffer = [];
// let chart;

function subToEvents () {
    const deviceSerial = document.getElementById('serialNumber').value;

    if (!document.hidden) {
        const url = `/api/sse/subscribe?deviceSerial=${deviceSerial}`;
        source = new EventSource(url);
        console.log("Subscribed");
    }

    const sensor1 = document.getElementById('sensor1');
    const sensor2 = document.getElementById('sensor2');
    const sensor3 = document.getElementById('sensor3');
    const sensor4 = document.getElementById('sensor4');
    const sensor5 = document.getElementById('sensor5');

    source.onmessage = function (event) {
        const data = JSON.parse(event.data);

        if (data.sensor1 !== null && data.sensor1 !== undefined) {
            sensor1.innerText = data.sensor1;
        }
        if (data.sensor2 !== null && data.sensor2 !== undefined) {
            sensor2.innerText = data.sensor2;
        }
        if (data.sensor3 !== null && data.sensor3 !== undefined) {
            sensor3.innerText = data.sensor3;
        }
        if (data.sensor4 !== null && data.sensor4 !== undefined) {
            sensor4.innerText = data.sensor4;
        }
        if (data.sensor5 !== null && data.sensor5 !== undefined) {
            sensor5.innerText = data.sensor5;
        }
    }
}

subToEvents();

document.onvisibilitychange = function () {
    const deviceSerial = document.getElementById('serialNumber').value;

    if (document.visibilityState === 'hidden') {
        const url = `/api/sse/unsubscribe?deviceSerial=${deviceSerial}`;
        fetch(url, {
            method: 'GET'
        }).then(response => {
            if (response.ok) {
                console.log('Unsubscribed!');
            } else {
                console.error('Failed to unsubscribe');
                throw new Error("Failed to unsubscribe");
            }

            if (source !== undefined) {
                source.close();
            }
        }).catch(error => {
            console.error('Error during fetch:', error);
        });
    }
    if (document.visibilityState === 'visible') {
        subToEvents();
    }
};
//
//
// function createChart() {
//     const ctx = document.getElementById('myChart').getContext('2d');
//     const chartElement = document.getElementById('myChart');
//
//     if (chartElement.chartInstance) {
//         chartElement.chartInstance.destroy();
//     }
//
//     chart = new Chart(ctx, {
//         type: 'line',
//         data: {
//             labels: labelsBuffer,
//             datasets: [{
//                 label: 'Real-Time Sensor Data',
//                 data: dataBuffer,
//                 borderColor: 'rgba(75, 192, 192, 1)',
//                 borderWidth: 1,
//                 fill: false
//             }]
//         },
//         options: {
//             scales: {
//                 x: {
//                     type: 'linear',
//                     title: {
//                         display: true,
//                         text: 'Time (minutes)'
//                     }
//                 },
//                 y: {
//                     beginAtZero: true
//                 }
//             }
//         }
//     });
// }
//
// function updateChart(time, value) {
//     if (labelsBuffer.length > 60) {
//         labelsBuffer.shift();
//         dataBuffer.shift();
//     }
//
//     labelsBuffer.push(time);
//     dataBuffer.push(value);
//
//     chart.update();
// }
//
// function toggleRealTime(isRealTime) {
//     // const dropdowns = document.querySelectorAll('#sensorSelect, #timeSelect, #frequencySelect');
//     const sensorSelect = document.getElementById('sensorSelect');
//     const timeSelect = document.getElementById('timeSelect');
//     const frequencySelect = document.getElementById('frequencySelect');
//
//     const generateButton = document.getElementById('generateButton');
//     const realTimeButton = document.getElementById('realTimeButton');
//
//     // dropdowns.forEach(dropdown => {
//     //     dropdown.disabled = isRealTime;
//     // });
//     sensorSelect.disabled = isRealTime;
//     timeSelect.disabled = isRealTime;
//     frequencySelect.disabled = isRealTime;
//
//     generateButton.disabled = isRealTime;
//     realTimeButton.textContent = isRealTime ? 'Stop' : 'Real-Time';
// }
//
// document.getElementById('realTimeButton').addEventListener('click', function() {
//     // Real-time button handler (currently does nothing)
//     console.log('Real-time button clicked');
//
//     const serialNumber = document.getElementById('serialNumber').value;
//     const realTimeButton = document.getElementById('realTimeButton');
//
//     if (realTimeButton.textContent === 'Stop') {
//         // eventSource.close();
//         // eventSource = null;
//         toggleRealTime(false);
//         return;
//     }
//
//     toggleRealTime(true);
//
//     source.onmessage = function(event) {
//         const data = JSON.parse(event.data);
//         const time = new Date().getMinutes();
//         updateChart(time, data.value);  // Assuming 'value' contains the sensor data
//     };
//
//     source.onerror = function() {
//         console.error('Error with SSE connection.');
//         source.close();
//         source = null;
//         toggleRealTime(false);
//     };
//
//     // Initialize the chart if it's not already initialized
//     if (!chart) {
//         createChart();
//     }
// });

