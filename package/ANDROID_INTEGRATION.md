# Android Integration Guide

This guide provides detailed instructions for integrating `@enginnblt/react-native-privacy-protect` into your Android React Native application.

## Overview

The Android implementation uses the `onWindowFocusChanged()` lifecycle method to detect when your app loses or gains focus. This approach provides immediate privacy protection without requiring additional permissions.

## Quick Setup

### 1. Install the Package

```bash
npm install @enginnblt/react-native-privacy-protect
```

### 2. Update MainActivity

Add the focus handling code to your `MainActivity.kt`:

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

### 3. Configure in React Native

```javascript
import PrivacyProtect from '@enginnblt/react-native-privacy-protect';
import { useEffect } from 'react';

export default function App() {
  useEffect(() => {
    PrivacyProtect.configurePrivacyProtect({
      autoEnable: true, // This enables the focus-based protection
      blurRadius: 20,   // Android 12+ blur effect
      overlayColor: '#00000066', // Semi-transparent overlay
      animated: true,
      animationDuration: 300,
    });
  }, []);

  return (
    // Your app content
  );
}
```

## How Focus Detection Works

The Android implementation leverages the `onWindowFocusChanged()` lifecycle method:

### When `hasFocus = false` (App loses focus):
- User presses the home button
- User switches to another app
- User opens the notification panel
- User opens the recent apps switcher
- System dialogs appear (calls, alarms, etc.)

### When `hasFocus = true` (App gains focus):
- User returns to your app from another app
- User returns from the home screen
- User dismisses system dialogs
- User closes the notification panel

## Android-Specific Features

### Blur Effects (Android 12+)

On Android 12 and above, the library can apply native blur effects:

```javascript
PrivacyProtect.configurePrivacyProtect({
  blurRadius: 25,        // Blur intensity (0-50 recommended)
  overlayColor: '#00000040', // Semi-transparent overlay on top of blur
  animated: true,        // Smooth blur animation
  animationDuration: 300, // Animation duration in milliseconds
});
```

**Technical Details:**
- Uses `RenderEffect.createBlurEffect()` for hardware-accelerated blur
- Falls back to overlay-only protection on Android < 12
- Blur is applied to the entire window decor view
- Overlay is rendered on top of the blur effect

### Background Images

Use custom images as privacy overlays:

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

**Image Implementation:**
- Uses Fresco for efficient image loading and caching
- Images are automatically scaled using `CENTER_CROP`
- Supports both local assets and remote URLs
- Overlay color is applied on top of the image

### Security Options

Enable FLAG_SECURE for maximum security:

```javascript
PrivacyProtect.configurePrivacyProtect({
  secureFlag: true, // Blocks screenshots and prevents content in app switcher
});
```

**FLAG_SECURE Behavior:**
- Prevents screenshots and screen recordings
- Hides app content in the recent apps switcher
- Shows a blank screen instead of app content
- Useful for banking, payment, or highly sensitive apps

## Advanced Configuration

### Dynamic Configuration

You can update the privacy protection settings at runtime:

```javascript
// Switch to blur mode
const enableBlurMode = () => {
  PrivacyProtect.configurePrivacyProtect({
    blurRadius: 20,
    overlayColor: '#00000080',
    animated: true,
  });
};

// Switch to image mode
const enableImageMode = () => {
  PrivacyProtect.configurePrivacyProtect({
    backgroundImage: require('./assets/custom-bg.png'),
    overlayColor: '#00000040',
    animated: true,
  });
};

// Switch to secure mode
const enableSecureMode = () => {
  PrivacyProtect.configurePrivacyProtect({
    secureFlag: true,
    overlayColor: '#00000000', // No overlay needed with FLAG_SECURE
  });
};
```

### Performance Optimization

**Memory Management:**
- Views are reused to minimize memory allocation
- Images are cached by Fresco for efficient memory usage
- Blur effects are hardware-accelerated on supported devices

**Animation Performance:**
- Blur animations use `ValueAnimator` for smooth 60fps performance
- Animations are automatically cancelled when switching between states
- Minimal CPU usage during idle state

**Recommended Settings:**
```javascript
// For older devices
PrivacyProtect.configurePrivacyProtect({
  blurRadius: 15,        // Lower blur radius
  animated: false,      // Disable animations
  overlayColor: '#00000080', // Stronger overlay
});

// For modern devices
PrivacyProtect.configurePrivacyProtect({
  blurRadius: 25,       // Higher blur radius
  animated: true,       // Enable animations
  animationDuration: 250, // Faster animations
  overlayColor: '#00000040', // Lighter overlay
});
```

## Troubleshooting

### Common Issues

**Privacy overlay not showing:**
- Ensure `onWindowFocusChanged` is implemented in MainActivity
- Check that `PrivacyProtectModule` is properly imported
- Verify `autoEnable: true` is set in configuration
- Test by pressing home button or switching apps

**Blur not working:**
- Confirm device is running Android 12+ (API level 31+)
- Check that `blurRadius` is greater than 0
- Some devices may have blur effects disabled in developer options

**Images not loading:**
- For local images, ensure file exists in assets folder
- For remote images, check network connectivity and URL validity
- Verify image format is supported (PNG, JPG, WebP)

**Performance issues:**
- Reduce `blurRadius` value if experiencing lag
- Disable animations by setting `animated: false`
- Use smaller image files for background images

**FLAG_SECURE not working:**
- Test on physical device (not emulator)
- Some devices may have additional security settings
- Ensure flag is set before activity becomes visible

### Integration Mistakes

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

**Missing autoEnable:**
```javascript
// ❌ Wrong - autoEnable not set
PrivacyProtect.configurePrivacyProtect({
  blurRadius: 20,
  overlayColor: '#00000066',
});

// ✅ Correct - autoEnable set to true
PrivacyProtect.configurePrivacyProtect({
  autoEnable: true, // This is required for focus-based protection
  blurRadius: 20,
  overlayColor: '#00000066',
});
```

## Testing

### Manual Testing

1. **Focus Loss Test:**
   - Press the home button
   - Switch to another app
   - Open notification panel
   - Verify privacy overlay appears

2. **Focus Gain Test:**
   - Return to your app from home screen
   - Return from another app
   - Close notification panel
   - Verify privacy overlay disappears

3. **Blur Test (Android 12+):**
   - Set `blurRadius` to a high value (e.g., 30)
   - Test focus loss/gain
   - Verify blur effect is visible

4. **Image Test:**
   - Set a `backgroundImage`
   - Test focus loss/gain
   - Verify image is displayed correctly

5. **FLAG_SECURE Test:**
   - Set `secureFlag: true`
   - Take a screenshot (should fail)
   - Open recent apps (should show blank screen)

### Automated Testing

```javascript
// Example test for focus behavior
describe('PrivacyProtect Android Integration', () => {
  it('should show privacy overlay when app loses focus', async () => {
    // Simulate focus loss
    // Verify overlay is visible
  });

  it('should hide privacy overlay when app gains focus', async () => {
    // Simulate focus gain
    // Verify overlay is hidden
  });
});
```

## Best Practices

1. **Always implement `onWindowFocusChanged`** in your MainActivity
2. **Set `autoEnable: true`** for automatic protection
3. **Test on both physical devices and emulators**
4. **Use appropriate blur radius** for your target devices
5. **Consider performance implications** of animations and blur effects
6. **Test with different Android versions** (especially Android 12+ for blur)
7. **Use FLAG_SECURE** for highly sensitive applications
8. **Optimize image sizes** for background images

## Migration from Other Libraries

If you're migrating from other privacy protection libraries:

1. **Remove old lifecycle listeners** (AppState, etc.)
2. **Add `onWindowFocusChanged`** to MainActivity
3. **Update configuration** to match new API
4. **Test thoroughly** on different devices and Android versions

## Support

For issues specific to Android integration:

1. Check this guide first
2. Review the troubleshooting section
3. Test with the example app
4. Open an issue on GitHub with:
   - Android version
   - Device model
   - Code snippets
   - Error logs
