import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  configure(options: {
    blurStyle?: string;
    overlayColor?: number;
    animated?: boolean;
    animationDuration?: number;
    backgroundImage?: string;
  }): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('PrivacyProtect');
