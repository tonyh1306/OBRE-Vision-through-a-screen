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

interface Obj-RecAlgo{
}

class OpenCV-Algo{
}
class otherAlgo{
}

class text-processing{
}
Obj-RecAlgo<|..OpenCV-Algo

@enduml