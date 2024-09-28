function updateUploadMessage() {
    const fileInput = document.getElementById('fileUpload');
    const uploadMessage = document.getElementById('uploadMessage');

    if (fileInput.files.length > 0) {
        uploadMessage.textContent = fileInput.files[0].name; // Show the file name
    } else {
        uploadMessage.style.display='flex';
        uploadMessage.textContent = 'No file attached'; // Reset the message
    }
}