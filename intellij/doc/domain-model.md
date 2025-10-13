```plantuml
@startuml
hide circle
skin rose
hide empty methods
class Image{
Mat
Net

}

class Video{}

class ImageObject{
name
frame
category
}

interface ObjRecAlgo{
}

class OpenCVAlgo{
+ {static} COCO_CLASSES : List<String>
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

' TesseractAlgo ..|> TextRecAlgo
@enduml