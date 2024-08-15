let myChart;

function fetchDataAndRenderChart(serialNumber, minutes, frequency, sensor) {
    let url;
    if (sensor === 'all') {
        url = `/sensorapi/data/device/${serialNumber}?minutes=${minutes}&frequency=${frequency}`;
    } else {
        url = `/sensorapi/data/sensor/${serialNumber}?sensorNumber=${sensor}&minutes=${minutes}&average=true&frequency=${frequency}`;
    }

    fetch(url)
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => new Date(item.timestamp).toLocaleTimeString());
            const datasets = [];

            if (sensor === 'all') {
                datasets.push({
                    label: 'Sensor 1',
                    data: data.map(item => item.sensor1),
                    borderColor: 'rgba(255, 99, 132, 1)',
                    borderWidth: 1,
                    fill: false
                });
                datasets.push({
                    label: 'Sensor 2',
                    data: data.map(item => item.sensor2),
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1,
                    fill: false
                });
                datasets.push({
                    label: 'Sensor 3',
                    data: data.map(item => item.sensor3),
                    borderColor: 'rgba(255, 206, 86, 1)',
                    borderWidth: 1,
                    fill: false
                });
                datasets.push({
                    label: 'Sensor 4',
                    data: data.map(item => item.sensor4),
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    fill: false
                });
                datasets.push({
                    label: 'Sensor 5',
                    data: data.map(item => item.sensor5),
                    borderColor: 'rgba(153, 102, 255, 1)',
                    borderWidth: 1,
                    fill: false
                });
            } else {
                datasets.push({
                    label: `Sensor ${sensor}`,
                    data: data.map(item => item.value),
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1,
                    fill: false
                });
            }

            const ctx = document.getElementById('myChart').getContext('2d');

            if (myChart) {
                myChart.destroy();
            }

            myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: datasets
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching data:', error));
}
//
// function createChart() {
//     const ctx = document.getElementById('myChart').getContext('2d');
//     myChart = new Chart(ctx, {
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
//     myChart.update();
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

document.getElementById('generateButton').addEventListener('click', function() {
    const serialNumber = document.getElementById('serialNumber').value;
    const sensor = document.getElementById('sensorSelect').value;
    const minutes = document.getElementById('timeSelect').value;
    const frequency = document.getElementById('frequencySelect').value;

    fetchDataAndRenderChart(serialNumber, minutes, frequency, sensor);
});

//
//
// document.getElementById('realTimeButton').addEventListener('click', function() {
//     // Real-time button handler (currently does nothing)
//     console.log('Real-time button clicked');
//
//     const serialNumber = document.getElementById('serialNumber').value;
//     const realTimeButton = document.getElementById('realTimeButton');
//
//     if (realTimeButton.textContent === 'Stop') {
//         eventSource.close();
//         eventSource = null;
//         toggleRealTime(false);
//         return;
//     }
//
//     toggleRealTime(true);
//
//     const url = `/api/sse/subscribe?deviceSerial=${serialNumber}`
//     eventSource = new EventSource(url);
//
//     eventSource.onmessage = function(event) {
//         const data = JSON.parse(event.data);
//         const time = new Date().getMinutes();
//         updateChart(time, data.value);  // Assuming 'value' contains the sensor data
//     };
//
//     eventSource.onerror = function() {
//         console.error('Error with SSE connection.');
//         eventSource.close();
//         eventSource = null;
//         toggleRealTime(false);
//     };
//
//     // Initialize the chart if it's not already initialized
//     if (!myChart) {
//         createChart();
//     }
// });

// Fetch data and render chart on page load
document.addEventListener('DOMContentLoaded', function() {
    const serialNumber = document.getElementById('serialNumber').value;
    const minutes = 60; // Last hour
    const frequency = 5; // Frequency of 5 minutes
    const sensor = 'all'; // All sensors

    fetchDataAndRenderChart(serialNumber, minutes, frequency, sensor);
});