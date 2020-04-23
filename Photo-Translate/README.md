# Photo-Translate

## Table of Contents

 * [Introduction](#introduction)
 * [Configuration ](#configuration )
 * [Supported Environments](#supported-environments)
 * [Sample-Code](#Sample-Code)
 * [License](#license)


## Introduction
The sample code mainly shows the use of Huawei Machine Learning SDK.

It uses text recognition and translation of Huawei Machine Learning SDK. It uses still image of text to translate words in the photo into the language you want. Currently, the following languages are supported: Simplified Chinese (zh), English (en), French (fr), Arabic (ar), Thai (th), Spanish (es), and Turkish (tr).

You need to use cloud analyzer, such as cloud text analyzer and translator.
So you need to apply for an agconnect-services.json file in the developer alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc), replacing the sample-agconnect-services.json in the project.

Attention: The package name in this project can not be used to apply for agconnect-services.json. You can use a custom package name to apply for agconnect-services.json.

You only need to modify the applicationId in app Photo-Translate
\app\build.gradle to the same package name as the applied agconnect-services.json to experience the mlkit on-Cloud services.


## Configuration
To use functions provided by packages in examples, You need to add the dependencies to the build.gradle file as follows:

    implementation 'com.huawei.hms:ml-computer-vision-cloud:1.0.3.300'
    implementation 'com.huawei.hms:ml-computer-translate:1.0.3.300'

## Supported Environments
android 4.4 or a later version is recommended.


## Sample Code
Sample code majors activitys as follows:
   1. Choose MainActivity to see a demo of the following:
      - Chooses the source language and target language.

   2. Choose RemoteTranslateActivity to see a demo of the following:
	  - Selects photo from album or take photo to do the translation


   Ability called by the sample:
   1. Text Recognition
	  - MLAnalyzerFactory.getInstance().getRemoteTextAnalyzer()：Create a cloud text recognizer.
	  - MLTextAnalyzer.asyncAnalyseFrame(frame): Parse text information in pictures.
	  - MLText.getBlocks(): Get text blocks. Generally, a text block represents one line. There is also a case where a text block corresponds to multiple lines.
      - MLText.Block.getContents(): Get list of text lines(MLText.TextLine).
   2. Translation
	  - MLTranslatorFactory.getInstance().getRemoteTranslator(settings)：Create a translator.
	  - MLRemoteTranslator.asyncTranslate(sourceText): Parse out text from source language to target language, sourceText indicates the language to be detected.


##  License
HUAWEI-HMS-MLKit-Sample/Photo-Translate is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
