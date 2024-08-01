document.addEventListener("DOMContentLoaded", function () {
    var source = new EventSource("/api/sse/subscribe?deviceSerial=asdf4");
    source.onmessage = function (event) {
        console.log("Received event: " + event.data);
    }
});

// document.addEventListener("beforeunload", function () {
//     const url = `/api/sse/unsubscribe?deviceSerial=asdf4`;
//     fetch(url, {
//         method: 'GET'
//     }).then(response => {
//         if (response.ok) {
//             console.log('Unsubscribed!');
//         } else {
//             console.error('Failed to unsubscribe');
//         }
//     }).catch(error => {
//         console.error('Error during fetch:', error);
//     });
// })

document.onvisibilitychange = function () {
    if (document.visibilityState === 'hidden') {
        const url = `/api/sse/unsubscribe?deviceSerial=asdf4`;
        fetch(url, {
            method: 'GET'
        }).then(response => {
            if (response.ok) {
                console.log('Unsubscribed!');
            } else {
                console.error('Failed to unsubscribe');
            }
        }).catch(error => {
            console.error('Error during fetch:', error);
        });
    }
};