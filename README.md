# react-native-privacy-protect

A cross-platform React Native library to protect sensitive app content when the app goes to the background or appears in the app switcher. Provides a blur (iOS) or secure overlay (Android) to hide UI content automatically.

## Features

- **Automatic Background Detection**: Automatically shows privacy protection when app goes to background
- **Cross-Platform**: Works on both iOS and Android
- **Customizable Overlays**: Support for custom images, blur effects, and overlay colors
- **Smooth Animations**: Configurable fade in/out animations
- **Security Options**: Android FLAG_SECURE support to block screenshots and app switcher
- **TypeScript Support**: Full TypeScript definitions included

## Installation

```sh
npm install @engin/react-native-privacy-protect
```

### React Native CLI Setup

#### iOS Setup

```sh
cd ios && pod install
```

#### Android Setup

No additional setup required for Android.

### Expo Setup

For Expo projects using development builds (not Expo Go):

1. Install the library:
```sh
npx expo install @engin/react-native-privacy-protect
```

2. Add the config plugin to your `app.config.js` or `app.json`:

**app.config.js**:
```js
export default {
  expo: {
    name: 'your-app',
    plugins: [
      '@engin/react-native-privacy-protect'
    ],
  },
};
```

**app.json**:
```json
{
  "expo": {
    "name": "your-app",
    "plugins": [
      "@engin/react-native-privacy-protect"
    ]
  }
}
```

3. Create a new development build:
```sh
npx expo prebuild
npx expo run:ios # or npx expo run:android
```

**Note**: This library requires native code and cannot be used with Expo Go. You need to use a development build or bare workflow.

## Examples

### React Native CLI Example
See the `example/` directory for a React Native CLI example.

## Usage

### Basic Usage with Auto-Enable

```js
import { configurePrivacyProtect } from '@engin/react-native-privacy-protect';

// Configure once in your App.tsx
useEffect(() => {
  configurePrivacyProtect({
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
import { configurePrivacyProtect } from '@engin/react-native-privacy-protect';

// Configure without auto-enable
configurePrivacyProtect({
  autoEnable: false,
  blurRadius: 15,
  overlayColor: '#00000080',
});

// Manually show/hide (if autoEnable is false)
// Note: These methods are available in the native module but not exposed in the JS API yet
```

### Custom Background Image

```js
configurePrivacyProtect({
  backgroundImage: require('./assets/privacy-overlay.png'), // Local image
  overlayColor: '#00000040', // Light overlay on top of image
  animated: true,
});
```

### Remote Image

```js
configurePrivacyProtect({
  backgroundImage: { uri: 'https://example.com/privacy-image.jpg' }, // Remote image
  overlayColor: '#00000060',
  animated: true,
});
```

### Dynamic Image Switching

```js
// Switch between different privacy modes
const switchToBlurMode = () => {
  configurePrivacyProtect({
    blurStyle: 'dark',
    overlayColor: '#00000080',
    animated: true,
  });
};

const switchToImageMode = () => {
  configurePrivacyProtect({
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
| `secureFlag` | `boolean` | Android | `false` | Use FLAG_SECURE to block screenshots |
| `backgroundImage` | `ImageSourcePropType` | Both | - | Custom image for privacy overlay (local or remote) |


## Contributing

- [Development workflow](CONTRIBUTING.md#development-workflow)
- [Sending a pull request](CONTRIBUTING.md#sending-a-pull-request)
- [Code of conduct](CODE_OF_CONDUCT.md)

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
