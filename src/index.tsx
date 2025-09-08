import PrivacyProtect from './NativePrivacyProtect';

export function multiply(a: number, b: number): number {
  return PrivacyProtect.multiply(a, b);
}
