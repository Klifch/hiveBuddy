let source;

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