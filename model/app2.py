import sys
import logging
from flask import Flask, request, jsonify
from diffusers import DiffusionPipeline
import torch
from PIL import Image
from io import BytesIO
import base64

# Set up logging
logging.basicConfig(level=logging.INFO)  # Consider changing to INFO for production
logger = logging.getLogger(__name__)

app = Flask(__name__)

# Print diagnostic information
logger.info(f"Python version: {sys.version}")
logger.info(f"Python executable: {sys.executable}")
logger.info(f"Sys path: {sys.path}")

# Check PyTorch and CUDA
logger.info(f"PyTorch version: {torch.__version__}")
logger.info(f"CUDA available: {torch.cuda.is_available()}")

# Load the pretrained lighter Stable Diffusion model
device = "cuda" if torch.cuda.is_available() else "cpu"
logger.info(f"Using device: {device}")

# Load the model
pipe = DiffusionPipeline.from_pretrained("runwayml/stable-diffusion-v1-5", torch_dtype=torch.float16)
pipe.to(device)
logger.info("Model loaded successfully")

@app.route("/generate-fashion", methods=["POST"])
def generate_fashion():
    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400

    # Get the prompt
    prompt = request.form.get("prompt")
    if not prompt:
        return jsonify({"error": "Prompt is required"}), 400

    # Get the image file
    image_file = request.files['image']

    # Load the image
    try:
        uploaded_image = Image.open(image_file.stream).convert("RGB")
        logger.info("Image loaded successfully")
    except Exception as e:
        logger.error(f"Error loading image: {e}")
        return jsonify({"error": str(e)}), 400

    # Generate the image
    try:
        with torch.cuda.amp.autocast(enabled=True):
            generated_image = pipe(prompt).images[0]  # Use the prompt to generate an image
        logger.info("Image generated successfully")
    except Exception as e:
        logger.error(f"Error during image generation: {e}")
        return jsonify({"error": str(e)}), 500

    # Convert image to base64 for easier return
    try:
        buffered = BytesIO()
        generated_image.save(buffered, format="PNG")
        image_base64 = base64.b64encode(buffered.getvalue()).decode('utf-8')
        logger.info("Image converted to base64 successfully")
    except Exception as e:
        logger.error(f"Error converting image to base64: {e}")
        return jsonify({"error": str(e)}), 500

    return jsonify({"generated_image": image_base64})

if __name__ == "__main__":
    app.run(port=5000)
