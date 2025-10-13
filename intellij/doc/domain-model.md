```plantuml
@startuml
hide circle
skin rose
hide empty methods

class Image{
String imagePath
Mat image
--
resize()
}

class Video {
String videoPath
}

class ImageObject{
String name
frame
String category
}

class OpenAI{
Image img
--
String showsDescription()
}

interface ObjRecAlgo{
void showMedia()

}

class OpenCVAlgo{
+ {static} COCO_CLASSES : List<String>
Mat image
Mat video 
--
void showMedia()
}

class OtherAlgo{


}

' class TesseractAlgo{}

' interface TextRecAlgo {}

ObjRecAlgo<|..OpenCVAlgo
ObjRecAlgo<|.right.OtherAlgo
Image -left- OpenCVAlgo: input >
Video - OpenCVAlgo: input > 
OpenCVAlgo -down- ImageObject: outputs >
Image -down- OpenAI : input >

' TesseractAlgo ..|> TextRecAlgo
@enduml