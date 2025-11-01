```plantuml
@startuml
hide circle
skin rose
hide empty methods

interface MediaSource {
--
Mat getFrame();
ArrayList<Mat> getFrameArray();
}

class ImageSource {
Mat image
ArrayList<Mat> frames
--
Mat getFrame();
ArrayList<Mat> getFrameArray();
}


class VideoSource {
boolean cameraMode
VideoCapture vd
ArrayList<Mat> frames
--
Mat getFrame();
ArrayList<Mat> getFrameArray();
}

class LLMAlgo{
Image img
--
String showsDescription()
}

interface MediaAlgo{
List<String> runAlgorithm()
}

class OpenCVAlgo{
+ {static} COCO_CLASSES : List<String>
Net net
+ {static} IMG_SIZE : int
List<String> detectObjects(MediaSource source)
MediaSource mediaSource
+ {static} MODEL_NAME : String
--
List<String> runAlgorithm(Mat frame)
boolean processFrame(Mat frame)
}

class OtherAlgo{

}

' class TesseractAlgo{}

' interface TextRecAlgo {}

MediaAlgo<|..OpenCVAlgo
MediaAlgo<|.right.OtherAlgo
ImageSource -left- OpenCVAlgo: input >
ImageSource -- LLMAlgo: input >
VideoSource - OpenCVAlgo: input > 
ImageSource ..|> MediaSource
VideoSource ..|> MediaSource
OpenCVAlgo ..> MediaSource : <<uses>>

' TesseractAlgo ..|> TextRecAlgo
@enduml