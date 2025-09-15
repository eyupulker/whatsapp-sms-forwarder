# Building APK Without Android Studio

This guide shows you how to build the WhatsApp SMS Forwarder APK using only command-line tools, without needing Android Studio.

## Prerequisites

### 1. Install Java JDK
- **Download**: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Version**: JDK 11 or higher (JDK 17 recommended)
- **Verify**: Run `java -version` in command prompt/terminal

### 2. Install Android SDK
You have several options:

#### Option A: Android Studio (SDK only)
1. Download Android Studio
2. Install only the SDK (skip IDE setup)
3. Set `ANDROID_HOME` environment variable

#### Option B: Command Line Tools Only
1. Download [Android Command Line Tools](https://developer.android.com/studio#command-tools)
2. Extract to a folder (e.g., `C:\Android\Sdk`)
3. Set `ANDROID_HOME` environment variable

#### Option C: Use existing Android Studio SDK
If you have Android Studio installed:
- Windows: `C:\Users\%USERNAME%\AppData\Local\Android\Sdk`
- macOS: `~/Library/Android/sdk`
- Linux: `~/Android/Sdk`

### 3. Set Environment Variables

#### Windows
```cmd
set ANDROID_HOME=C:\Users\YourUsername\AppData\Local\Android\Sdk
set PATH=%PATH%;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools
```

#### Linux/macOS
```bash
export ANDROID_HOME=/home/yourusername/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

## Building the APK

### Method 1: Using Build Scripts (Recommended)

#### Windows
```cmd
# Run the build script
build.bat
```

#### Linux/macOS
```bash
# Make script executable and run
chmod +x build.sh
./build.sh
```

### Method 2: Using Gradle Wrapper Directly

#### Windows
```cmd
# Clean and build
gradlew.bat clean
gradlew.bat assembleDebug
gradlew.bat assembleRelease
```

#### Linux/macOS
```bash
# Make gradlew executable
chmod +x gradlew

# Clean and build
./gradlew clean
./gradlew assembleDebug
./gradlew assembleRelease
```

## Setup local.properties

1. Copy `local.properties.template` to `local.properties`
2. Update the SDK path:
   ```properties
   sdk.dir=C:\Users\YourUsername\AppData\Local\Android\Sdk
   ```

## Output Files

After successful build, APK files will be located in:
- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Troubleshooting

### Common Issues

#### 1. "SDK location not found"
- **Solution**: Create `local.properties` file with correct SDK path
- **Check**: Verify `ANDROID_HOME` environment variable

#### 2. "Java version not supported"
- **Solution**: Install JDK 11 or higher
- **Check**: Run `java -version`

#### 3. "Android SDK not found"
- **Solution**: Install Android SDK and set `ANDROID_HOME`
- **Check**: Verify SDK tools are in PATH

#### 4. "Gradle wrapper not found"
- **Solution**: Ensure `gradlew` and `gradlew.bat` files exist
- **Check**: Run from project root directory

### Build Commands Reference

```bash
# Clean project
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build both debug and release
./gradlew assemble

# Run tests
./gradlew test

# Check dependencies
./gradlew dependencies
```

## Installing the APK

### Method 1: ADB (Android Debug Bridge)
```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release APK
adb install app/build/outputs/apk/release/app-release.apk
```

### Method 2: Direct Transfer
1. Copy APK to your Android device
2. Enable "Unknown Sources" in device settings
3. Tap the APK file to install

## Advanced Build Options

### Build Variants
```bash
# Build specific variant
./gradlew assembleDebug
./gradlew assembleRelease

# Build all variants
./gradlew assemble
```

### Signing Release APK
For production release, you'll need to sign the APK:

1. Generate keystore:
```bash
keytool -genkey -v -keystore my-release-key.keystore -alias alias_name -keyalg RSA -keysize 2048 -validity 10000
```

2. Update `app/build.gradle.kts` with signing config
3. Build signed release APK

## Performance Tips

- **Parallel Builds**: Add `org.gradle.parallel=true` to `gradle.properties`
- **Daemon**: Gradle daemon is enabled by default for faster builds
- **Memory**: Increase heap size if needed: `-Xmx2g`

## Next Steps

After building successfully:
1. Install the APK on your Android device
2. Follow the app setup instructions in the main README
3. Grant necessary permissions
4. Configure the target phone number
5. Test the forwarding functionality

## Support

If you encounter issues:
1. Check the troubleshooting section above
2. Verify all prerequisites are installed
3. Check the build output for specific error messages
4. Ensure you're running commands from the project root directory
