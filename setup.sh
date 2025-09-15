#!/bin/bash
# Setup script for Linux/macOS
# This script helps set up the build environment

echo "WhatsApp SMS Forwarder - Setup Script"
echo "===================================="
echo

# Check if Java is installed
echo "Checking Java installation..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo
    echo "Please install Java JDK 11 or higher from:"
    echo "https://www.oracle.com/java/technologies/downloads/"
    echo "or"
    echo "https://openjdk.org/"
    echo
    exit 1
else
    echo "Java is installed ✓"
fi

# Check if Android SDK is available
echo
echo "Checking Android SDK..."
if [ -z "$ANDROID_HOME" ]; then
    echo "WARNING: ANDROID_HOME is not set"
    echo
    echo "Please set ANDROID_HOME environment variable to your Android SDK directory"
    echo
    echo "Common locations:"
    echo "  ~/Android/Sdk"
    echo "  ~/Library/Android/sdk (macOS)"
    echo "  /opt/android-sdk"
    echo
    echo "To set it permanently, add to your ~/.bashrc or ~/.zshrc:"
    echo "  export ANDROID_HOME=\$HOME/Android/Sdk"
    echo "  export PATH=\$PATH:\$ANDROID_HOME/tools:\$ANDROID_HOME/platform-tools"
    echo
else
    echo "Android SDK found at: $ANDROID_HOME ✓"
fi

# Create local.properties if it doesn't exist
echo
echo "Setting up local.properties..."
if [ ! -f "local.properties" ]; then
    if [ -f "local.properties.template" ]; then
        cp "local.properties.template" "local.properties"
        echo "Created local.properties from template"
        echo "Please edit local.properties and set the correct SDK path"
    else
        echo "Creating local.properties..."
        echo "sdk.dir=YOUR_ANDROID_SDK_PATH_HERE" > local.properties
        echo "Created local.properties - please edit it with your SDK path"
    fi
else
    echo "local.properties already exists ✓"
fi

# Make gradlew executable
echo
echo "Making gradlew executable..."
chmod +x gradlew
echo "gradlew is now executable ✓"

echo
echo "Setup complete!"
echo
echo "Next steps:"
echo "1. Edit local.properties and set the correct SDK path"
echo "2. Run ./build.sh to build the APK"
echo
