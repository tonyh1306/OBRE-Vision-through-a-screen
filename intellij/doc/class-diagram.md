```plantuml
@startuml
hide circle
skin rose
hide empty methods
skinparam Linetype ortho

interface UI <<Interface>> {
  setListener(Controller ctrl)
  startUI()
  displayDetections(List<String>)
}

class CmdLineUI {
  CmdLineUI()
  setListener(Controller ctrl)
  startVideoStreaming()
  displayDetections(List<String>)
}

UI <|.. CmdLineUI

class Controller {
  Controller(UI ui)
  main(String[] args) {static}
  processImageFile(String filename)
}

Controller ..> UI : <<uses>>
CmdLineUI ..> Controller : <<sends commands>>

interface MediaSource <<Interface>> {
Mat getFrame();
ArrayList<Mat> getFrameArray();
}

class ImageSource {
    Mat image
    ArrayList<Mat> frames
    --
  ImageSource(String filePath)
  getMediaAddress() : String
  readFrame() : Object
}

MediaSource <|.. ImageSource

interface MediaAlgo <<Interface>> {
--
runAlgorithm(Mat frame) : List<String>
}

class OpenCVAlgo {
+ {static} COCO_CLASSES : List<String>
Net net
+ {static} IMG_SIZE : int
List<String> detectObjects(MediaSource source)
MediaSource mediaSource
+ {static} MODEL_NAME : String
--
  OpenCVAlgo()
  detectObjects(MediaSource source) : List<String>
}


Controller --> MediaSource : <<uses>>
Controller --> MediaAlgo : <<uses>>
MediaAlgo <|.. OpenCVAlgo
Controller ..> ImageSource : <<creates>>
Controller ..> OpenCVAlgo : <<creates>>
@enduml