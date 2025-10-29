```plantuml
@startuml
hide circle
skin rose
hide empty methods
skinparam Linetype ortho

interface UI <<Interface>> {
  + setListener(Controller ctrl)
  + startUI()
  + displayDetections(List<String>)
}

class CmdLineUI {
  - controller : Controller
  + CmdLineUI()
  + setListener(Controller ctrl)
  + startVideoStreaming()
  + displayDetections(List<String>)
}

UI <|.. CmdLineUI

class Controller {
  - ui : UI
  + Controller(UI ui)
  + main(String[] args) {static}
  + processImageFile(String filename)
}

Controller ..> UI : <<uses>>
CmdLineUI ..> Controller : <<sends commands>>

interface MediaSource <<Interface>> {
  - getFrame : Mat
  - getFrameArray : ArrayList<Mat>
  + readFrame() : Object
}

class ImageSource {
  - filePath : String
  + ImageSource(String filePath)
  + getMediaAddress() : String
  + readFrame() : Object
}

MediaSource <|.. ImageSource

interface ObjRecAlgo <<Interface>> {
    + detectObjects(MediaSource) : List<String>
}

class OpenCVAlgo {
  + OpenCVAlgo()
  + detectObjects(MediaSource source) : List<String>
  - drawNoDetectionsOverlay(Mat frame): void
  - processFrame(Mat frame, Mat resizedImage, Size netSize) : List<String> 
}


Controller --> MediaSource : <<uses>>
Controller --> ObjRecAlgo : <<uses>>
ObjRecAlgo <|.. OpenCVAlgo
Controller ..> ImageSource : <<creates>>
Controller ..> OpenCVAlgo : <<creates>>
@enduml