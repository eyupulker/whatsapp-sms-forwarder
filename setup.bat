@echo off
REM Setup script for Windows
REM This script helps set up the build environment

echo WhatsApp SMS Forwarder - Setup Script
echo ====================================
echo.

REM Check if Java is installed
echo Checking Java installation...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java JDK 11 or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo or
    echo https://openjdk.org/
    echo.
    pause
    exit /b 1
) else (
    echo Java is installed ✓
)

REM Check if Android SDK is available
echo.
echo Checking Android SDK...
if not exist "%ANDROID_HOME%" (
    echo WARNING: ANDROID_HOME is not set
    echo.
    echo Please set ANDROID_HOME environment variable to your Android SDK directory
    echo.
    echo Common locations:
    echo   %USERPROFILE%\AppData\Local\Android\Sdk
    echo   C:\Android\Sdk
    echo.
    echo To set it permanently:
    echo   1. Open System Properties ^> Advanced ^> Environment Variables
    echo   2. Add new system variable: ANDROID_HOME
    echo   3. Set value to your SDK path
    echo   4. Add %ANDROID_HOME%\tools and %ANDROID_HOME%\platform-tools to PATH
    echo.
) else (
    echo Android SDK found at: %ANDROID_HOME% ✓
)

REM Create local.properties if it doesn't exist
echo.
echo Setting up local.properties...
if not exist "local.properties" (
    if exist "local.properties.template" (
        copy "local.properties.template" "local.properties" >nul
        echo Created local.properties from template
        echo Please edit local.properties and set the correct SDK path
    ) else (
        echo Creating local.properties...
        echo sdk.dir=YOUR_ANDROID_SDK_PATH_HERE > local.properties
        echo Created local.properties - please edit it with your SDK path
    )
) else (
    echo local.properties already exists ✓
)

echo.
echo Setup complete!
echo.
echo Next steps:
echo 1. Edit local.properties and set the correct SDK path
echo 2. Run build.bat to build the APK
echo.
pause
