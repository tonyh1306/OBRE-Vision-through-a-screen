```plantuml
@startuml
skin rose
hide empty methods

interface MediaSource {
--
Mat getFrame()
ArrayList<Mat> getFrameArray()
}

class ImageSource implements MediaSource {
  image : Mat
  frames : ArrayList<Mat>
  --
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

interface MediaAlgo {
--
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
  List<String> runAlgorithm()
  boolean processFrame(Mat image, Mat resizedImage, Size netSize)
}

class TextRecognizer {
  --
  String recognizeText(String filename)
}

class LLMAlgo {
  API_KEY : String
}

class ResourceUtils {
  --
  String getResourcePath(String resourceName) {static}
}

ImageSource --|> MediaSource
VideoSource --|> MediaSource
OpenCVAlgo --|> MediaAlgo
OpenCVAlgo --> MediaSource : mediaSource
OpenCVAlgo --> ResourceUtils : uses

@enduml
