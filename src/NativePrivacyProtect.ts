import {
  NativeModules,
  TurboModuleRegistry,
  type TurboModule,
} from 'react-native';

const { PrivacyProtect } = NativeModules;

export type PrivacyProtectOptions = {
  /**
   * iOS only: Blur style for the privacy screen.
   * Options: "dark", "light", "extraLight"
   */
  blurStyle?: 'dark' | 'light' | 'extraLight';

  /**
   * Overlay color on top of the blur (iOS) or background overlay (Android).
   * Accepts hex string, e.g. "#00000080" (black with 50% opacity).
   */
  overlayColor?: string;

  /**
   * Animate the appearance and disappearance of the privacy screen.
   * Default: true
   */
  animated?: boolean;

  /**
   * Animation duration in milliseconds.
   * Only used if `animated` is true.
   * Default: 300
   */
  animationDuration?: number;

  /**
   * Android only: If true, uses FLAG_SECURE to block screenshots
   * and show a black screen in the app switcher instead of blur.
   * Default: false
   */
  secureFlag?: boolean;
};

export function configurePrivacyProtect(options: PrivacyProtectOptions) {
  if (PrivacyProtect && typeof PrivacyProtect.configure === 'function') {
    PrivacyProtect.configure(options);
  } else {
    console.warn(
      '[react-native-privacy-protect] Native module not found or configure method missing'
    );
  }
}

export interface Spec extends TurboModule {
  configure(options: PrivacyProtectOptions): void;
}

export const PrivacyProtectTurbo =
  TurboModuleRegistry.getEnforcing<Spec>('PrivacyProtect');
