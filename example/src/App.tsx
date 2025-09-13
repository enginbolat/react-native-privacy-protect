import { useEffect } from 'react';
import { StyleSheet, Text, View } from 'react-native';
import PrivacyProtect from '@engin/react-native-privacy-protect';

export default function App() {
  useEffect(() => {
    PrivacyProtect.configurePrivacyProtect({
      // overlayColor: 'red',
      blurStyle: 'light',
      animated: true,
      animationDuration: 250,
      secureFlag: false,
      backgroundImage: require('./assets/arches.jpg'),
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>React Native Privacy Protect</Text>
      <Text style={styles.subtitle}>
        Auto-enable is active! Try switching to another app or going to home
        screen.
      </Text>
      <Text style={styles.description}>
        The privacy overlay will automatically appear when the app goes to
        background.
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 16,
    fontWeight: '600',
    marginBottom: 10,
    textAlign: 'center',
    color: '#007AFF',
  },
  description: {
    fontSize: 14,
    textAlign: 'center',
    color: '#666',
    lineHeight: 20,
  },
  testButton: {
    backgroundColor: '#007AFF',
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderRadius: 8,
    marginTop: 20,
  },
  testButtonText: {
    color: 'white',
    fontSize: 16,
    fontWeight: '600',
  },
});
