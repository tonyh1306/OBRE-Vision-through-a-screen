```plantuml
@startuml
hide circle
skin rose
hide empty methods
class Image{}

class Video{}

class Imageobject{
name
bounding-box
category
}

interface ObjRecAlgo{
}

class OpenCVAlgo{
}

class otherAlgo{
}

' associations
ObjRecAlgo <|.. OpenCVAlgo 


@enduml