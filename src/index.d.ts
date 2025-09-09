declare module 'react-native-privacy-protect' {
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

  /**
   * Configure the behavior of the privacy protection screen.
   * Call once (e.g., in App.tsx) before the app goes background.
   */
  export function configurePrivacyProtect(options: PrivacyProtectOptions): void;
}
