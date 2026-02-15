// utils/resolveAsset.ts
// @ts-expect-error resolveAssetSource is not typed in RN 0.81
import resolveAssetSource from 'react-native/Libraries/Image/resolveAssetSource';
import type {
  ImageResolvedAssetSource,
  ImageSourcePropType,
} from 'react-native';

export function resolveImageSource(
  source: ImageSourcePropType
): ImageResolvedAssetSource | null {
  try {
    return resolveAssetSource(source);
  } catch (error) {
    console.warn('Failed to resolve image source:', error);
    return null;
  }
}
