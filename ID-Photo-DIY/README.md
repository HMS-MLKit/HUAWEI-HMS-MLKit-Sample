# ID-Photo-DIY

## Table of Contents

 * [Introduction](#introduction)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample-Code](#Sample-Code)
 * [License](#license)


## Introduction
The sample code mainly shows the use of Huawei Machine Learning SDK.

It uses image segmentation function of Huawei Machine Learning SDK. It uses still image of people to composite an ID photo with blue background or white background.


## Configuration
To use functions provided by packages in examples, You need to add the dependencies to the build.gradle file as follows:

    implementation 'com.huawei.hms:ml-computer-vision-image-segmentation-body-model:1.0.3.300'
    implementation 'com.huawei.hms:ml-computer-vision-segmentation:1.0.3.300'

## Supported Environments
android 4.4 or a later version is recommended.


## Sample Code
Sample code majors activitys as follows:
   1. Choose MainActivity to see a demo of the following:
      - Chooses ID photo background

   2. Choose StillCutPhotoActivity to see a demo of the following:
	  - Segments the pixels representing human body from an image


   Ability called by the sample:
   1. Image Segmentation
      - MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(settings)：Create a image segment analyzer.
      - MLImageSegmentationSetting.Factory.setExact()：Set detection mode, true is fine detection mode, false is speed priority detection mode.
      - MLImageSegmentationAnalyzer.asyncAnalyseFrame(frame): Parse out all target contained in the picture.

##  License
HUAWEI-HMS-MLKit-Sample/ID-Photo_DIY is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
