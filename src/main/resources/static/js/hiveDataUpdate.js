document.addEventListener('DOMContentLoaded', function () {
    const showNewControlsButton = document.getElementById('showNewControls');
    const showOldControlsButton = document.getElementById('showOldControls');
    const newControls = document.getElementById('newControls');
    const oldControls = document.getElementById('oldControls');

    showNewControlsButton.addEventListener('click', function () {
        newControls.style.display = 'block';
        oldControls.style.display = 'none';
    });

    showOldControlsButton.addEventListener('click', function () {
        newControls.style.display = 'none';
        oldControls.style.display = 'block';
    });
});