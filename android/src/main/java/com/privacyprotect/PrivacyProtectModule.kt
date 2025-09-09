package com.privacyprotect

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap

class PrivacyProtectModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val NAME = "PrivacyProtect"
    }

    private var overlayView: View? = null
    private var overlayColor: Int = 0x80000000.toInt() // default yarı saydam siyah
    private var animated: Boolean = true
    private var animationDuration: Int = 300
    private var secureFlag: Boolean = false
    private var imageUri: String? = null

    override fun getName() = NAME

    /**
     * Configure privacy protect options from JS
     */
    @ReactMethod
    fun configure(options: ReadableMap) {
        if (options.hasKey("overlayColor")) {
            overlayColor = try {
                Color.parseColor(options.getString("overlayColor"))
            } catch (e: Exception) {
                0x80000000.toInt()
            }
        }
        if (options.hasKey("animated")) {
            animated = options.getBoolean("animated")
        }
        if (options.hasKey("animationDuration")) {
            animationDuration = options.getInt("animationDuration")
        }
        if (options.hasKey("secureFlag")) {
            secureFlag = options.getBoolean("secureFlag")
        }
        if (options.hasKey("image")) {
            imageUri = options.getString("image")
        }
    }

    /**
     * Add overlay or image when app goes to background
     */
    fun addPrivacyView(activity: Activity) {
        val decorView = activity.window.decorView as FrameLayout

        // FLAG_SECURE → Screenshot engelle + App Switcher’da siyah ekran
        if (secureFlag) {
            activity.window.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_SECURE,
                android.view.WindowManager.LayoutParams.FLAG_SECURE
            )
            return
        }

        if (overlayView == null) {
            overlayView = if (imageUri != null) {
                val imageView = ImageView(activity)
                try {
                    val inputStream = activity.contentResolver.openInputStream(Uri.parse(imageUri))
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                    imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                } catch (_: Exception) { }
                imageView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                imageView
            } else {
                View(activity).apply {
                    setBackgroundColor(overlayColor)
                    layoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }

        if (overlayView?.parent == null) {
            overlayView?.alpha = 0f
            decorView.addView(overlayView)
            if (animated) {
                overlayView?.animate()
                    ?.alpha(1f)
                    ?.setDuration(animationDuration.toLong())
                    ?.start()
            } else {
                overlayView?.alpha = 1f
            }
        }
    }

    /**
     * Remove overlay or image when app comes back to foreground
     */
    fun removePrivacyView(activity: Activity) {
        val decorView = activity.window.decorView as FrameLayout
        overlayView?.let {
            if (animated) {
                it.animate()
                    .alpha(0f)
                    .setDuration(animationDuration.toLong())
                    .withEndAction {
                        decorView.removeView(it)
                        overlayView = null
                    }
                    .start()
            } else {
                decorView.removeView(it)
                overlayView = null
            }
        }
        if (secureFlag) {
            activity.window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}
