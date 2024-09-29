function updateUploadMessage() {
    const fileInput = document.getElementById('fileUpload');
    const uploadMessage = document.getElementById('uploadMessage');

    if (fileInput.files.length > 0) {
        uploadMessage.textContent = fileInput.files[0].name; // Show the file name
    } else {
        uploadMessage.style.display = 'flex';
        uploadMessage.textContent = 'No file attached'; // Reset the message
    }
}

// Handle the click event for the generate button
document.getElementById('generateButton').addEventListener('click', function () {
    const promptInput = document.getElementById('promptInput').value;
    const fileInput = document.getElementById('fileUpload');

    // Create a FormData object
    const formData = new FormData();
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]); // Add the uploaded file
    }
    formData.append('prompt', promptInput); // Add the prompt text

    // Send the form data to a URL
    fetch('http://localhost:8080/generate', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // Parse the response as JSON
            }
            throw new Error('Network response was not ok.');
        })
        .then(data => {
            console.log('Success:', data);
            // Assuming the response contains the Base64 string of the generated image
            const base64Image = data.imageBase64; // Adjust this based on your API response structure
            const generatedImage = document.getElementById('generatedImage');
            const imageContainer = document.getElementById('imageContainer');
            const errorMessage = document.getElementById('errorMessage');

            // Display the generated image using Base64
            generatedImage.src = 'data:image/png;base64,' + base64Image; // Ensure the correct MIME type (change png to jpg if needed)
            generatedImage.style.display = 'block';
            errorMessage.style.display = 'none'; // Hide error message if any
        })
        .catch(error => {
            console.error('Error:', error);
            const errorMessage = document.getElementById('errorMessage');
            errorMessage.textContent = 'Failed to generate image. Please try again.';
            errorMessage.style.display = 'block'; // Show error message
        });
});
