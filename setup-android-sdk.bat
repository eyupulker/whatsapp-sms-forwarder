@echo off
echo Setting up minimal Android SDK for local building...
echo.

REM Create Android SDK directory
if not exist "C:\Android\Sdk" mkdir "C:\Android\Sdk"

REM Download Android Command Line Tools
echo Downloading Android Command Line Tools...
powershell -Command "Invoke-WebRequest -Uri 'https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip' -OutFile 'commandlinetools.zip'"

REM Extract to SDK directory
echo Extracting tools...
powershell -Command "Expand-Archive -Path 'commandlinetools.zip' -DestinationPath 'C:\Android\Sdk' -Force"

REM Move to correct location
if exist "C:\Android\Sdk\cmdline-tools" (
    if not exist "C:\Android\Sdk\cmdline-tools\latest" (
        mkdir "C:\Android\Sdk\cmdline-tools\latest"
        xcopy "C:\Android\Sdk\cmdline-tools\*" "C:\Android\Sdk\cmdline-tools\latest\" /E /I /Y
    )
)

REM Set environment variables
setx ANDROID_HOME "C:\Android\Sdk"
setx PATH "%PATH%;C:\Android\Sdk\platform-tools;C:\Android\Sdk\cmdline-tools\latest\bin"

echo.
echo Android SDK setup complete!
echo Please restart your command prompt and run: setup-sdk.bat
echo.
pause
