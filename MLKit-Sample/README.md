# HUAWEI-HMS-MLKit-Sample


## build status
![Android CI](https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/workflows/Android%20CI/badge.svg)


## Table of Contents

 * [Introduction](#introduction)
 * [Installation](#installation)
 * [Supported Environments](#supported-environments)
 * [License](#license)


## Introduction
The sample code mainly shows the use of Huawei Machine Learning SDK.

The project is divided into two parts, one is the text demo: module-text, including: text recognition,
document recognition, bank card recognition, ID card recognition, general card recognition, text translation,language detection.
The other is the visual demo: module-vision, including: image classification,
object detection and tracking, landmark recognition, image segmentation, face detection.

[Read more about Huawei Machine Learning SDK.](https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-introduction-4)

The following are screenshots of the apk running interface.
<table><tr>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/mainText.jpg" width=180 title="main page" border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/language.jpg" width=180 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/text.jpg" width=180 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/bcr.jpg" width=180 border=2></td>
</tr></table>

<table><tr>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/mainVision.jpg" width=180 title="main page" border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/imageSegmentVideo.gif" width=180 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/face.jpg" width=180 border=2></td>
<td><img src="https://github.com/HMS-MLKit/HUAWEI-HMS-MLKit-Sample/blob/master/MLKit-Sample/resources/object.jpg" width=180 border=2></td>
</tr></table>

If you want to use cloud analyzer, such as cloud text analyzer(document and cloud text), cloud image classification analyzer, landmark analyzer, translation, language detection,
you need to apply for an agconnect-services.json file in the developer alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc), replacing the sample-agconnect-services.json in the project.

Attention: The package name in this project can not be used to apply for agconnect-services.json. You can use a custom package name to apply for agconnect-services.json.

You only need to modify the applicationId in app HUAWEI-HMS-MLKit-Sample\app\build.gradle to the same package name
as the applied agconnect-services.json to experience the mlkit on-Cloud services.


## Installation
Download the HUAWEI-HMS-MLKit-Sample code and open in Android Studio, after ensuring that your device is connected to the network, obtain the apk through build.


## Supported Environments
android 4.4 or a later version is recommended.


##  License
HUAWEI-HMS-MLKit-Sample/MLKit-Sample is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
