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

    /**
     * Android only: Blur radius for the privacy overlay.
     * Only works on Android 12+ (API 31+).
     * Default: 0 (no blur)
     */
    blurRadius?: number;

    /**
     * Android only: Enable automatic privacy protection when app goes to background.
     * When true, the privacy overlay will automatically show when the app loses focus
     * and hide when the app comes back to foreground.
     * Default: false
     */
    autoEnable?: boolean;

    /**
     * Image source for custom privacy overlay.
     * Can be either a URI string or an object with uri/resourceId.
     */
    image?: string;
    imageSource?: {
      uri?: string;
      resourceId?: number;
    };
  };

  /**
   * Configure the behavior of the privacy protection screen.
   * Call once (e.g., in App.tsx) before the app goes background.
   */
  export function configurePrivacyProtect(options: PrivacyProtectOptions): void;
}
