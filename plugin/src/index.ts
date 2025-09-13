import { type ConfigPlugin, createRunOncePlugin, withPlugins } from '@expo/config-plugins';

const pkg = {
  name: '@engin/react-native-privacy-protect',
  version: 'UNVERSIONED',
};

/**
 * A config plugin for react-native-privacy-protect.
 * This plugin doesn't require any native configuration changes
 * as the library works automatically once installed.
 */
const withPrivacyProtect: ConfigPlugin<void | {}> = (config, _props) => {
  return withPlugins(config, [
    // No specific native configuration needed for this library
    // The library automatically handles privacy protection
  ]);
};

export default createRunOncePlugin(withPrivacyProtect, pkg.name, pkg.version);
