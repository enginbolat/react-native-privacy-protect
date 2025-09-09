import { useEffect } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import { configurePrivacyProtect } from 'react-native-privacy-protect';

export default function App() {
  useEffect(() => {
    configurePrivacyProtect({
      blurStyle: 'dark',
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>React Native Privacy Protect</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
