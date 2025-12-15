# Speak Result Text

## 1. Primary actor and goals

__User__: Receives automatic audio announcement of detection results for enhanced accessibility.

## 2. Other stakeholders and their goals

* __Visually-impaired user__: Relies on audio feedback to understand detection results without visual interface.
* __Application__: Automatically provides audio output on result screen to ensure accessibility compliance.

## 3. Preconditions

What must be true prior to the start of the use case.

* Detection analysis has completed (object detection or text recognition).
* ResultFragment is displayed with result text.
* Device has TextToSpeech engine available (standard Android feature).
* System audio output is not muted (controlled by device settings).

## 4. Post-conditions

What must be true upon successful completion of the use case.

* Result text has been spoken aloud automatically.
* User hears the result announcement in English (Locale.US).
* TextToSpeech resources are properly released when fragment is destroyed.

## 5. Workflow

```plantuml
@startuml
skin rose
title Speak Result Text (Automatic)

|#implementation|System|

|System|
start
:ResultFragment created with result text;
:onViewCreated() called;
:Request audio focus from AudioManager;
:Display result text on screen;
:Initialize TextToSpeech engine;

if (TTS initialization successful?) is (yes) then
  :Set language to US English;
  
  if (Language supported?) is (yes) then
    :Prepare result text for speech;
    :Call tts.speak(text, QUEUE_FLUSH);
    :Audio output plays on device;
    :User hears result announcement;
  else (not supported)
    :Log error and continue;
    :Display text only;
  endif
else (initialization failed)
  :Log error;
  :Display text only (silent mode);
endif

:Fragment ready for user interaction;
:User can navigate back;

:Fragment destroyed;
:Call tts.stop() and tts.shutdown();
:Release TextToSpeech resources;
stop
@enduml
```

## 6. Sequence Diagram

```plantuml
@startuml
skin rose
hide footbox

actor User
participant "MainActivity" as main
participant "ResultFragment" as result
participant "AudioManager" as audio
participant "TextToSpeech" as tts
participant "Android OS" as os

main -> result: newInstance(resultText)
result -> result: onCreate()
note over result: Store resultText from Bundle

main -> result: onViewCreated(view, savedInstanceState)

result -> audio: getSystemService(AUDIO_SERVICE)
result -> audio: requestAudioFocus()
audio --> result: focus granted

result -> result: Display result text in TextView

result -> result: new TextToSpeech(context, onInitListener)
tts -> os: Initialize TTS engine
os --> tts: Engine ready
tts --> result: onInit(SUCCESS)

result -> tts: setLanguage(Locale.US)
tts --> result: LANG_AVAILABLE

result -> result: Prepare speech text
result -> tts: speak(text, QUEUE_FLUSH, null, "utteranceId")
tts -> os: Queue audio output
os -> os: Play audio
os --> User: Audio output

User -> result: Click back button
result -> result: onDestroy()

result -> tts: stop()
result -> tts: shutdown()
tts -> os: Release audio resources

@enduml
```

## 7. Special Requirements

- **Accessibility**: Automatic speech output without user intervention ensures visually-impaired users receive results
- **Performance**: TTS initialization happens in background via callback; does not block UI thread
- **Resource Management**: TextToSpeech must be properly shut down in onDestroy() to prevent memory leaks
- **Language**: Currently hardcoded to Locale.US English (no multi-language support)
- **Audio Focus**: Requests audio focus to manage system audio policy (music pause, etc.)
- **Error Resilience**: Handles TTS engine unavailability gracefully by falling back to text-only display
- **Device Compatibility**: Relies on Android OS standard TextToSpeech (available on all devices)
