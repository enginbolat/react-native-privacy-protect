# react-native-privacy-protect

A cross-platform React Native library to protect sensitive app content when the app goes to the background or appears in the app switcher. Provides a blur (iOS) or secure overlay (Android) to hide UI content automatically.

## Features

- **Automatic Background Detection**: Automatically shows privacy protection when app goes to background
- **Cross-Platform**: Works on both iOS and Android
- **Customizable Overlays**: Support for custom images, blur effects, and overlay colors
- **Smooth Animations**: Configurable fade in/out animations
- **TypeScript Support**: Full TypeScript definitions included

## Installation

```sh
npm install @enginnblt/react-native-privacy-protect
```

### React Native CLI Setup

#### iOS Setup

```sh
cd ios && pod install
```

#### Android Setup

The Android integration requires manual setup in your `MainActivity` to handle focus events. This is necessary because React Native doesn't provide built-in lifecycle events for when the app loses/gains focus.

**1. Update your MainActivity:**

```kotlin
// MainActivity.kt
package com.yourpackage

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.privacyprotect.PrivacyProtectModule

class MainActivity : ReactActivity() {
  
  /**
   * This method is called when the window focus changes.
   * It's the key integration point for privacy protection.
   * 
   * hasFocus = true:  App is in foreground (user can see it)
   * hasFocus = false: App is in background (user switched away or home button pressed)
   */
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    
    if (hasFocus) {
      // App gained focus - remove privacy overlay
      PrivacyProtectModule.removePrivacyCover(this)
    } else {
      // App lost focus - show privacy overlay
      PrivacyProtectModule.applyPrivacyCover(this)
    }
  }

  override fun getMainComponentName(): String = "YourAppName"

  override fun createReactActivityDelegate(): ReactActivityDelegate =
    DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
```

**2. Import the PrivacyProtectModule:**

Make sure to import the module in your MainActivity:
```kotlin
import com.privacyprotect.PrivacyProtectModule
```

**3. Configure in your React Native code:**

```javascript
import PrivacyProtect from '@enginnblt/react-native-privacy-protect';

useEffect(() => {
  PrivacyProtect.configurePrivacyProtect({
    autoEnable: true, // This enables the focus-based protection
    blurRadius: 20,   // Android 12+ blur effect
    overlayColor: '#00000066', // Semi-transparent overlay
    animated: true,
    animationDuration: 300,
  });
}, []);
```

**How the Focus Integration Works:**

The Android implementation uses the `onWindowFocusChanged()` lifecycle method to detect when your app loses or gains focus:

- **App loses focus** (`hasFocus = false`): User presses home button, switches to another app, or opens notification panel
- **App gains focus** (`hasFocus = true`): User returns to your app from another app or home screen

This approach provides immediate privacy protection without requiring additional permissions or complex setup.

> 📖 **For detailed Android integration instructions, see [ANDROID_INTEGRATION.md](./ANDROID_INTEGRATION.md)**

## Android-Specific Features

### Blur Effects (Android 12+)

On Android 12 and above, the library can apply native blur effects to the entire app content:

```javascript
PrivacyProtect.configurePrivacyProtect({
  blurRadius: 25,        // Blur intensity (0-50 recommended)
  overlayColor: '#00000040', // Semi-transparent overlay on top of blur
  animated: true,        // Smooth blur animation
  animationDuration: 300, // Animation duration in milliseconds
});
```

**Blur Behavior:**
- **Android 12+**: Native `RenderEffect.createBlurEffect()` provides hardware-accelerated blur
- **Android < 12**: Falls back to overlay-only protection (no blur available)

### Background Images

You can use custom images as privacy overlays instead of blur effects:

```javascript
// Local image from assets
PrivacyProtect.configurePrivacyProtect({
  backgroundImage: require('./assets/privacy-bg.png'),
  overlayColor: '#00000030', // Light overlay on top of image
});

// Remote image
PrivacyProtect.configurePrivacyProtect({
  backgroundImage: { uri: 'https://example.com/privacy-image.jpg' },
  overlayColor: '#00000050',
});
```

**Image Features:**
- Supports both local assets and remote URLs
- Uses Fresco for efficient image loading and caching
- Images are automatically scaled to cover the entire screen
- Overlay color is applied on top of the image for additional privacy

### Performance Considerations

**Memory Management:**
- Views are reused to minimize memory allocation
- Images are cached by Fresco for efficient memory usage
- Blur effects are hardware-accelerated on supported devices

**Animation Performance:**
- Blur animations use `ValueAnimator` for smooth 60fps performance
- Animations are automatically cancelled when switching between states
- Minimal CPU usage during idle state

## Examples

### React Native CLI Example
See the `example/` directory for a React Native CLI example.

## Usage

### Basic Usage with Auto-Enable

```js
import PrivacyProtect from '@enginnblt/react-native-privacy-protect';

// Configure once in your App.tsx
useEffect(() => {
  PrivacyProtect.configurePrivacyProtect({
    autoEnable: true, // Automatically show privacy overlay when app goes to background
    blurRadius: 20, // Android 12+ blur effect
    blurStyle: 'dark', // iOS blur style
    overlayColor: '#00000066', // Semi-transparent overlay
    animated: true,
    animationDuration: 300,
  });
}, []);
```

### Manual Control

```js
import PrivacyProtect from '@enginnblt/react-native-privacy-protect';

// Configure without auto-enable
PrivacyProtect.configurePrivacyProtect({
  autoEnable: false,
  blurRadius: 15,
  overlayColor: '#00000080',
});

// Manually show/hide (if autoEnable is false)
// Note: These methods are available in the native module but not exposed in the JS API yet
```

### Custom Background Image

```js
PrivacyProtect.configurePrivacyProtect({
  backgroundImage: require('./assets/privacy-overlay.png'), // Local image
  overlayColor: '#00000040', // Light overlay on top of image
  animated: true,
});
```

### Remote Image

```js
PrivacyProtect.configurePrivacyProtect({
  backgroundImage: { uri: 'https://example.com/privacy-image.jpg' }, // Remote image
  overlayColor: '#00000060',
  animated: true,
});
```

### Dynamic Image Switching

```js
// Switch between different privacy modes
const switchToBlurMode = () => {
  PrivacyProtect.configurePrivacyProtect({
    blurStyle: 'dark',
    overlayColor: '#00000080',
    animated: true,
  });
};

const switchToImageMode = () => {
  PrivacyProtect.configurePrivacyProtect({
    backgroundImage: require('./assets/custom-bg.png'),
    overlayColor: '#00000040',
    animated: true,
  });
};
```

## API Reference

### `configurePrivacyProtect(options)`

Configure the privacy protection behavior.

#### Options

| Option | Type | Platform | Default | Description |
|--------|------|----------|---------|-------------|
| `autoEnable` | `boolean` | Android | `false` | Automatically show privacy overlay when app goes to background |
| `blurRadius` | `number` | Android 12+ | `0` | Blur radius for the privacy overlay |
| `blurStyle` | `'dark' \| 'light' \| 'extraLight'` | iOS | - | iOS blur style |
| `overlayColor` | `string` | Both | `'#80000000'` | Overlay color (hex format) |
| `animated` | `boolean` | Both | `true` | Enable fade animations |
| `animationDuration` | `number` | Both | `300` | Animation duration in milliseconds |
| `backgroundImage` | `ImageSourcePropType` | Both | - | Custom image for privacy overlay (local or remote) |

## Troubleshooting

### Android Issues

**Privacy overlay not showing:**
- Ensure you've added the `onWindowFocusChanged` method to your `MainActivity`
- Check that `PrivacyProtectModule` is properly imported
- Verify that `autoEnable: true` is set in your configuration
- Test by pressing the home button or switching to another app

**Blur not working on Android 12+:**
- Confirm your device is running Android 12 (API level 31) or higher
- Check that `blurRadius` is set to a value greater than 0
- Some devices may have blur effects disabled in developer options

**Images not loading:**
- For local images, ensure the image file exists in your assets folder
- For remote images, check network connectivity and image URL validity
- Verify image format is supported (PNG, JPG, WebP)

**Performance issues:**
- Reduce `blurRadius` value if experiencing lag
- Disable animations by setting `animated: false`
- Use smaller image files for background images

### Common Integration Mistakes

**Missing MainActivity setup:**
```kotlin
// ❌ Wrong - Missing onWindowFocusChanged
class MainActivity : ReactActivity() {
  override fun getMainComponentName(): String = "YourApp"
}

// ✅ Correct - With focus handling
class MainActivity : ReactActivity() {
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) {
      PrivacyProtectModule.removePrivacyCover(this)
    } else {
      PrivacyProtectModule.applyPrivacyCover(this)
    }
  }
  // ... rest of your code
}
```

**Incorrect import:**
```kotlin
// ❌ Wrong import
import com.privacyprotect.PrivacyProtect

// ✅ Correct import
import com.privacyprotect.PrivacyProtectModule
```

## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
