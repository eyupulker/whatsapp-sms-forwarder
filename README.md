# WhatsApp SMS Forwarder

An Android Kotlin application that forwards WhatsApp notifications as SMS to another phone number.

## Features

- **Real-time Forwarding**: Automatically forwards WhatsApp messages as SMS
- **Customizable Format**: Configure message format with sender name, timestamp, and custom prefix
- **Privacy Focused**: No data storage, messages are forwarded directly
- **Easy Setup**: Simple configuration through the app interface
- **Permission Management**: Clear permission requests and status indicators

## Requirements

- Android 7.0 (API level 24) or higher
- WhatsApp installed on the device
- SMS permissions
- Notification access permission

## Setup Instructions

### 1. Install the App

1. Build the project using Android Studio
2. Install the APK on your Android device
3. Open the app

### 2. Grant Permissions

The app requires two main permissions:

#### SMS Permission
1. When prompted, tap "Grant SMS Permission"
2. Allow all SMS-related permissions (Send SMS, Receive SMS, Read SMS)

#### Notification Access Permission
1. Tap "Grant Notification Access"
2. Find "WhatsApp SMS Forwarder" in the list
3. Toggle the switch to enable notification access
4. Confirm the permission request

### 3. Configure Settings

1. Tap "Open Settings" in the main screen
2. Enter the target phone number (include country code, e.g., +1234567890)
3. Configure message format options:
   - **Include Sender Name**: Show who sent the WhatsApp message
   - **Include Timestamp**: Show when the message was sent
   - **Custom Prefix**: Add a prefix to identify forwarded messages
4. Tap "Test SMS" to verify the configuration
5. Tap "Save Settings"

### 4. Enable Forwarding

1. Return to the main screen
2. Toggle "Enable Forwarding" switch
3. The app will now forward WhatsApp messages as SMS

## How It Works

1. **Notification Monitoring**: The app uses Android's NotificationListenerService to monitor all notifications
2. **WhatsApp Filtering**: Only processes notifications from WhatsApp (com.whatsapp and com.whatsapp.w4b)
3. **Message Extraction**: Extracts sender name and message content from notification data
4. **SMS Forwarding**: Sends the extracted message as SMS to the configured phone number
5. **Formatting**: Applies user-configured formatting (prefix, sender, timestamp)

## Privacy & Security

- **No Data Storage**: Messages are not stored on the device or sent to external servers
- **Local Processing**: All message processing happens locally on your device
- **Minimal Permissions**: Only requests necessary permissions for functionality
- **User Control**: You can disable forwarding at any time

## Troubleshooting

### Forwarding Not Working

1. **Check Permissions**: Ensure both SMS and notification access permissions are granted
2. **Verify Settings**: Confirm the target phone number is correct and forwarding is enabled
3. **Test SMS**: Use the "Test SMS" feature to verify SMS sending works
4. **Restart App**: Close and reopen the app to refresh permissions

### Messages Not Being Detected

1. **Notification Access**: Ensure notification access is properly granted in system settings
2. **WhatsApp Notifications**: Make sure WhatsApp notifications are enabled
3. **App Status**: Check if the app is running and forwarding is enabled

### SMS Not Sending

1. **SMS Permissions**: Verify all SMS permissions are granted
2. **Phone Number Format**: Ensure the target phone number includes country code
3. **Network Connection**: Check if you have cellular network access
4. **SMS Limits**: Some carriers have daily SMS limits

## Technical Details

### Architecture

- **MVVM Pattern**: Clean separation of concerns with ViewModels
- **Repository Pattern**: Centralized data management
- **Service-Based**: Background services for continuous operation
- **Coroutines**: Asynchronous processing for better performance

### Key Components

- `WhatsAppNotificationListenerService`: Monitors notifications
- `SMSManager`: Handles SMS sending
- `NotificationParser`: Extracts message data from notifications
- `PreferenceManager`: Manages app settings
- `MainActivity` & `SettingsActivity`: User interface

### Permissions Used

- `SEND_SMS`: Send SMS messages
- `RECEIVE_SMS`: Receive SMS messages (for future features)
- `READ_SMS`: Read SMS messages (for future features)
- `BIND_NOTIFICATION_LISTENER_SERVICE`: Access notifications
- `POST_NOTIFICATIONS`: Show app notifications
- `WAKE_LOCK`: Keep device awake for processing
- `FOREGROUND_SERVICE`: Run background service

## Development

### Building the Project

#### Option 1: Command Line (No Android Studio Required)

1. **Prerequisites**:
   - Java JDK 11 or higher
   - Android SDK (command line tools or full SDK)
   - Set `ANDROID_HOME` environment variable

2. **Quick Setup**:
   ```bash
   # Windows
   setup.bat
   
   # Linux/macOS
   chmod +x setup.sh
   ./setup.sh
   ```

3. **Build APK**:
   ```bash
   # Windows
   build.bat
   
   # Linux/macOS
   ./build.sh
   ```

4. **Manual Build**:
   ```bash
   # Create local.properties with SDK path
   echo "sdk.dir=YOUR_ANDROID_SDK_PATH" > local.properties
   
   # Build APK
   ./gradlew assembleDebug
   ./gradlew assembleRelease
   ```

   **APK Output**: `app/build/outputs/apk/debug/app-debug.apk`

#### Option 2: Android Studio

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device or emulator

### Detailed Build Instructions

See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for comprehensive setup and build instructions.

### Dependencies

- AndroidX libraries
- Material Design Components
- Kotlin Coroutines
- Timber (Logging)

## License

This project is for educational purposes. Please ensure compliance with local laws and WhatsApp's terms of service when using this application.

## Disclaimer

This app is provided as-is for educational purposes. Users are responsible for ensuring compliance with applicable laws and terms of service. The developers are not responsible for any misuse of this application.
