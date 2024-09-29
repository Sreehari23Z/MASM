import sys
import logging
from flask import Flask, request, jsonify
from diffusers import DiffusionPipeline
import torch
from PIL import Image
from io import BytesIO
import base64


logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)


logger.info(f"Python version: {sys.version}")
logger.info(f"Python executable: {sys.executable}")
logger.info(f"Sys path: {sys.path}")


logger.info(f"PyTorch version: {torch.__version__}")
logger.info(f"CUDA available: {torch.cuda.is_available()}")


device = "cuda" if torch.cuda.is_available() else "cpu"
logger.info(f"Using device: {device}")

pipe = DiffusionPipeline.from_pretrained("Falah/fashion-model").to(device)
logger.info("Model loaded successfully")

@app.route("/generate-fashion", methods=["POST"])
def generate_fashion():
    if 'image' not in request.files:
        return jsonify({"error": "No image file provided"}), 400

    prompt = request.form.get("prompt")
    if not prompt:
        return jsonify({"error": "Prompt is required"}), 400

    image_file = request.files['image']

    try:
        uploaded_image = Image.open(image_file.stream).convert("RGB")
        logger.info("Image loaded successfully")
    except Exception as e:
        logger.error(f"Error loading image: {e}")
        return jsonify({"error": str(e)}), 400

    try:
        with torch.no_grad(), torch.cuda.amp.autocast():
            generated_image = pipe(prompt, image=uploaded_image).images[0]
        logger.info("Image generated successfully")
    except Exception as e:
        logger.error(f"Error during image generation: {e}")
        return jsonify({"error": str(e)}), 500

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
    app.run(port=5000, debug=True)
