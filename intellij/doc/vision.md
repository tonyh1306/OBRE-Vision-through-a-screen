
# OBRE System - Vision document

## 1. Introduction

We envision an Object Recognition Application that provides real-time notification from the video feed with 
a user-friendly and out-of-the-box experience.

## 2. Business case

Our software addresses the visually-impaired community's needs:
1. It can provide immediate access without login requirements, creating a smooth
and immediate user experience.
2. It integrates text-to-speech capabilities that allow users to become
aware of their surroundings just through pointing their camera at their
environment.

## 3. Key functionality
- Video feed capture with audible notification using Google Cloud's Text-to-speech API.
- Text recognition with Text-to-speech capability.
- Image upload for recognition through OpenAI's API.


## 4. Stakeholder goals summary
- **Users**: recognize objects, reads out loud the object. 

## Use case diagram

```plantuml
@startuml
skin rose

' human actors
actor "End user" as user

' list all use cases in package
package OBRE {
    usecase "Recognize real-time object" as obre
    usecase "Notify user" as tts
    usecase "Process image" as process
    usecase "Acquire image description" as describe 
    usecase "Real-time recording" as record
    usecase "Real-time text recognition" as videotext
}

' list relationships between actors and use cases
user --> obre
tts --|> obre : \t<<extends>>\t
record <-- obre : <<includes>>
videotext --|> obre : <<extends>>

describe <-- user
describe <|-- process : <<extends>>
tts --|> describe : <<extends>>
@enduml
```