from PIL import Image
import pytesseract
from pytesseract import Output

print(pytesseract.image_to_data(Image.open(
    './images.jpeg'), output_type=Output.DICT))

print('\n\n')

print(pytesseract.image_to_string(Image.open(
    './images.jpeg'), output_type=Output.BYTES))
