#!/bin/bash
# Build script for Linux/macOS
# This script builds the Android APK using Gradle wrapper

echo "Building WhatsApp SMS Forwarder APK..."
echo

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo "Please install Java JDK 11 or higher and add it to your PATH"
    exit 1
fi

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ]; then
    echo "WARNING: ANDROID_HOME is not set"
    echo "Please set ANDROID_HOME to your Android SDK directory"
    echo "Example: export ANDROID_HOME=\$HOME/Android/Sdk"
    echo
fi

# Make gradlew executable
chmod +x gradlew

# Clean previous build
echo "Cleaning previous build..."
./gradlew clean
if [ $? -ne 0 ]; then
    echo "ERROR: Clean failed"
    exit 1
fi

# Build debug APK
echo "Building debug APK..."
./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "ERROR: Build failed"
    exit 1
fi

# Build release APK
echo "Building release APK..."
./gradlew assembleRelease
if [ $? -ne 0 ]; then
    echo "ERROR: Release build failed"
    exit 1
fi

echo
echo "Build completed successfully!"
echo
echo "APK files are located in:"
echo "  Debug:   app/build/outputs/apk/debug/app-debug.apk"
echo "  Release: app/build/outputs/apk/release/app-release.apk"
echo
