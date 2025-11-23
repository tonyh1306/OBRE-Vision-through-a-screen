```plantuml
@startuml
skin rose
hide empty methods
skinparam Linetype ortho

interface UI <<Interface>> {
  void setListener(Listener listener)
  void startDetecting()
  void startVideoStreaming()
  void displayDetections(List<String> detections)
  void displayMessage(String message)
}

interface UI.Listener <<Interface>> {
  void onUploadImage(String fileName)
}

class CmdLineUI implements UI {
  ps : PrintStream
  in : Scanner
  listener : UI.Listener
  --
  CmdLineUI()
  void startDetecting()
  void startVideoStreaming()
  void setListener(UI.Listener listener)
  void displayDetections(List<String> detections)
  void displayMessage(String message)
  boolean isValidImageFile(String filename)
}

class Controller implements UI.Listener {
  ui : UI
  option : Options
  --
  Controller(CmdLineUI ui)
  void onUploadImage(String filename)
  void processTextRecognition(String filename)
  void main(String[] args) {static}
}

UI <|.. CmdLineUI
UI.Listener <|.. Controller
Controller --> UI : ui

interface MediaSource <<Interface>> {
  Mat getFrame()
  ArrayList<Mat> getFrameArray()
}

class ImageSource implements MediaSource {
  image : Mat
  frames : ArrayList<Mat>
  --
  ImageSource(String imageAddress)
  Mat getFrame()
  ArrayList<Mat> getFrameArray()
}

class VideoSource implements MediaSource {
  cameraMode : boolean
  vd : VideoCapture
  frames : ArrayList<Mat>
  --
  Mat getFrame()
  ArrayList<Mat> getFrameArray()
}

interface MediaAlgo <<Interface>> {
  List<String> runAlgorithm()
}

class OpenCVAlgo implements MediaAlgo {
  + {static} MODEL_NAME : String
  + {static} IMG_SIZE : int
  + {static} COCO_CLASSES : String[]
  mediaSource : MediaSource
  net : Net
  detectedObjects : List<String>
  --
  OpenCVAlgo(MediaSource mediaSource)
  List<String> runAlgorithm()
  boolean processFrame(Mat image, Mat resizedImage, Size netSize)
  void drawNoDetectionsOverlay(Mat frame)
}

class TextRecognizer {
  --
  String recognizeText(String filename)
}

class LLMAlgo {
  API_KEY : String
  --
  LLMAlgo()
}

class ResourceUtils {
  --
  String getResourcePath(String resourceName) {static}
}

ImageSource --|> MediaSource
VideoSource --|> MediaSource
OpenCVAlgo --|> MediaAlgo
OpenCVAlgo --> MediaSource : mediaSource
Controller --> ImageSource : creates
Controller --> OpenCVAlgo : creates
Controller --> TextRecognizer : uses

@enduml
