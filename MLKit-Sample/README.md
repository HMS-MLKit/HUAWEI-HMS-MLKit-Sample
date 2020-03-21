# HUAWEI-HMS-MLKit-Sample


## build status
![Build Status](https://travis-ci.org/HMS-MLKit/ai_mlkit_sample.svg?branch=master)


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample-Code](#Sample-Code)
 * [License](#license)


## Introduction
The sample code mainly shows the use of Huawei Machine Learning SDK.

Including face recognition, text recognition, image classification, landmark recognition, object detection and tracking, translation, language detection, image segmentation. Product visual search will coming soon.

It includes both camera capture video for real-time detection and still image recognition.

The following are screenshots of the apk running interface. The first is the main page, the second is the segmentation video stream segmentation interface, and the third is the portrait segmentation function demo.
<table><tr>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/main.jpg" width=200 title="main page" border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/imageSegmentMain.jpg" width=200 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/imageSegmentVideo.gif" width=200 border=2></td>
</tr></table>

The first is the interface for translation and language detection, the second is a demonstration of object detection and tracking, and the third is a text recognition demonstration.

<table><tr>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/language.jpg" width=200 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/object.jpg" width=200 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/resources/text.jpg" width=200 border=2></td>
</tr></table>

If you want to use cloud analyzer, such as cloud text analyzer(document and cloud text), cloud image classification analyzer, landmark analyzer, translation, language detection,
you need to apply for an agconnect-services.json file in the developer alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-preparations4), replacing the sample-agconnect-services.json in the project.

Attention: The package name in this project can not be used to apply for agconnect-services.json. You can use a custom package name to apply for agconnect-services.json.

You only need to modify the applicationId in app HUAWEI-HMS-MLKit-Sample
\app\build.gradle to the same package name as the applied agconnect-services.json to experience the mlkit on-Cloud services.


## Installation
You can find the app-release.apk in code->releases->app-release.apk, you can download the APK, copy it to your phone, install and experience it. if you have installed adb on your computer, open the cmd window and install the application on your phone through adb install app-release. apk.

Download the HUAWEI-HMS-MLKit-Sample code and open in Android Studio, after ensuring that your device is connected to the network, obtain the apk through build.


## Configuration
To use functions provided by packages in examples, You need to add the dependencies to the build.gradle file as follows:

    implementation 'com.huawei.hms:ml-computer-vision-ocr-cn-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-ocr-jk-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-ocr-latin-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-image-classification-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-object-detection-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-face-recognition-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-card-icr-cn-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision-image-segmentation-model:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-vision:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-translate:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-card-icr-cn-plugin:1.0.2.300'
    implementation 'com.huawei.hms:ml-computer-language-detection:1.0.2.300'

## Supported Environments
android 4.4 or a later version is recommended.


## Sample Code
Sample code majors activitys as follows:
   1. Choose TakePhotoActivity to see a demo of the following:
      - Camera background replacement
      - Picture view
      - Camera front and back flip
   2. Choose LoadPhotoActivity to see a demo of the following:
	  - Image Segmentation(Segments the pixels representing specific elements (such as human body, plant, and sky) from an image)
   3. Choose IDCardRecognitionActivity to see a demo of the following:
      - Product Visual Search(Recognizes product information such as the product category in an image)
   4. Choose IDIdentificationActivity to see a demo of the following:
      - Chinese IDCard Recognition(Recognizes text in images of second-generation ID cards of Chinese mainland residents)
   5. Choose ObjectDetectionActivity to see a demo of the following:
      - Object detection (Detects and tracks an object in images or video streams)
   6. Choose ImageClassificationActivity to see a demo of the following:
      - Image Classification(Classifies entities in images, such as people, objects, environments, activities, or artwork to define image themes and application scenarios)
   7. Choose FaceDetectionActivity to see a demo of the following:
      - Face detection (Detects and tracks faces)
      - Camera front and back flip
   8. Choose TranslatorActivity to see a demo of the following:
      - Language translation function
      - Automatic language monitoring for input text
      - Multi language support
   9. Choose TextRecognitionActivity to see a demo of the following:
      - Dynamic and real-time text recognition
      - Take photos after identifying words
      - Generated photos can be zoomed
      - It can load photos in the local album and parse the text in the photos (Cloud)
   10. Choose CloudDetectionActivity to see a demo of the following:
      - Document text recognition (Cloud)
      - Landmark recognition (Cloud)

   Ability called by the sample:
   1. Face Recognition:
      - MLAnalyzerFactory.getInstance().GetFaceAnalyzer (MLFaceAnalyzerSetting): Create a face recognizer. This is the most core class of face recognition.
      - MLFaceAnalyzer.setTransactor(): Set the face recognition result processor for subsequent processing of the recognition result.
      - MLFaceAnalyzerSetting.Factory().SetFeatureType (MLFaceAnalyzerSetting.TYPE_FEATURES): Turn on facial expression and feature detection, including smile, eyes open, beard and age.
      - MLFaceAnalyzerSetting.Factory().AllowTracing (): Whether to start face tracking mode.
      - LensEngine: camera that generates continuous image data for detection.
   2. Text Recognition
	  - MLAnalyzerFactory.getInstance().getLocalTextAnalyzer()：Create a device text recognizer.
	  - MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer()：Create a cloud text recognizer.
	  - MLAnalyzerFactory.getInstance().getRemoteDocumentAnalyzer()：Create a cloud document recognizer.
	  - MLTextAnalyzer.asyncAnalyseFrame(frame): Parse text information in pictures.
	  - MLDocumentAnalyzer.asyncAnalyseFrame(frame): Parse document information in pictures.
	  - MLText.getBlocks(): Get text blocks. Generally, a text block represents one line. There is also a case where a text block corresponds to multiple lines.
      - MLText.Block.getContents(): Get list of text lines(MLText.TextLine).
	  - MLText.TextLine.getContents(): Get the text content of each line(MLText.Word, The device text analyzer returns contains spaced, the cloud text analyzer does not).
	  - MLText.Word.getStringValue(): Gets the word of each line.
	  - MLDocument.getBlocks(): Get document blocks. Generally, a document block represents multiple paragraphs(MLDocument.Block).
      - MLDocument.getSections(): Get list of document paragraphs(MLDocument.Section).
      - MLDocument.getLineList(): Get list of document lines(MLDocument.Line).
      - MLDocument.getWordList(): Get list of document words(MLDocument.Word).
   3. Image Classification
	  - MLAnalyzerFactory.getInstance().getLocalImageClassificationAnalyzer(setting)：Create a device image classifier.
	  - MLAnalyzerFactory.getInstance().getRemoteImageClassificationAnalyzer()：Create a cloud image classifier.
	  - MLImageClassificationAnalyzer.asyncAnalyseFrame(frame): Classify images and generate a MLImageClassification collection, which indicates the category to which the image belongs.
	  - MLImageClassification.getName()：Get the name of the image category, such as pen, phone, computer, etc.
   4. Object Detection And Tracking
	  - MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting)：Creating an object analyzer.
	  - MLObjectAnalyzerSetting.Factory.setAnalyzerType(MLObjectAnalyzerSetting.TYPE_VIDEO): Set the recognition mode.
	  - MLOject.getTypePossibility: Get the category name of the object.
	  - MLOject.getTypeIdentity()：Get the ID number of the object.
	  - LensEngine：camera that generates continuous image data for detection.
   5. Landmark Detection
	  - MLAnalyzerFactory.getInstance().getRemoteLandmarkAnalyzer(settings)：Create a landmark analyzer.
	  - MLRemoteLandmarkAnalyzerSetting.Factory.setLargestNumOfReturns()：Set the maximum number of detection results.
	  - MLRemoteLandmarkAnalyzerSetting.Factory.setPatternType()：Set detection mode.
	  - MLRemoteLandmarkAnalyzer.asyncAnalyseFrame(frame): Parse out all landmark information contained in the picture.
   6. Translation
	  - MLTranslatorFactory.getInstance().getRemoteTranslator(settings)：Create a translator.
	  - MLRemoteTranslateSetting.Factory.setSourceLangId()：Set source language ID.
	  - MLRemoteTranslateSetting.Factory.setTargetLangId()：Set target language ID.
	  - MLRemoteTranslator.asyncTranslate(sourceText): Parse out text from source language to target language, sourceText indicates the language to be detected.
   7. Language Detection
      - MLLangDetectorFactory.getInstance().getRemoteLangDetector(settings)：Create a language detector.
	  - MLRemoteLangDetectorSetting.Factory.setTrustedThreshold()：Set the minimum confidence threshold for language detection.
	  - MLRemoteLangDetector.firstBestDetect(sourceText):
	  - MLRemoteLangDetector.probabilityDetect(sourceText): Returns the language code with the highest confidence, sourceText represents the language to be detected.
   8. Image Segmentation
      - MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(settings)：Create a image segment analyzer.
      - MLImageSegmentationSetting.Factory.setExact()：Set detection mode, true is fine detection mode, false is speed priority detection mode.
      - MLImageSegmentationAnalyzer.asyncAnalyseFrame(frame): Parse out all target contained in the picture.
      - LensEngine：camera that generates continuous image data for detection.

##  License
HUAWEI-HMS-MLKit-Sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
