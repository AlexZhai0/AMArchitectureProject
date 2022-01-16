package cn.alex.basemodule.utils

import android.content.res.Resources
import androidx.annotation.DimenRes
import cn.alex.basemodule.R


val density = Resources.getSystem().displayMetrics.density
val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
// 10.dp、0.5.dp
val Number.dp: Int
    get() {
        val currentNum = toFloat() * density
        return if (currentNum <= 1) 1 else currentNum.toInt()
    }
// 10.sp、0.5.sp
val Number.sp: Int
    get() {
        val currentNum = toFloat() * scaledDensity
        return if (currentNum <= 1) 1 else currentNum.toInt()
    }

// 屏幕宽度
val width get() = Resources.getSystem().displayMetrics.widthPixels
// 屏幕高度：不包括虚拟键和刘海高度
val height get() = Resources.getSystem().displayMetrics.heightPixels
// 真是的屏幕高度
val heightReal get() = Resources.getSystem().displayMetrics.heightPixels + statusBarH + navigationBarH

// 状态栏高度
val statusBarH = getStatusBarHeight()
// 虚拟栏高度
val navigationBarH = getNavigationBarHeight()
// 获取状态栏高度
fun getStatusBarHeight(): Int {
    val resourceId = Res.getResources().getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        Res.getResources().getDimensionPixelSize(resourceId)
    } else 0
}
// 获取虚拟栏高度
fun getNavigationBarHeight(): Int {
    val resourceId = Res.getResources().getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        Res.getResources().getDimensionPixelSize(resourceId)
    } else 0
}

@Suppress("UNCHECKED_CAST")
fun <T> getFloat(@DimenRes id: Int): T? {
    return tryCatchT { Res.getFloat(id) as T }
}

// dimens
fun getDimension(@DimenRes id: Int): Int {
    return try {
        Res.getDimension(id).toInt()
    } catch (e: Exception) {
        0
    }
}
// 页面中通用的距离单位，方便使用
// 页面内容距两边边距的距离 12dp
val borderDistance = getDimension(R.dimen.border_distance)
// 页面内容之间的距离 8dp
val contentDistance = getDimension(R.dimen.content_distance)
// 控件圆角 8dp
val viewCorner = getDimension(R.dimen.view_corner)
// 从底部弹出控件圆角 20dp
val bottomViewCorner = getDimension(R.dimen.bottom_view_corner)

// Dialog 统一背景透明度
val dialogBgColor = getFloat<Float>(R.dimen.dialog_bg_color) ?: 0f