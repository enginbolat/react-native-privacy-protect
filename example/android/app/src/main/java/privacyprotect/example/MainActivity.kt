package privacyprotect.example

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import com.privacyprotect.PrivacyProtectModule
import com.facebook.react.bridge.ReactApplicationContext

class MainActivity : ReactActivity() {

    override fun onPause() {
        super.onPause()
        currentReactContext()?.getNativeModule(PrivacyProtectModule::class.java)
            ?.addPrivacyView(this)
    }

    override fun onResume() {
        super.onResume()
        currentReactContext()?.getNativeModule(PrivacyProtectModule::class.java)
            ?.removePrivacyView(this)
    }

    private fun currentReactContext(): ReactApplicationContext? {
        return reactInstanceManager?.currentReactContext as? ReactApplicationContext
    }

    override fun getMainComponentName(): String = "PrivacyProtectExample"

    override fun createReactActivityDelegate(): ReactActivityDelegate =
        DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
}
