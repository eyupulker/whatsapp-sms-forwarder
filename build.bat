@echo off
REM Build script for Windows
REM This script builds the Android APK using Gradle wrapper

echo Building WhatsApp SMS Forwarder APK...
echo.

REM Check if Java is available
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 11 or higher and add it to your PATH
    pause
    exit /b 1
)

REM Check if Android SDK is available
if not exist "%ANDROID_HOME%" (
    echo WARNING: ANDROID_HOME is not set
    echo Please set ANDROID_HOME to your Android SDK directory
    echo Example: set ANDROID_HOME=C:\Users\%USERNAME%\AppData\Local\Android\Sdk
    echo.
)

REM Clean previous build
echo Cleaning previous build...
call gradlew.bat clean
if %ERRORLEVEL% neq 0 (
    echo ERROR: Clean failed
    pause
    exit /b 1
)

REM Build debug APK
echo Building debug APK...
call gradlew.bat assembleDebug
if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

REM Build release APK
echo Building release APK...
call gradlew.bat assembleRelease
if %ERRORLEVEL% neq 0 (
    echo ERROR: Release build failed
    pause
    exit /b 1
)

echo.
echo Build completed successfully!
echo.
echo APK files are located in:
echo   Debug:   app\build\outputs\apk\debug\app-debug.apk
echo   Release: app\build\outputs\apk\release\app-release.apk
echo.
pause
