const path = require('path');

module.exports = {
  project: {
    ios: {
      automaticPodsInstallation: true,
    },
  },
  dependencies: {
    'react-native-privacy-protect': {
      root: path.join(__dirname, '..'),
      platforms: {
        ios: {
          podspecPath: '../PrivacyProtect.podspec',
        },
        android: {
          sourceDir: '../android',
        },
      },
    },
  },
};
