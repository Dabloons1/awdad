# Soul Strike Floating Menu - LSPosed Module

An LSPosed module that adds a floating window menu to the Soul Strike game (`com.com2usholdings.soulstrike.android.google.global.normal`).

## Features

- 🎮 **ImGui Floating Menu**: A powerful, modern floating menu using ImGui for professional UI
- 🎨 **Modern Interface**: Beautiful dark theme with collapsible sections and tooltips
- ⚡ **Real-time Controls**: Instant feature toggles with live preview
- 🎯 **Game Hooks**: Direct integration with Soul Strike game functions
- 🛠️ **Extensible**: Easy to add new features and modifications
- 📱 **LSPosed Compatible**: Built for LSPosed 1.9.2+ framework
- 🎮 **Unity Support**: Optimized for Unity-based games like Soul Strike

## Installation

### Prerequisites
- Android device with root access
- LSPosed framework installed
- Soul Strike game installed

### Steps
1. **Install LSPosed Framework**
   - Download and install LSPosed from [official repository](https://github.com/LSPosed/LSPosed)
   - Reboot your device after installation

2. **Install the Module**
   - Download the latest APK from [Releases](../../releases)
   - Install the APK on your device

3. **Enable in LSPosed**
   - Open LSPosed Manager
   - Go to "Modules" tab
   - Enable "Soul Strike Floating Menu"
   - Set scope to include Soul Strike app (`com.com2usholdings.soulstrike.android.google.global.normal`)

4. **Reboot and Test**
   - Reboot your device
   - Launch Soul Strike
   - The floating menu should appear automatically

## Usage

Once installed and configured:

1. **Launch Soul Strike** - The ImGui menu will appear automatically
2. **Navigate the menu** - Use collapsible sections to organize features
3. **Toggle features** - Enable/disable features with checkboxes and sliders
4. **Real-time adjustments** - Modify settings like speed, alpha, and scale instantly
5. **Apply changes** - Use "Apply All Changes" to activate selected features
6. **Hide menu** - Click "Hide Menu" to completely hide the floating window

## Building from Source

### Prerequisites
- Android Studio or command line tools
- JDK 17 or higher
- Android SDK

### Build Steps
1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd SoulStrikeFloatingMenu
   ```

2. **Setup Gradle Wrapper** (if not already present)
   ```bash
   # On Linux/Mac
   chmod +x setup-gradle.sh
   ./setup-gradle.sh
   
   # On Windows
   setup-gradle.bat
   ```

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Generate APK**
   ```bash
   ./gradlew assembleRelease
   ```

The APK will be generated at `app/build/outputs/apk/release/app-release.apk`

## GitHub Actions

This project includes automated builds via GitHub Actions:

- **Automatic builds** on push to main/master branches
- **Release creation** with APK artifacts
- **Cross-platform compatibility** testing

### Manual Build Trigger
You can manually trigger builds using the "Actions" tab in GitHub.

## Project Structure

```
├── app/
│   ├── src/main/
│   │   ├── java/com/soulstrikefloatingmenu/
│   │   │   ├── HookEntry.kt          # Main Xposed hook entry point
│   │   │   ├── FloatingWindow.kt     # Floating window implementation
│   │   │   └── MainActivity.kt       # Module configuration UI
│   │   ├── assets/
│   │   │   └── xposed_init           # Xposed module entry point
│   │   └── res/                      # Resources and layouts
│   └── build.gradle                  # App-level build configuration
├── .github/workflows/
│   └── build.yml                     # GitHub Actions workflow
└── README.md                         # This file
```

## Development

### Adding New Features

1. **Modify FloatingWindow.kt** to add new buttons and functionality
2. **Update HookEntry.kt** if you need to hook additional methods
3. **Test thoroughly** with the target app
4. **Update documentation** as needed

### Key Components

- **HookEntry.kt**: Main hook class that gets loaded by LSPosed
- **FloatingWindow.kt**: Manages the floating window UI and interactions
- **xposed_init**: Tells LSPosed which class to load as the entry point

## Troubleshooting

### Module Not Loading
- Ensure LSPosed is properly installed and active
- Check that the module is enabled in LSPosed Manager
- Verify the scope includes the Soul Strike app
- Reboot after enabling the module

### Floating Window Not Appearing
- Check if the app has overlay permissions
- Ensure the target app is running
- Check LSPosed logs for any error messages

### Build Issues
- Ensure you have JDK 17+ installed
- Check that Android SDK is properly configured
- Verify all dependencies are resolved

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Disclaimer

This module is for educational purposes only. Use at your own risk. The authors are not responsible for any damage to your device or account.

## Support

For issues and questions:
- Create an issue on GitHub
- Check the troubleshooting section above
- Review LSPosed documentation for framework-related issues
