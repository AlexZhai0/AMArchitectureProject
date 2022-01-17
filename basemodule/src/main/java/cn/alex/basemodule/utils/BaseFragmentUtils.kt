package cn.alex.basemodule.utils

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory

/**
 * 通过当前 View 中找到其所在的 Fragment
 */
fun View?.findCurrentFragment(): Fragment? {
    val activity = this?.context?.getActivity()
    return if (activity is FragmentActivity) {
        findSupportFragment(this, activity)
    } else null
}

private val tempViewToSupportFragment by lazy { ArrayMap<View, Fragment>() }
private fun findSupportFragment(
    target: View?,
    activity: FragmentActivity
): Fragment? {
    tempViewToSupportFragment.clear()
    findAllSupportFragmentsWithViews(
        activity.supportFragmentManager.fragments, tempViewToSupportFragment
    )
    var result: Fragment? = null
    val activityRoot = activity.findViewById<View>(android.R.id.content)
    var current = target
    while (current != activityRoot) {
        result = tempViewToSupportFragment[current]
        if (result != null) {
            break
        }
        current = if (current?.parent is View) {
            current.parent as View
        } else {
            break
        }
    }
    tempViewToSupportFragment.clear()
    return result
}

private fun findAllSupportFragmentsWithViews(
    topLevelFragments: Collection<Fragment>?,
    result: MutableMap<View?, Fragment>
) {
    if (topLevelFragments == null) {
        return
    }
    for (fragment in topLevelFragments) {
        if (fragment.view == null) {
            continue
        }
        result[fragment.view] = fragment
        findAllSupportFragmentsWithViews(fragment.childFragmentManager.fragments, result)
    }
}

/**
 * 通过 Fragment 的全包名，反射拿到 Fragment 对象。
 * 参考：Fragment#instantiate()
 */
fun instantiateFragment(context: Context, name: String, args: Bundle? = null): Fragment? {
    return try {
        val clazz = FragmentFactory.loadFragmentClass(context.classLoader, name)
        val f = clazz.getConstructor().newInstance()
        if (args != null) {
            args.classLoader = f.javaClass.classLoader
            f.arguments = args
        }
        f
    } catch (e: Exception) {
        logE(LOG_MARK, e.printStackTrace().toString() + "初始化 Fragment 失败")
        null
    }
}
