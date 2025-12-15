# This Iteration (#4)
## For the final iteration we added
- TTS integration: We added Android's Text-to-Speech API to provide audio descriptions for detected objects.
- Added a history feature with the results of previous image analyses
- We also added OCR which can detect text from anything in the video stream and displays it at the top of the page

## Testing
- We added testing for TTS. 
- We also have testing for a default bottle.jpg image to test the Gemini call and image analysis

## Cutoff

- We were unable to get to Multimodal Feedback System (haptic output), although we were able to have audio output
- 

# Previous Iteration (#3)
## Risk and priority

1.) Functionality of video stream

2.) Functionality of uploading image and AI description feedback with testing specifically for only upload image.

## Cutoff

3.) Multimodal Feedback System (audio + haptic output)

4.) Recognize Text

## Clarification

Real-time object recognition in live recording is our highest priority use case as it is the main function
of the app and decides its usability. This iteration we spent a lot of time trying to get this and the Gemini API to work.
Then, we have text-recognition that we want to focus on last as it is not the focus of our app. text-recognition is also not a pervasive
element. We may work on this on after we get everything working. We want to improve the UI after our functionality improves.

## Next Iteration: Android Platform

1. **Add testing for Video Feed Streaming**: Implement unit and Android to comprehensively tests Video Stream use case.

2. **Add TTS integration**: Implement Android's Text-to-Speech API to provide audio feedback for detected objects and recognized text.

3. **Accessibility enhancements**: Ensure the app meets accessibility standards for visually-impaired users, including screen reader compatibility and high-contrast UI elements.
