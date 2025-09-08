import { NativeModules } from 'react-native';

const { PrivacyProtect } = NativeModules;

export function configurePrivacyProtect(options: {
  blurStyle?: 'dark' | 'light' | 'extraLight',
  overlayColor?: string,
  animated?: boolean,
  animationDuration?: number,
  secureFlag?: boolean,
}) {
  PrivacyProtect.configure(options);
}
