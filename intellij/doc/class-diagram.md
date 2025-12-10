@startuml
set separator none
skinparam linetype ortho
hide empty methods

' Interfaces
interface MediaSource {
+ getFrame() : Mat
+ getFrameArray() : List<Mat>
}

interface MediaAlgo {
+ runOnFrame(frame : Mat) : List<DetectedObject>
}

interface VideoStreamUI
interface ResultUI

interface ResultListener {
+ onSwitchToUpload(resultText : String) : void
}

interface UploadListener {
+ onPickImageRequested() : void
+ onAnalyzeImageRequested(image : Bitmap) : void
+ onSwitchBackToStream() : void
}

interface LLMListener {
+ onSuccess(responseText : String) : void
+ onError(e : Exception) : void
}

' Classes
class CameraController {
- mainUI : MainUI
- videoSource : VideoSource
- imageSource : ImageSource
- mediaAlgo : MediaAlgo
}

class MainUI {
- currentFragment : Fragment
}

class VideoStreamFragment {
- binding : FragmentVideoStreamBinding
- listener : VideoStreamFragment.Listener
}

class UploadImageFragment {
- selectedBitmap : Bitmap
- listener : UploadListener
- binding : FragmentUploadImageBinding
- pendingDetectionText : String
- history : List<String>
- static ARG_RESULT_TEXT : String
}

class ResultFragment {
- resultText : String
- listener : ResultListener
- binding : FragmentResultBinding
- tts : TextToSpeech
}

class DetectionOverlayView {
- detectedObjects : List<DetectedObject>
- boxPaint : Paint
- textPaint : Paint
+ DetectionOverlayView(context : Context, attrs : AttributeSet)
+ setDetectedObjects(objects : List<DetectedObject>) : void
+ onDraw(canvas : Canvas) : void
}

class VideoSource {
- capture : VideoCapture
}

class ImageSource {
- image : Mat
}

class OpenCVAlgo {
- mediaSource : MediaSource
+ runOnFrame(frame : Mat) : List<DetectedObject>
}

class LLMAlgo {
- apiKey : String
+ LLMAlgo()
+ runOnFrame(frame : Mat) : List<DetectedObject>
+ static bitmapFromUri(context : Context, uri : Uri) : Bitmap
+ sendImageToGemini(bitmap : Bitmap, listener : LLMListener) : void
}

class TextRecognizer {
+ analyze(imageProxy : ImageProxy) : void
}

class ResourceUtils {
+ static getResourcePath(fileName : String) : String
+ static copyResourceToFile(context : Context, resId : int, fileName : String) : String
}

class DetectedObject {
+ DetectedObject(name : String, x : Double, y : Double, w : Double, h : Double)
+ getName() : String
}

' Inheritance / Implementation
MediaSource <|.. ImageSource
MediaSource <|.. VideoSource
MediaAlgo <|.. OpenCVAlgo
VideoStreamFragment ..|> VideoStreamUI
ResultFragment ..|> ResultUI
DetectionOverlayView ..|> View
TextRecognizer ..|> ImageAnalysis.Analyzer

' Associations / "has-object-of-type"
MediaAlgo <|.. OpenCVAlgo
CameraController --> MediaAlgo : mediaAlgo
OpenCVAlgo --> MediaSource : mediaSource
CameraController --> MainUI : mainUI
CameraController --> ImageSource : imageSource
CameraController --> VideoSource : videoSource
CameraController --> MediaAlgo : mediaAlgo

OpenCVAlgo --> MediaSource : mediaSource

UploadImageFragment "1" --> "0..1" UploadListener : listener
UploadImageFragment "1" --> "0..1" FragmentUploadImageBinding : binding
UploadImageFragment "1" --> "0..1" Bitmap : selectedBitmap

ResultFragment "1" --> "0..1" ResultListener : listener
ResultFragment "1" --> "0..1" FragmentResultBinding : binding
ResultFragment "1" --> "0..1" TextToSpeech : tts

VideoStreamFragment "1" --> "0..1" VideoStreamFragment.Listener : listener
VideoStreamFragment "1" --> "0..1" FragmentVideoStreamBinding : binding
VideoStreamFragment "1" --> "0..*" DetectedObject : updateDetections
VideoStreamFragment "1" --> "0..1" PreviewView : getPreviewView()

DetectionOverlayView "1" --> "0..*" DetectedObject : detectedObjects
DetectionOverlayView "1" --> "1" Paint : boxPaint
DetectionOverlayView "1" --> "1" Paint : textPaint

LLMAlgo "1" --> "0..1" LLMListener : listener

MainUI --> UploadImageFragment : displays
MainUI --> VideoStreamFragment : displays
MainUI --> ResultFragment : displays

@enduml
