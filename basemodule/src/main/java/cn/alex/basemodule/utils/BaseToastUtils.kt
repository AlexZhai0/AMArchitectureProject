package cn.alex.basemodule.utils

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import cn.alex.basemodule.R

fun toast(@StringRes resId: Int) {
    toast(appRes?.getString(resId))
}

fun toast(@StringRes resId: Int, vararg params: Any?) {
    toast(appRes?.getString(resId, *params))
}

fun toast(text: String?) {
    showToast(text, Toast.LENGTH_SHORT)
}

fun toast(text: String?, duration: Int) {
    showToast(text, duration)
}

fun toast(@StringRes resId: Int, duration: Int) {
    showToast(appRes?.getString(resId), duration)
}

/**
 * 统一 Toast 样式提示
 */
@SuppressLint("InflateParams")
@JvmOverloads
fun showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    if (!text.hasValue() || appContext == null) return
    val layoutInflater = LayoutInflater.from(appContext)
    val toastView = layoutInflater.inflate(R.layout.base_toast_layout, null)
    val localTextView = toastView.findViewById<View>(R.id.base_toast_tv_content) as TextView
    localTextView.text = text
    val toast = Toast(appContext).apply {
        this.duration = duration
        this.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        this.view = toastView
    }
    toast.show()
}