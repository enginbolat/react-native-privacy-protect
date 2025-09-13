package privacyprotect.example
import expo.modules.ReactActivityDelegateWrapper

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.privacyprotect.PrivacyProtectModule

class MainActivity : ReactActivity() {
  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) {
      PrivacyProtectModule.removePrivacyCover(this)
    } else {
      PrivacyProtectModule.applyPrivacyCover(this)
    }
  }

  override fun getMainComponentName(): String = "PrivacyProtectExample"

  override fun createReactActivityDelegate(): ReactActivityDelegate =
    ReactActivityDelegateWrapper(this, BuildConfig.IS_NEW_ARCHITECTURE_ENABLED, DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled))
}
