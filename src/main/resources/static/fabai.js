document.addEventListener('DOMContentLoaded', function () {
    // Ensure the event listener is attached once
    const generateButton = document.getElementById('generateButton');

    // Attach event listener only once to avoid duplicate submissions
    generateButton.addEventListener('click', function (event) {
        // Prevent default behavior if the button is inside a form
        event.preventDefault();

        // Get the file input and prompt values
        const promptInput = document.getElementById('promptInput').value.trim();
        const fileInput = document.getElementById('fileUpload');

        // Log the selected file and prompt for debugging
        console.log('Selected File:', fileInput.files[0]);
        console.log('Prompt Text:', promptInput);

        // Create a FormData object
        const formData = new FormData();

        // Validate if a file is selected
        if (fileInput.files.length > 0) {
            formData.append('image', fileInput.files[0]); // Add the selected file
        } else {
            console.error('Error: No file selected.');
            alert('Please select a file to upload.'); // Provide user feedback
            return; // Stop if no file is selected
        }

        // Validate the prompt input
        if (promptInput) {
            formData.append('prompt', promptInput); // Add the prompt text
        } else {
            console.error('Error: No prompt entered.');
            alert('Please enter a prompt.'); // Provide user feedback
            return; // Stop if no prompt is provided
        }

        // Log the FormData contents for debugging
        for (let [key, value] of formData.entries()) {
            console.log(`${key}:`, value);
        }

        // Send the form data to the backend URL
        fetch('http://localhost:8080/generate', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                // Check if the response is OK (status code 200-299)
                if (response.ok) {
                    return response.json(); // Parse the response as JSON
                }
                throw new Error(`Network response was not ok: ${response.statusText}`);
            })
            .then(data => {
                console.log('Server Response:', data);

                // Assuming the response contains the Base64 string of the generated image
                const base64Image = data.imageBase64;

                // Get the image and error elements
                const generatedImage = document.getElementById('generatedImage');
                const errorMessage = document.getElementById('errorMessage');

                // Display the generated image using Base64
                if (base64Image) {
                    generatedImage.src = 'data:image/png;base64,' + base64Image; // Set the image source
                    generatedImage.style.display = 'block'; // Show the generated image
                    errorMessage.style.display = 'none'; // Hide the error message
                } else {
                    throw new Error('The response did not contain a valid image.');
                }
            })
            .catch(error => {
                console.error('Error:', error);

                // Display error message on failure
                const errorMessage = document.getElementById('errorMessage');
                errorMessage.textContent = 'Failed to generate image. Please try again.';
                errorMessage.style.display = 'block'; // Show the error message
            });
    }, { once: true }); // Attach the listener with {once: true} to ensure it runs only once
});
