```plantuml
@startuml
hide circle
skin rose
hide empty methods
class Image{
}

class Video{}

class ImageObject{
name
category
}

interface ObjRecAlgo{
}

class OpenCVAlgo{
}

class OtherAlgo{
}

class TesseractAlgo{
}

interface TextRecAlgo {
}

ObjRecAlgo<|..OpenCVAlgo
ObjRecAlgo<|.right.OtherAlgo
Image -left- OpenCVAlgo: input >
Video - OpenCVAlgo: input > 
OpenCVAlgo -down- ImageObject: outputs >

TesseractAlgo ..|> TextRecAlgo
@enduml