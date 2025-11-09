# CMPU-203 F25 - Team C

## OBRE
A image recognition app for the visually impaired. 
The prototype demonstrates basic MVC architecture with a controller, model, and view, supporting:

- Object recognition (placeholder for future OpenCV integration)
- Text recognition (mock prototype, simulates OCR output)
- Command-line interface for user input and output

## Description
An Object Recognition Application that provides accessibility for visually-impaired individuals
real-time notification from the video feed with
a user-friendly and out-of-the-box experience. With additional options to upload a file and get an OCR reading of the text.

## Implemented Functionality

1. **Object Recognition (Image-based)**
    - Accepts `.jpg` and `.png` images from the user.
    - Currently prints placeholder messages simulating object detection.
    - Prepares structure for future integration with OpenCV algorithms.

2. **Text Recognition**
    - Accepts `.jpg` or `.png` images for text scanning.
    - Outputs a simulated recognized text message.
    - Demonstrates the MVC flow without requiring real OCR.

3. **User Interface**
    - Command-line interface prompting for input.
    - Validates input file types and displays errors for invalid files.
    - Provides placeholder outputs for both object and text recognition.

## Installation

1. Clone the repository
2. Download dependencies:
   1. OpenCV (Follow OpenCV installation instructions for your OS from their official documentation.)
      1. Set up VM options to include OpenCV native libraries. (e.g., `-Djava.library.path=/path/to/opencv/build/java/x64`)
      2. Add OpenCV jar files to your project dependencies.
   2. YOLOv12
      1. If the app is not working, please refer to the 
      YOLOv12 instructions on how to export the model to .onnx format and 
      include it in the project in the `/resources/` directory from this documentation: https://docs.ultralytics.com/modes/export/#key-features-of-export-mode
3. Compile the source files 
4. Run the Application

## Usage
currently being implemented as a CLI prototype

## Known Limitations
Some limitations include: 
1. Text recognition is simulated and so no real OCR is performed.
2. It is Command-line interface only. There is no GUI. 
3. Currently supports only .jpg and .png images.

## Authors and acknowledgment
Authors: Maya Laidler and Tony Nguyen

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
