# Smile-Camera

## Table of Contents

 * [Introduction](#introduction)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample-Code](#Sample-Code)
 * [License](#license)


## Introduction
The sample code mainly shows the use of Huawei Machine Learning SDK.

It uses face detection function of Huawei Machine Learning SDK. It uses real-time detection of face to confirm if user is smiling, and then captures a smile photo of the user.


## Configuration
To use functions provided by packages in examples, You need to add the dependencies to the build.gradle file as follows:

    implementation 'com.huawei.hms:ml-computer-vision-face-recognition-model:1.0.3.300'
    implementation 'com.huawei.hms:ml-computer-vision-face:1.0.3.300'

## Supported Environments
android 4.4 or a later version is recommended.


## Sample Code
Sample code majors activity as follows:
   1. Choose LiveFaceAnalyseActivity to see a demo of the following:
      - Face detection (Detects and tracks faces)
      - Camera front and back flip


   Ability called by the sample:
   1. Face Recognition:
      - MLAnalyzerFactory.getInstance().GetFaceAnalyzer (MLFaceAnalyzerSetting): Create a face recognizer. This is the most core class of face recognition.
      - MLFaceAnalyzer.setTransactor(): Set the face recognition result processor for subsequent processing of the recognition result.
      - MLFaceAnalyzerSetting.Factory().SetFeatureType (MLFaceAnalyzerSetting.TYPE_FEATURES): Turn on facial expression and feature detection, including smile, eyes open, beard and age.
      - MLFaceAnalyzerSetting.Factory().AllowTracing (): Whether to start face tracking mode.

##  License
HUAWEI-HMS-MLKit-Sample/Smile-Camera is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
