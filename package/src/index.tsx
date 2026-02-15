import { processColor, type ImageSourcePropType } from 'react-native';
import NativePrivacyProtectTurbo from './NativePrivacyProtect';
import LegacyPrivacyProtect from './LegacyPrivacyProtect';
import { resolveImageSource } from './helper/resolveAsset';

// Use TurboModule if available, otherwise fall back to legacy
const NativePrivacyProtect = NativePrivacyProtectTurbo || LegacyPrivacyProtect;

export type PrivacyProtectOptions = {
  blurStyle?: 'dark' | 'light' | 'extraLight';
  overlayColor?: string | number | null;
  animated?: boolean;
  animationDuration?: number;
  secureFlag?: boolean;
  backgroundImage?: ImageSourcePropType;
};

export function configurePrivacyProtect(options: PrivacyProtectOptions = {}) {
  const overlayColor =
    options.overlayColor != null
      ? (processColor(options.overlayColor) as number | undefined)
      : (processColor('#00000080') as number);

  let backgroundImage: string | undefined;
  if (options.backgroundImage != null) {
    const resolved = resolveImageSource(options.backgroundImage);
    backgroundImage = resolved?.uri;
  }

  const merged = {
    blurStyle: options.blurStyle ?? 'dark',
    overlayColor,
    animated: options.animated ?? true,
    animationDuration: options.animationDuration ?? 300,
    secureFlag: options.secureFlag ?? false,
    backgroundImage,
  };

  NativePrivacyProtect.configure(merged);
}

export default {
  configurePrivacyProtect,
};
