package cn.alex.commonmodule.router.manager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import cn.alex.basemodule.container.ContainerActivity
import cn.alex.basemodule.utils.hasValue
import cn.alex.basemodule.utils.value
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_ITEM
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_ITEM_FLUTTER
import cn.alex.commonmodule.router.manager.ARouterPathManager.URL_ITEM
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.launcher.ARouter
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 从服务端返回的 Schema 中找到本地 ARouter 路径，用 ARouter 跳转
 */
fun getSchemaPathFromARouterPath(schemaPath: String?): String {
    if (!schemaPath.hasValue()) return ""
    val remoteUrlMatcher: Matcher? = when {
        schemaPath!!.startsWith(URL_ITEM) -> schemaPath.matchHttp()
        schemaPath.startsWith(NATIVE_ITEM) -> schemaPath.matchXyqb()
        schemaPath.startsWith(NATIVE_ITEM_FLUTTER) -> schemaPath.matchFlutter()
        else -> null
    }

    if (remoteUrlMatcher?.find() == true) {
        val schemaData = RouterConfiguration.requestItems.find {
            // 解析本地 ARouter 路径，找到其 Path 内容，与服务端返回的路径比较
            val aRouteMatcher = it.path?.aRouterMatch()
            val localPath = if (aRouteMatcher?.find() == true) {
                aRouteMatcher.group(2) ?: ""
            } else ""
            localPath == remoteUrlMatcher.group(2)
        }
        return schemaData?.path ?: ""
    }
    return ""
}

/**
 * 解析 ARouter 路径，例如：/aa/bb/ccc，结果是 bb/ccc
 */
fun String.aRouterMatch(): Matcher? {
    return Pattern.compile("/([a-z0-9A-Z]+)/(.+)?").matcher(this)
}

/**
 * https://
 * 解析 http、https 后面的内容
 */
fun String.matchHttp(): Matcher? {
    return Pattern.compile("(http|https)://([^?]+)\\??(.+)?").matcher(this)
}

/**
 * xyqb://
 * 解析 xyqb 后面的内容
 */
fun String.matchXyqb(): Matcher? {
    return Pattern.compile("(xyqb)://([^?]+)\\??(.+)?").matcher(this)
}

/**
 * flutter://
 * 解析 flutter 后面的内容
 */
fun String.matchFlutter(): Matcher? {
    return Pattern.compile("(flutter)://([^?]+)\\??(.+)?").matcher(this)
}

/**
 * 使用页面路径打开对应页面
 * 路径包括：xyqb://、/login/..
 */
fun aRouterNavigation(context: Context?, navUrl: String?, params: Map<String, String>? = null) {
    if (context == null || !navUrl.hasValue()) return
    val localPath = if (navUrl!!.startsWith("/")) {
        navUrl
    } else {
        getSchemaPathFromARouterPath(navUrl)
    }
    if (!localPath.hasValue()) return
    val postCard = ARouter.getInstance().build(localPath)
    params.value {
        for (p in it) {
            postCard.withString(p.key, p.value)
        }
    }
    postCard.navigation()
    if (postCard.type == RouteType.FRAGMENT) {
        // TODO frag 已经是 Fragment 实例，怎么使用 ContainerActivity 打开？
        val frag = postCard.navigation() as? Fragment
        if (!frag.hasValue()) return
        val bundle = Bundle()
        params.value {
            for (p in it) {
                bundle.putString(p.key, p.value)
            }
        }
        ContainerActivity.startWithFragName(context, frag!!.javaClass.name, bundle)
    }
}