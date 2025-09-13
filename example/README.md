# React Native Privacy Protect - Example App

This is an example React Native application demonstrating how to use the `@enginnblt/react-native-privacy-protect` library.

## Features Demonstrated

- **Automatic Privacy Protection**: The app automatically shows a privacy overlay when it goes to the background
- **Customizable Blur Effects**: Demonstrates different blur styles (dark, light, extraLight)
- **Background Images**: Shows how to use custom background images for the privacy overlay
- **Animation Options**: Configurable animation duration and effects
- **Secure Flag**: Android-specific secure flag configuration

## Installation

```bash
# Clone the repository
git clone https://github.com/enginbolat/react-native-privacy-protect.git
cd react-native-privacy-protect/example

# Install dependencies
npm install
# or
yarn install

# For iOS
cd ios && pod install && cd ..

# Run the app
npm run ios
# or
npm run android
```

## Usage

The example app shows a simple implementation of the privacy protection library:

```tsx
import PrivacyProtect from '@enginnblt/react-native-privacy-protect';

export default function App() {
  useEffect(() => {
    PrivacyProtect.configurePrivacyProtect({
      blurStyle: 'light',
      animated: true,
      animationDuration: 250,
      secureFlag: false,
      backgroundImage: require('./assets/arches.jpg'),
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>React Native Privacy Protect</Text>
      <Text>Auto-enable is active! Try switching to another app.</Text>
    </View>
  );
}
```

## Configuration Options

The example demonstrates various configuration options:

- `blurStyle`: 'dark' | 'light' | 'extraLight'
- `overlayColor`: Custom overlay color
- `animated`: Enable/disable animations
- `animationDuration`: Animation duration in milliseconds
- `secureFlag`: Android secure flag
- `backgroundImage`: Custom background image

## Testing

1. Run the app on a device or simulator
2. Switch to another app or go to the home screen
3. The privacy overlay should automatically appear
4. Return to the app to see the overlay disappear

## Requirements

- React Native 0.81+
- iOS 11.0+ / Android API 21+
- Node.js 20+

## License

MIT License - see the main repository for details.