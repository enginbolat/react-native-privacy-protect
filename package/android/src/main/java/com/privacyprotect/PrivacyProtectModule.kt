package com.privacyprotect

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View
import android.widget.FrameLayout
import android.animation.ValueAnimator
import com.facebook.react.bridge.*
import android.graphics.Color
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.bridge.ReadableType


@ReactModule(name = PrivacyProtectModule.NAME)
class PrivacyProtectModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String = "PrivacyProtect"

  companion object {
    const val NAME = "PrivacyProtect"
    private var dimView: View? = null
    private var imageView: SimpleDraweeView? = null
    private var blurAnimator: ValueAnimator? = null

    // === CONFIG vars (JS'den değiştirilebilir) ===
    private var blurRadius = 20f
    private var animated = true
    private var animationDurationMs = 220L
    private var overlayColor: Int = Color.parseColor("#99000000")
    private var backgroundImage: String? = null



    fun applyPrivacyCover(activity: Activity) {
      val win = activity.window ?: return
      val decor = win.decorView as? FrameLayout ?: return

      if (!backgroundImage.isNullOrEmpty()) {
        if (imageView == null) {
          imageView = SimpleDraweeView(activity).apply {
            layoutParams = FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT
            )
            hierarchy?.setActualImageScaleType(
              com.facebook.drawee.drawable.ScalingUtils.ScaleType.CENTER_CROP
            )
            setImageURI(backgroundImage)
            elevation = 9999f
          }
        }
        if (imageView?.parent == null) {
          decor.addView(imageView)
          decor.bringChildToFront(imageView)
        }
        return
      }

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Blur
        if (animated) {
          blurAnimator?.cancel()
          blurAnimator = ValueAnimator.ofFloat(0f, blurRadius).apply {
            duration = animationDurationMs
            addUpdateListener { a ->
              val r = a.animatedValue as Float
              decor.setRenderEffect(
                RenderEffect.createBlurEffect(r, r, Shader.TileMode.CLAMP)
              )
            }
            start()
          }
        } else {
          decor.setRenderEffect(
            RenderEffect.createBlurEffect(blurRadius, blurRadius, Shader.TileMode.CLAMP)
          )
        }

        // Overlay (blur üstüne şeffaf renk)
        if (dimView == null) {
          dimView = View(activity).apply {
            setBackgroundColor(overlayColor) // JS’ten gelen int (processColor)
            layoutParams = FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT
            )
            elevation = 9999f
          }
        }
        if (dimView?.parent == null) {
          decor.addView(dimView)
          decor.bringChildToFront(dimView)
        }
      } else {
        // Android < 12: sadece overlay
        if (dimView == null) {
          dimView = View(activity).apply {
            setBackgroundColor(overlayColor)
            layoutParams = FrameLayout.LayoutParams(
              FrameLayout.LayoutParams.MATCH_PARENT,
              FrameLayout.LayoutParams.MATCH_PARENT
            )
            elevation = 9999f
          }
        }
        if (dimView?.parent == null) {
          decor.addView(dimView)
          decor.bringChildToFront(dimView)
        }
      }
    }

    fun removePrivacyCover(activity: Activity) {
      val win = activity.window ?: return
      val decor = win.decorView as? FrameLayout ?: return

      // Blur sıfırla
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        blurAnimator?.cancel()
        blurAnimator = null
        decor.setRenderEffect(null)
      }

      // Overlay kaldır
      dimView?.let { v ->
        (v.parent as? FrameLayout)?.removeView(v)
        dimView = null
      }

      // Image kaldır
      imageView?.let { v ->
        (v.parent as? FrameLayout)?.removeView(v)
        imageView = null
      }
    }

    // === JS configure çağrısı için set edilebilir ===
    fun updateConfig(options: ReadableMap) {
      if (options.hasKey("overlayColor") && !options.isNull("overlayColor")) {
        val value = options.getDynamic("overlayColor")
        when (value.type) {
          ReadableType.Number -> {
            // processColor() int döner
            overlayColor = value.asInt()
          }
          ReadableType.String -> {
            try {
              overlayColor = Color.parseColor(value.asString())
            } catch (_: Exception) {
              overlayColor = Color.parseColor("#99000000") // fallback
            }
          }
          else -> {
            overlayColor = Color.parseColor("#99000000")
          }
        }
      }
      if (options.hasKey("animated")) {
        animated = options.getBoolean("animated")
      }
      if (options.hasKey("animationDuration")) {
        animationDurationMs = options.getInt("animationDuration").toLong()
      }
      if (options.hasKey("backgroundImage") && !options.isNull("backgroundImage")) {
        val value = options.getDynamic("backgroundImage")
        when (value.type) {
          ReadableType.String -> {
            backgroundImage = value.asString()
          }
          ReadableType.Number -> {
            // local resource id (örn: R.drawable.bg)
            val resId = value.asInt()
            backgroundImage = "res://${resId}" // Fresco local resource scheme
          }
          else -> {
            backgroundImage = null
          }
        }
      }
    }
  }


  @ReactMethod
  fun configure(options: ReadableMap) {
    updateConfig(options)
  }
}
