package cn.alex.basemodule

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.Exception

inline fun <reified VB : ViewBinding> Activity?.inflate() = lazy {
    initViewBinding<VB>()
}

inline fun <reified VB : ViewBinding> Activity?.initViewBinding(): VB? {
    if (this == null) return null
    val binding = try {
        val clazz = VB::class.java
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as VB
    } catch (e: Exception) {
        null
    }
    binding?.let { setContentView(it.root) }
    return binding
}


inline fun <reified VB : ViewBinding> Fragment?.initViewBinding(view: ViewGroup?): VB? {
    if (this == null) return null
    val binding = try {
        val clazz = VB::class.java
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater, view, false) as VB
    } catch (e: Exception) {
        null
    }
    return binding
}