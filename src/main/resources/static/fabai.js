document.addEventListener('DOMContentLoaded', function () {});

document.getElementById('generateButton').addEventListener('click', function () {
    const promptInput = document.getElementById('promptInput').value;
    const fileInput = document.getElementById('fileUpload');

    // Create a FormData object
    const formData = new FormData();
    if (fileInput.files.length > 0) {
        formData.append('image', fileInput.files[0]); // Add the uploaded file
    }
    formData.append('prompt', promptInput); // Add the prompt text

    // Send the form data to a URL
    fetch('http://localhost:8080/generate', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                return response.blob(); // Return the response as a blob (binary data)
            }
            throw new Error('Network response was not ok.');
        })
        .then(blob => {
            const generatedImage = document.getElementById('generatedImage');
            const errorMessage = document.getElementById('errorMessage');

            // Create a URL for the blob and display it in the img tag
            const objectURL = URL.createObjectURL(blob);
            generatedImage.src = objectURL;
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
