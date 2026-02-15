import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { configurePrivacyProtect } from '@enginnblt/react-native-privacy-protect';
import { Stack } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import React, { useEffect } from 'react';
import 'react-native-reanimated';

import { useColorScheme } from '@/hooks/use-color-scheme';

export const unstable_settings = {
  anchor: '(tabs)',
};

export default function RootLayout() {
  const colorScheme = useColorScheme();

  useEffect(() => {
    configurePrivacyProtect({
      blurStyle: 'extraLight',
      animated: true,
      animationDuration: 0,
      backgroundImage: require('../assets/images/arches.jpg'),
    });
  }, []);

  return (
    <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
      <Stack>
        <Stack.Screen name="index" />
      </Stack>
      <StatusBar style="auto" />
    </ThemeProvider>
  );
}
