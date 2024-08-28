let source;
// const dataBuffer = [];
// const labelsBuffer = [];
// let chart;

function subToEvents () {
    const deviceSerial = document.getElementById('serialNumber').value;
    const connectionId = deviceSerial + 'dash'

    if (!document.hidden) {
        const url = `/api/sse/subscribe?deviceSerial=${deviceSerial}&connectionId=${connectionId}`;
        source = new EventSource(url);
        console.log("Subscribed");
    }

    const sensor1 = document.getElementById('sensor1');
    const sensor2 = document.getElementById('sensor2');
    // const sensor3 = document.getElementById('sensor3');
    const sensor4 = document.getElementById('sensor4');
    const sensor5 = document.getElementById('sensor5');

    source.onmessage = function (event) {
        const data = JSON.parse(event.data);

        if (data.sensor1 !== null && data.sensor1 !== undefined) {
            sensor1.innerText = (Math.round(data.sensor1 * 10) /10) + '°C';
        }
        if (data.sensor2 !== null && data.sensor2 !== undefined) {
            sensor2.innerText = (Math.round(data.sensor2 * 10) /10) + '%';
        }
        // if (data.sensor3 !== null && data.sensor3 !== undefined) {
        //     sensor3.innerText = data.sensor3;
        // }
        if (data.sensor4 !== null && data.sensor4 !== undefined) {
            const value4 = Math.round(data.sensor4 * 10) / 10
            switch (value4) {
                case 0:
                    sensor4.innerText = "No water";
                    break;
                case 1:
                    sensor4.innerText = "Small leak";
                    break;
                case 2:
                    sensor4.innerText = "Large leak";
                    break;
                case 3:
                    sensor4.innerText = "Flood";
                    break;
                default:
                    sensor4.innerText = value4;
                    break;
            }
        }
        if (data.sensor5 !== null && data.sensor5 !== undefined) {
            if (data.sensor5 === '0' || data.sensor5 === 0) {
                sensor5.innerText = 'Quiet';
            } else {
                sensor5.innerText = 'Party';
            }
        }
    }
}

subToEvents();

// It works, but I think it's redundant
//
// document.onvisibilitychange = function () {
//     const deviceSerial = document.getElementById('serialNumber').value;
//
//     if (document.visibilityState === 'hidden') {
//         const url = `/api/sse/unsubscribe?deviceSerial=${deviceSerial}`;
//         fetch(url, {
//             method: 'GET'
//         }).then(response => {
//             if (response.ok) {
//                 console.log('Unsubscribed!');
//             } else {
//                 console.error('Failed to unsubscribe');
//                 throw new Error("Failed to unsubscribe");
//             }
//
//             if (source !== undefined) {
//                 source.close();
//             }
//         }).catch(error => {
//             console.error('Error during fetch:', error);
//         });
//     }
//     if (document.visibilityState === 'visible') {
//         subToEvents();
//     }
// };
