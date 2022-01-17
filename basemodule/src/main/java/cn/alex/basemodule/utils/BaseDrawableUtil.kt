package cn.alex.basemodule.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import cn.alex.basemodule.R

/**
 * 设置点击时的 Drawable 效果
 *
 * @param color 一个正常显示的颜色，默认白色
 * @param radius 设置按钮的圆角
 * @param radiusLeftTop 矩形左上角的圆角
 * @param radiusLeftBottom 矩形左下角的圆角
 * @param radiusRightTop 矩形右上角的圆角
 * @param radiusRightBottom 矩形右下角的圆角
 * @param pressedColor 按下后的颜色，如果没有则根据正常颜色计算出按下颜色
 * @param showContentDrawable 是否设置按下之前状态，true 有边界的正常按下状态，且会显示初始背景色
 * @param showMaskDrawable 是否设置波纹 mask，false 且 showContentDrawable 也是 false 时无边界
 * @param showCommonSelector 使用5.0之前的按下效果（无波纹）
 */
@SuppressLint("ObsoleteSdkInt")
@Suppress("SameParameterValue", "DEPRECATION")
@Deprecated("请使用 setViewForeDrawable() 方法代替")
fun View.setViewDrawable(
  color: Int = Color.parseColor("#FFFFFF"),
  radius: Float = 0f,
  radiusLeftTop: Float = 0f,
  radiusLeftBottom: Float = 0f,
  radiusRightTop: Float = 0f,
  radiusRightBottom: Float = 0f,
  pressedColor: Int = 0,
  showContentDrawable: Boolean = true,
  showMaskDrawable: Boolean = true,
  showCommonSelector: Boolean = false
) {
  // 点击后的颜色
  val pressColor = if (pressedColor == 0) shallowColorWithAlpha(color) else pressedColor

  val drawable: Drawable
  val roundRectShape: RoundRectShape

  val gradientDrawableNormal = GradientDrawable()
  gradientDrawableNormal.setColor(color)
  if (radius != 0f) {
    gradientDrawableNormal.cornerRadius = radius
    val outRadius = floatArrayOf(
      radius, radius, radius, radius,
      radius, radius, radius, radius
    )
    roundRectShape = RoundRectShape(outRadius, null, null)
  } else {
    val floatArray = floatArrayOf(
      radiusLeftTop, radiusLeftTop,
      radiusLeftBottom, radiusLeftBottom,
      radiusRightTop, radiusRightTop,
      radiusRightBottom, radiusRightBottom
    )
    gradientDrawableNormal.cornerRadii = floatArray
    roundRectShape = RoundRectShape(floatArray, null, null)
  }

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !showCommonSelector) {
    val maskDrawable = ShapeDrawable()
    maskDrawable.shape = roundRectShape
    // 点击有水波纹效果
    drawable = RippleDrawable(
      ColorStateList.valueOf(pressColor),
      if (showContentDrawable) gradientDrawableNormal else null,
      if (showMaskDrawable) maskDrawable else null
    )
  } else {
    // 正常点击效果
    val gradientDrawablePress = GradientDrawable()
    gradientDrawablePress.setColor(pressColor)
    if (radius != 0f) {
      gradientDrawablePress.cornerRadius = radius
    } else {
      gradientDrawablePress.cornerRadii = floatArrayOf(
        radiusLeftTop, radiusLeftTop,
        radiusLeftBottom, radiusLeftBottom,
        radiusRightTop, radiusRightTop,
        radiusRightBottom, radiusRightBottom
      )
    }
    drawable = StateListDrawable()
    drawable.addState(intArrayOf(android.R.attr.state_pressed), gradientDrawablePress)
    if (showContentDrawable) {
      drawable.addState(intArrayOf(), gradientDrawableNormal)
    }
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    background = drawable
  } else {
    setBackgroundDrawable(drawable)
  }
}

/**
 * 设置 View 的（包括 ImageView）前景色 foreground，即点击效果，图片也可以有点击效果，API 23 以上适用。
 * 此方法的主要目的是让图片也有按压效果。
 *
 * 如果是普通按钮（Button、TextView、RelativeLayout 等）需要同时设置背景色和前景色；
 * 如果是 ImageView，只需要设置前景色 foregroundColor。
 *
 * 注意：如果不设置背景色和前景色，默认背景色使用白色，前景色使用白色计算后的颜色。
 *
 * @param backgroundColor 按钮背景色，点击前的背景色，默认白色
 * @param foregroundColor 按钮前景色，点击后的背景色，默认白色
 * @param radius 圆角的角度
 * @param showContentDrawable 是否显示默认背景，参考下面的 RippleDrawable 注释
 * @param showMaskDrawable 是否显示波纹背景色，参考下面的 RippleDrawable 注释
 * @param showCommonSelector 是否使用原始的点击效果，即没有波纹效果
 * @param strokeWidth 边线宽度
 * @param strokeColor 边线颜色
 */
fun View.setViewForeDrawable(
  foregroundColor: Int = -1,
  backgroundColor: Int = -1,
  radius: Float = 0f,
  radiusTopLeft: Float = 0f,
  radiusTopRight: Float = 0f,
  radiusBottomRight: Float = 0f,
  radiusBottomLeft: Float = 0f,
  showContentDrawable: Boolean = false,
  showMaskDrawable: Boolean = true,
  showCommonSelector: Boolean = false,
  strokeWidth: Int = 0,
  strokeColor: Int = -1,
  isSetBackDrawable: Boolean = true,
  isShowRipple: Boolean = true
) {
  val drawable: Drawable
  // 按压后的 Shape
  val roundRectShape: RoundRectShape
  // 按压前的 Drawable
  val gradientDrawableNormal = GradientDrawable()

  val tempColor = Color.parseColor("#FFFFFF")

  val myForegroundColor = if (foregroundColor == -1) {
    // 如果没传前景色，只传了背景色，则是普通按钮设置点击效果，点击后的颜色使用背景色计算后的颜色
    if (backgroundColor != -1) {
      shallowColorWithAlpha(backgroundColor)
    } else {
      shallowColorWithAlpha(tempColor)
    }
  } else {
    foregroundColor
  }

  if (this !is ImageView) {
    if (backgroundColor == -1) {
      if (isSetBackDrawable) {
        gradientDrawableNormal.setColor(tempColor)
      }
    } else {
      gradientDrawableNormal.setColor(backgroundColor)
    }
  }

  if (radius != 0f) {
    val outRadius = floatArrayOf(
      radius, radius, radius, radius,
      radius, radius, radius, radius
    )
    gradientDrawableNormal.cornerRadius = radius
    roundRectShape = RoundRectShape(outRadius, null, null)
  } else {
    // top-left, top-right, bottom-right, bottom-left
    val floatArray = floatArrayOf(
      radiusTopLeft, radiusTopLeft,
      radiusTopRight, radiusTopRight,
      radiusBottomRight, radiusBottomRight,
      radiusBottomLeft, radiusBottomLeft
    )
    gradientDrawableNormal.cornerRadii = floatArray
    roundRectShape = RoundRectShape(floatArray, null, null)
  }
  if (strokeWidth != 0) gradientDrawableNormal.setStroke(strokeWidth, strokeColor)

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !showCommonSelector) {
    // 适配5.0，修复控件不显示默认颜色，RippleDrawable 需使用默认 Drawable content
    var showContentDrawableApi5 = false
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1
      && !showContentDrawable
      && this !is ImageView
    ) {
      showContentDrawableApi5 = true
    }
    // mask Drawable
    val maskDrawable = ShapeDrawable()
    maskDrawable.shape = roundRectShape
    // 点击有水波纹效果
    drawable = RippleDrawable(
      ColorStateList.valueOf(myForegroundColor),
      // 如果为null，没有默认背景颜色，除了控件已经有了背景色（比如页面顶部
      // 返回键）时需要 gradientDrawableNormal
      if (showContentDrawableApi5) gradientDrawableNormal
      else (if (showContentDrawable) gradientDrawableNormal else null),
      // 如果为null，点击没效果
      if (showMaskDrawable) maskDrawable else null
    )
  } else {
    // 正常点击效果
    val gradientDrawablePress = GradientDrawable()
    gradientDrawablePress.setColor(myForegroundColor)
    if (radius != 0f) {
      gradientDrawablePress.cornerRadius = radius
    } else {
      // top-left, top-right, bottom-right, bottom-left
      gradientDrawablePress.cornerRadii = floatArrayOf(
        radiusTopLeft, radiusTopLeft,
        radiusTopRight, radiusTopRight,
        radiusBottomRight, radiusBottomRight,
        radiusBottomLeft, radiusBottomLeft
      )
    }
    drawable = StateListDrawable()
    drawable.addState(intArrayOf(android.R.attr.state_pressed), gradientDrawablePress)
    // 如果是控件显示默认颜色
    if (!showContentDrawable) {
      drawable.addState(intArrayOf(), gradientDrawableNormal)
    }
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    // 如果使用非波纹按压效果，此时图片的按压效果不用处理，其他控件的按压效果只使用 background
    if (showCommonSelector) {
      if (this !is ImageView) {
        background = drawable
      }
    } else {
      // 前景色 foreground 没有按压后无限扩大的效果
      foreground = drawable
      // 如果不是 ImageView，其他 Layout 都需要默认背景色
      if (this !is ImageView) {
        background = gradientDrawableNormal
      }
    }
  } else {
    if (this !is ImageView) {
      background = drawable
    }
  }
  if (!isShowRipple) {
    isClickable = false
    isEnabled = false
  }
}

/**
 * View 添加渐变色和圆角
 */
fun View?.setGradientDrawable(
  colors: IntArray? = null,
  color: Int? = null,
  radius: Float = 0f,
  radiusTopLeft: Float = 0f,
  radiusTopRight: Float = 0f,
  radiusBottomRight: Float = 0f,
  radiusBottomLeft: Float = 0f,
) {
  if (this == null || this is ImageView) return
  val gradientDrawableNormal: GradientDrawable
  if (colors != null) {
    gradientDrawableNormal = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
  } else {
    gradientDrawableNormal = GradientDrawable()
    color?.let { gradientDrawableNormal.setColor(it) }
  }
  if (radius != 0f) {
    gradientDrawableNormal.cornerRadius = radius
  } else {
    // top-left, top-right, bottom-right, bottom-left
    val floatArray = floatArrayOf(
      radiusTopLeft, radiusTopLeft,
      radiusTopRight, radiusTopRight,
      radiusBottomRight, radiusBottomRight,
      radiusBottomLeft, radiusBottomLeft
    )
    gradientDrawableNormal.cornerRadii = floatArray
  }
  background = gradientDrawableNormal
}

/**
 * 对 color 颜色的色彩添加阴影，并加入透明度
 *
 * @param color 要改变的颜色
 * @param shallowRatio 阴影相对于 color 的比例
 * @param shallowNum 当 color 是黑色时，计算的数值不能小于 shallowNum
 * @return 添加了阴影和透明度的色值
 */
private fun shallowColorWithAlpha(
  color: Int,
  shallowRatio: Float = 0.9f,
  shallowNum: Int = 50
): Int {
  val alpha = Color.alpha(color)
  var red = Color.red(color)
  var green = Color.green(color)
  var blue = Color.blue(color)

  if (red <= shallowNum && green <= shallowNum && blue <= shallowNum) {
    red = shallowNum
    green = shallowNum
    blue = shallowNum
  } else {
    red = (red * shallowRatio).toInt()
    green = (green * shallowRatio).toInt()
    blue = (blue * shallowRatio).toInt()
  }
  return Color.argb(alpha, red, green, blue)
}

private fun shallowColor(
        color: Int = -1,
        shallowRatio: Float = 0.7f
): Int{
  val alpha = (255 * shallowRatio).toInt()
  return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
}

/**
 * 计算 color 是浅色还是深色
 */
fun isDarkStyle(color: Int?): Boolean {
  val outHsl = FloatArray(3)
  val blackMaxLightness = 0.05f
  val whiteMinLightness = 0.95f
  ColorUtils.colorToHSL(color ?: -1, outHsl)
  return when {
    outHsl[2] <= blackMaxLightness -> false
    outHsl[2] >= whiteMinLightness -> true
    else -> false
  }
}

@Suppress("unused")
fun getDrawable(context: Context, @DrawableRes res: Int, @DrawableRes selectRes: Int): Drawable {
  val stateListDrawable = StateListDrawable()
  stateListDrawable.addState(
    intArrayOf(android.R.attr.state_selected),
    ContextCompat.getDrawable(context, selectRes)
  )
  stateListDrawable.addState(intArrayOf(), ContextCompat.getDrawable(context, res))
  return stateListDrawable
}

/**
 * 加载项目中的图片，混淆后有问题，找不到本地id
 */
//fun getURLForResource(resourceId: Int): String? {
//  return Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + resourceId).toString()
//}


/**
 * 解析颜色，可以加默认颜色
 * @param c #4D4A67、4D4A67
 * @param d 必须是一个手动填写的正确（#ARGB）的值，比如：defaultColor = "#EC1500"
 */
fun parseStringColorD(c: String?, d: String? = "#EC1500"): Int {
  return parseStringColor(c) ?: Color.parseColor(d)
}
/**
 * 解析颜色
 * @param c #4D4A67、4D4A67
 */
fun parseStringColor(c: String?): Int? {
  if (!c.hasValue()) return null
  return tryCatchT {
    if (c!!.contains("#")) {
      Color.parseColor(c)
    } else {
      Color.parseColor("#$c")
    }
  }
}


fun Context.setColor(@ColorRes color: Int): Int {
  return ContextCompat.getColor(this, color)
}
fun Fragment.setColor(@ColorRes color: Int): Int {
  if (context == null) return -1
  return ContextCompat.getColor(context!!, color)
}
fun Activity.setColor(@ColorRes color: Int): Int {
  return ContextCompat.getColor(this, color)
}


// "res://"+ PackageUtils.getPackageName()+"/"+R.drawable.icon  方式也行
fun Context.setDrawable(@androidx.annotation.DrawableRes drawableId: Int): Drawable? {
  return ContextCompat.getDrawable(this, drawableId)
}
fun Fragment.setDrawable(@androidx.annotation.DrawableRes drawableId: Int): Drawable? {
  if (context == null) return null
  return ContextCompat.getDrawable(context!!, drawableId)
}
fun Activity.setDrawable(@androidx.annotation.DrawableRes drawableId: Int): Drawable? {
  return ContextCompat.getDrawable(this, drawableId)
}
fun Context.setDrawableWithColor(@ColorRes color: Int): Drawable? {
  return ColorDrawable(ContextCompat.getColor(this, color))
}


// 设置按钮不可点，并且置灰，覆盖的透明度是白色的70%
fun View?.setViewUnEnabled(alpha: Float? = null) {
  val color = if (alpha != null) shallowColor(shallowRatio = alpha) else null
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    this?.foreground =
            if (color == null) this?.context?.setDrawableWithColor(R.color.black)
            else ColorDrawable(color)
  } else {
    this?.alpha = 0.7f
  }
  this?.isEnabled = false
}
// 一般在 Adapter 中设置了 setViewUnEnabled()，就要调用此方法，容错处理
// 或者使用 setViewForeDrawable() 方法
fun View?.setViewEnabled() {
  val backgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    when (this?.background) {
      is GradientDrawable -> {
        (this.background as? GradientDrawable)?.color?.defaultColor
      }
      is ColorDrawable -> {
        (this.background as? ColorDrawable)?.color
      }
      else -> null
    }
  } else null
  val foregroundColor = if (backgroundColor != null) shallowColorWithAlpha(backgroundColor) else null
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    this?.foreground = if (foregroundColor == null) foregroundColor else null
  } else {
    this?.alpha = 1f
  }
  this?.isEnabled = true
}