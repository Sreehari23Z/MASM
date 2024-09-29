import requests
import base64
import logging

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

def test_fashion_model(image_path, prompt):
    url = "http://localhost:5000/generate-fashion"
    
    with open(image_path, 'rb') as img:
        files = {'image': img}
        data = {'prompt': prompt}

        try:
            response = requests.post(url, files=files, data=data)
            logger.info(f"Status code: {response.status_code}")
            logger.info(f"Response headers: {response.headers}")
            logger.info(f"Raw response: {response.text}")
            
            response.raise_for_status() 
            
            try:
                response_json = response.json()
            except requests.exceptions.JSONDecodeError:
                logger.error("Response is not in JSON format.")
                logger.error(f"Response content: {response.text}")
                return

            if "generated_image" in response_json:
                logger.info("Image generated successfully!")
                generated_image_base64 = response_json["generated_image"]
                with open("generated_image.png", "wb") as f:
                    f.write(base64.b64decode(generated_image_base64))
                logger.info("Generated image saved as 'generated_image.png'.")
            elif "error" in response_json:
                logger.error(f"Error from server: {response_json['error']}")
            else:
                logger.warning("Unexpected response format.")
                logger.warning(f"Response content: {response_json}")
        except requests.exceptions.RequestException as e:
            logger.error(f"An error occurred while making the request: {e}")

if __name__ == "__main__":
    image_path = "image/m1.jpg"  
    prompt = ("A high-quality image of a fashionable middle-aged Indian woman wearing a pink and blue saree. "
          "The saree has intricate golden elephant designs integrated into the fabric, "
          "with fine embellishments on the borders. The model has a clean face, is skinny, and poses elegantly "
          "to showcase the saree's detailed patterns. Minimalist background to emphasize the saree's colors and design.")  
    test_fashion_model(image_path, prompt)