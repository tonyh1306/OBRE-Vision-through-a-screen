# OBRE - Object Recognition for Blind and Visually-Impaired Individuals

OBRE is an Android accessibility application that enables visually-impaired individuals to recognize objects and text in their environment through real-time video analysis and audio feedback.

## Description

OBRE is an **Object Recognition Application** designed as an accessibility tool for low-vision and visually-impaired users. The app enables users to:

- **Recognize objects** from live camera feeds with real-time audio descriptions
- **Describe uploaded images** to receive accurate object detection results
- **Read text** in their environment in real time
- **Control all features** through audio feedback via text-to-speech (TTS)

The application provides an out-of-the-box experience with no login requirements, prioritizing ease of use and accessibility.

## Implemented Functionality

### Current Android Implementation

1. **Live Video Object Recognition**
   - Integrates Android CameraX API for real-time camera feed
   - Uses YOLOv12 ONNX model via OpenCV DNN for object detection
   - Detects objects from the COCO dataset (80+ object classes)
   - Displays detected objects with bounding boxes on screen
   - Audio feedback via text-to-speech announces detected objects

2. **Image Upload & Analysis**
   - Users can select images from device gallery
   - Processes uploaded images through detection algorithm
   - Returns list of detected objects in the image
   - Integrates with Google's Gemini API for image description (configured via API key)

3. **Text Recognition**
   - Framework in place for OCR text detection from camera and images
   - Mock implementation demonstrates the pipeline
   - Ready for integration with Tesseract or Google Cloud Vision API

4. **User Interface**
   - Fragment-based Android UI with multiple screens
   - HomeFragment: Main navigation menu
   - VideoStreamFragment: Live camera feed with real-time object detection
   - UploadImageFragment & UploadImageUI: Image upload and selection
   - ResultFragment: Display detection results
   - DetectionOverlayView: Visual overlay for detected objects on video
   - Full accessibility support with screen reader compatibility

5. **MVC Architecture**
   - **Controller**: MainActivity coordinates between UI and model components
   - **Model**: MediaSource (video/image sources), MediaAlgo (detection algorithms), TextRecognizer
   - **View**: Fragment-based UI with accessibility features

### Previous CLI Prototype Features

The command-line prototype (in `/intellij` directory) demonstrated:

- Object recognition from static images
- Text recognition simulation
- Basic MVC architecture validation

## Installation & Setup

### Prerequisites

- Android Studio (2023.1 or later)
- Android SDK 36 (compileSdk)
- Minimum SDK: Android 12 (API 32)
- Java 11 or later
- OpenCV 4.12.0 Android SDK (included as module)

### Clone & Build

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd team-c
   ```

2. **Configure Android Environment**
   - Open the project in Android Studio
   - Let Gradle sync automatically (or sync manually via `File > Sync Now`)
   - Ensure OpenCV module is correctly linked (`:opencv`)

3. **Set up API Keys** (Gemini integration)
   - Create a `local.properties` file in the project root:

     ```
     GOOGLE_API_KEY=<your-gemini-api-key>
     ```

4. **Build the App**

   ```bash
   ./gradlew :app:assembleDebug
   ```

5. **Run on Device or Emulator**
   - Connect an Android device (API 32+) or launch an emulator:
        - **Specifically for an emulator**, it is recommended that the user goes into the Android Device Management Pane. Choose `Edit > Advanced` Settings and choose Webcam0 for Rear Camera and Emulated for Front Camera.
   - Click `Run` in Android Studio

### Required Permissions

The app requests the following permissions (handled in `AndroidManifest.xml`):

- `CAMERA` - Access device camera for real-time video
- `READ_EXTERNAL_STORAGE` - Access device gallery for image upload
- `INTERNET` - API calls for Gemini integration
- `RECORD_AUDIO` - For future audio recording features

## Usage

### Main Features

##### Home Screen

- Launch the app to see the main menu
- Choose between Live Video Detection and Image Upload

##### Live Video Detection

- Point camera at objects to detect them
- Detected objects appear with bounding boxes
- Audio feedback announces detected objects

##### Image Upload

- Select "Upload Image" from the home menu
- Choose an image from device gallery
- Processing time: typically 1-3 seconds on modern devices
- Results displayed with detected objects and optional description via Gemini API

##### Text Recognition

- Select "Recognize Text" mode
- Point camera at text or upload image with text
- Extracted text displayed with audio feedback

## Known Limitations

1. **Text Recognition** - Currently a framework; real OCR integration pending (Tesseract/Google Cloud Vision)

2. **Gemini API Integration** - Optional image description feature requires valid API key

3. **Performance** - Real-time detection optimized for Android 12+ devices with mid-range hardware and above

4. **Model Size** - YOLOv12n ONNX model (~6-8MB); full YOLOv12 variants may require more resources

5. **Camera Constraints** - Best performance in well-lit environments; low-light detection accuracy reduced

## Authors and Acknowledgment

**Authors**: Maya Laidler and Tony Nguyen

**Key Technologies**:

- Android CameraX API for real-time camera integration
- OpenCV 4.12.0 with ONNX runtime for DNN inference
- YOLOv12 ONNX model for object detection
- Google Gemini API for image description
- Android Text-to-Speech API for accessibility

## License

This project is part of CMPU-203 at Vassar College. License details pending.

## Project Status

**Current Phase**: Android Implementation (Iteration 2)

The project has evolved from a CLI prototype to a full Android application with:

- Live video object detection via camera
- Image upload and analysis
- Fragment-based UI with accessibility support
- Text recognition framework (implementation in progress)
- Gemini API integration (optional, requires API key)
- Future: Haptic feedback, voice commands, offline model variants

**Next Steps**: Refine detection accuracy, integrate advanced OCR, enhance accessibility features for visually-impaired users.
