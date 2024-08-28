document.addEventListener('DOMContentLoaded', function () {
    const generateButton = document.getElementById('generateButton');
    const avgGenerateButton = document.getElementById('generateButtonOld');

    const realTimeButton = document.getElementById('realTimeButton');
    const newControlsButton = document.getElementById('showNewControls');
    const oldControlsButton = document.getElementById('showOldControls');

    const oldControlsMenu = document.getElementById('oldControls');
    const newControlsMenu = document.getElementById('newControls');

    const datePicker = document.getElementById('datePicker');
    const sensorSelect = document.getElementById('sensorSelect');
    const oldSensorSelect = document.getElementById('oldSensorSelect');
    const timeFrom = document.getElementById('timeFrom');
    const timeTo = document.getElementById('timeTo');
    const serialNumber = document.getElementById('serialNumber').value;
    let myChart;
    const connectionId = serialNumber + 'chart'
    let eventSource;
    let lastTimeStamp;

    // initialize the chart with default settings
    function initializeChart() {
        const ctx = document.getElementById('myChart').getContext('2d');
        myChart = new Chart(ctx, {
            type: 'scatter',
            data: {
                datasets: [{
                    label: 'Sensor Data',
                    data: [],
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 2,
                    fill: false,
                    parsing: false, // Disable parsing to allow custom x/y processing
                }]
            },
            options: {
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: 'hour', // Default unit is hour, will adjust dynamically
                            tooltipFormat: 'HH:mm',
                            displayFormats: {
                                hour: 'HH:mm',
                                minute: 'HH:mm',
                            }
                        },
                        title: {
                            display: true,
                            text: 'Time'
                        }
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Value'
                        },
                        beginAtZero: true,
                        ticks: {
                            stepSize: 10
                        }
                    }
                }
            }
        });
    }

    // set the Y-axis based on the selected sensor
    function getYAxisConfig(sensor) {
        if (sensor === '1') {
            return { min: 0, max: 50, stepSize: 10 };
        } else if (sensor === '2') {
            return { min: 10, max: 100, stepSize: 10 };
        } else if (sensor === '4') {
            return { min: 0, max: 3, stepSize: 1, labels: ['No water', 'Small leak', 'Large leak', 'Flood'] };
        } else {
            return { min: 0, max: 100, stepSize: 10 };  // Default
        }
    }

    function getChartLabel(sensor) {
        if (sensor === '1') {
            return 'Temperature'
        } else if (sensor === '2') {
            return 'Humidity'
        } else if (sensor === '3') {
            return 'Weight'
        } else if (sensor === '4') {
            return 'Water level'
        } else if (sensor === '5') {
            return 'Sound'
        } else {
            return 'Sensor Data'
        }
    }

    // validate the form fields
    function validateForm() {
        return datePicker.value && sensorSelect.value;
    }

    // build the API URL based on user inputs
    function buildApiUrl() {
        let url = `/sensorapi/data/device/${serialNumber}/sensor/${sensorSelect.value}?date=${datePicker.value}`;

        if (timeFrom.value && timeTo.value) {
            url += `&from=${datePicker.value}T${timeFrom.value}&to=${datePicker.value}T${timeTo.value}`;
        }

        return url;
    }

    //handle the API response and update the chart
    function updateChart(data) {
        if (myChart) {
            myChart.destroy();
        }

        console.log(data)
        console.log(data[0].timestamp)
        console.log(new Date(data[0].timestamp))

        const yAxisConfig = getYAxisConfig(sensorSelect.value);
        const chartLabel = getChartLabel(sensorSelect.value)

        const ctx = document.getElementById('myChart').getContext('2d');
        myChart = new Chart(ctx, {
            type: 'line',
            data: {
                datasets: [{
                    label: chartLabel,
                    data: data.map(item => ({
                        x: new Date(item.timestamp),  // Use the timestamp as x
                        y: item.value                  // Use the value as y
                    })),
                    pointRadius: 1, // include in config
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 2,
                    // fill: false,
                    // parsing: false, // Disable parsing to allow custom x/y processing
                }]
            },
            options: {
                maintainAspectRatio: false,
                scales: {
                    x: {
                        type: 'time',
                        time: {
                            unit: determineTimeUnit(data),  // Adjust this based on your data granularity
                            displayFormats: {
                                hour: 'HH:mm',
                                minute: 'HH:mm'
                            },
                            // stepSize: 1, //
                        },
                        title: {
                            display: true,
                            text: 'Time'
                        },
                        ticks: {
                            autoSkip: true,
                            maxTicksLimit: 12,
                        },
                        // min: data[0].timestamp,
                        // max: data[data.length - 1].timestamp,
                        // bounds: 'ticks',
                    },
                    y: {
                        title: {
                            display: true,
                            text: 'Value'
                        },
                        beginAtZero: true,
                        ticks: {
                            stepSize: yAxisConfig.stepSize,
                            min: yAxisConfig.min,
                            max: yAxisConfig.max,
                            callback: yAxisConfig.labels ? function (value, index) {
                                return yAxisConfig.labels[index];
                            } : undefined
                        }
                    }
                    // y: yAxisConfig // Assuming getYAxisConfig returns the y-axis configuration
                }
            }
            // options: { man, It took me 8 hours to figure out how to work with this crap
            //     scales: {
            //         x: {
            //             type: 'time',
            //             time: {
            //                 unit: determineTimeUnit(data), // Dynamically determine the unit
            //                 tooltipFormat: 'HH:mm',
            //                 displayFormats: {
            //                     hour: 'HH:mm',
            //                     minute: 'HH:mm',
            //                 }
            //             },
            //             title: {
            //                 display: true,
            //                 text: 'Time'
            //             },
            //             ticks: {
            //                 autoSkip: true,
            //                 maxTicksLimit: 12,
            //             }
            //         },
            //         y: {
            //             title: {
            //                 display: true,
            //                 text: 'Value'
            //             },
            //             beginAtZero: true,
            //             ticks: {
            //                 stepSize: yAxisConfig.stepSize,
            //                 min: yAxisConfig.min,
            //                 max: yAxisConfig.max,
            //                 callback: yAxisConfig.labels ? function(value, index) {
            //                     return yAxisConfig.labels[index];
            //                 } : undefined
            //             }
            //         }
            //     }
            // }
        });
    }

    // Function to determine the time unit (hour, minute) based on the time range
    function determineTimeUnit(data) {
        const timeDiff = (new Date(data[data.length - 1].timestamp) - new Date(data[0].timestamp)) / 3600000; // time difference in hours

        if (timeDiff <= 6) {
            return 'minute'; // Display every 30 minutes for time range <= 6 hours
        } else if (timeDiff <= 12) {
            return 'hour'; // Display every 1 hour for time range <= 12 hours
        } else {
            return 'hour'; // Display every 2 hours for time range > 12 hours
        }
    }

    // Event listener for the Generate button
    generateButton.addEventListener('click', function () {
        if (!validateForm()) {
            alert('Please fill all required fields.');
            return;
        }

        const apiUrl = buildApiUrl();

        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    alert('No data available for the selected parameters.');
                    return;
                }
                updateChart(data);
            })
            .catch(error => console.error('Error fetching sensor data:', error));
    });

    avgGenerateButton.addEventListener('click', function () {
        const sensor = document.getElementById('oldSensorSelect').value;
        const minutes = document.getElementById('timeSelect').value;
        const frequency = document.getElementById('frequencySelect').value;

        const apiUrl = `/sensorapi/data/sensor/${serialNumber}?sensorNumber=${sensor}&minutes=${minutes}&average=true&frequency=${frequency}`


        fetch(apiUrl)
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    alert('No data available for the selected parameters.');
                    return;
                }
                updateChart(data);
            })
            .catch(error => console.error('Error fetching sensor data:', error));
    });

    function logData(data, sensor) {
        const currentTimeStamp = new Date(data.timestamp).getTime();
        if (lastTimeStamp === undefined || currentTimeStamp !== lastTimeStamp) {
            lastTimeStamp = currentTimeStamp;
            console.log('New data:', data);

            switch (sensor) {
                case '1':
                    addDataPointToChart(data.sensor1, data.timestamp);
                    break;
                case '2':
                    addDataPointToChart(data.sensor2, data.timestamp);
                    break;
                case '4':
                    addDataPointToChart(data.sensor4, data.timestamp);
                    break;
                case '5':
                    addDataPointToChart(data.sensor5, data.timestamp);
                    break;
                default:
                    console.log("Error updating chart!");
                    console.log(sensor)
                    console.log(data.timestamp + '  ' + data.sensor1);
                    break;
            }
        }
    }

    function addDataPointToChart(value, timestamp) {
        if (myChart && myChart.data.datasets.length > 0) {
            const dataset = myChart.data.datasets[0];

            console.log("Adding new data point:", { x: new Date(timestamp), y: value });
            console.log(dataset);

            myChart.data.datasets.forEach((dataset) => {
                dataset.data.push({
                    x: new Date(timestamp),
                    y: value
                });
            });

            // dataset.data.push({
            //     x: new Date(timestamp),
            //     y: value
            // });

            myChart.update();
        } else {
            console.error("Chart or dataset not initialized.");
        }
    }

    function buildRealtimeChart(sensor) {
        const now = new Date();
        const tenMinutesAgo = new Date(now.getTime() - 5 * 60000); // 10 minutes ago

        // if (oldControlsMenu.style.display === 'none') {
        //     sensor = sensorSelect.value;
        // } else if (newControlsMenu.style.display === 'none') {
        //     sensor = oldSensorSelect.value;
        // }

        const formatDate = (date) => {
            return date.toISOString().slice(0, 19);
        }

        const apiUrl = `/sensorapi/data/device/${serialNumber}/sensor/${sensor}?date=${formatDate(now).slice(0, 10)}&from=${formatDate(tenMinutesAgo)}&to=${formatDate(now)}`;

        fetch(apiUrl)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                updateChart(data);
            })
            .catch(error => console.error('Error fetching sensor data:', error));
    }

    realTimeButton.addEventListener('click', function () {
        if (realTimeButton.innerText === 'RealTime' && eventSource === undefined) {
            let selectedSensor;

            oldControlsButton.disabled = true;
            newControlsButton.disabled = true;
            generateButton.disabled = true;
            avgGenerateButton.disabled = true;

            const now = new Date();
            const tenMinutesAgo = new Date(now.getTime() - 10 * 60000); // 10 minutes ago

            const formatDate = (date) => {
                return date.toISOString().slice(0, 19);
            }

            if (oldControlsMenu.style.display === 'none') {
                selectedSensor = sensorSelect.value;
            } else if (newControlsMenu.style.display === 'none') {
                selectedSensor = oldSensorSelect.value;
            }

            buildRealtimeChart(selectedSensor);

            const url = `/api/sse/subscribe?deviceSerial=${serialNumber}&connectionId=${connectionId}`;
            eventSource = new EventSource(url);
            console.log("Subscribed");

            eventSource.onmessage = function (event) {
                const data = JSON.parse(event.data);
                const currentTimeStamp = new Date(data.timestamp).getTime();

                logData(data, selectedSensor);
            };

            realTimeButton.innerText = 'Stop'
        } else if (realTimeButton.innerText === 'Stop' && eventSource) {
            eventSource.close();
            eventSource = undefined;

            realTimeButton.innerText = 'RealTime';

            oldControlsButton.disabled = false;
            newControlsButton.disabled = false;
            generateButton.disabled = false;
            avgGenerateButton.disabled = false;
        }

    });

    // Initialize the empty chart on page load
    initializeChart();
});