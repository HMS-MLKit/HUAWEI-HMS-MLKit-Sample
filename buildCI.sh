#!/bin/bash

echo "Begine build."
# Exit if error
set -e

# Move agconnect-services.json in apps.
cp MLKit-Sample/app/sample-agconnect-services.json MLKit-Sample/app/agconnect-services.json
cp MLKit-Sample/app/sample-agconnect-services.json ID-Photo-DIY/app/agconnect-services.json
cp MLKit-Sample/app/sample-agconnect-services.json Smile-Camera/app/agconnect-services.json
echo "Copy agc file end."

# Build
./gradlew build
