package privacyprotect.example

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
    DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
