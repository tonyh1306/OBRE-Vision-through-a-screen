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
boundingbox
category
}

interface ObjRecAlgo{
}

class OpenCVAlgo{
}

class OtherAlgo{
}

class TextProcessing{
}
ObjRecAlgo<|..OpenCVAlgo
ObjRecAlgo<|..OtherAlgo
Image - ObjectAlgo: input >
Video - ObjectAlgo: input > 
ObjectAlgo - ImageObject: outputs >
@enduml