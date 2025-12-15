# Recognize Objects in Video

## 1. Primary actor and goals

__User__: Wants to recognize what objects are in the video stream from the camera and receive real-time audio descriptions.

## 2. Preconditions

What must be true prior to the start of the use case.

* We are not going to have a log-in system for the purpose of easy-use and quick-access of the app.
* The camera is working and is granted permission.
* There is enough lighting and the objects are visible and not obscured.

## 3. Post-conditions

What must be true upon successful completion of the use case.

* Object is recognized.
* The object is described in text.
* The app is open and running.
* There is a text-to-speech function that reads out the description.

## 4. Workflow

for _analyze-video_:

```plantuml
@startuml
skin rose
title Analyze Video (Fully-dressed)
|#implementation|User|
|#technology|System|

|User|
:Open the app;

|System|
:Open the camera and start video stream;
if (Recognition type) is (text) then
:Execute __Recognize text__;
else (default) 
:Automatically start detecting an object;
endif 
:Frame and label object;
stop
@enduml
```

## 5. Sequence Diagram
```plantuml
@startuml
skin rose
actor User

participant "VideoStreamFragment" as videoFragment
participant "CameraController" as controller
participant ": MediaAlgo" as mediaAlgo
participant "TextRecognizer" as textRecognizer
participant "DetectionOverlayView" as overlayView
participant "TextView" as textView

User -> videoFragment : open camera / start stream
videoFragment -> controller : onStartStream(videoFragment)

loop for each frame
    videoFragment -> mediaAlgo : runOnFrame(frame)
    mediaAlgo --> videoFragment : List<DetectedObject>

    videoFragment -> overlayView : setDetectedObjects(detectedObjects)
    overlayView --> videoFragment : redraw overlay

    videoFragment -> textRecognizer : analyze(frame)
    textRecognizer --> videoFragment : detectedText
    videoFragment -> textView : setText(detectedText)
end

User -> videoFragment : stop stream/turn off the app
videoFragment -> controller : onStopStream(videoFragment)

@enduml

```
