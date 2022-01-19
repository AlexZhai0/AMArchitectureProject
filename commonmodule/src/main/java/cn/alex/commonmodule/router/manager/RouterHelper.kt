package cn.alex.commonmodule.router.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.alex.basemodule.utils.hasValue
import cn.alex.basemodule.utils.value
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_XYQB
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_FLUTTER
import cn.alex.commonmodule.router.manager.ARouterPathManager.ROUTER_URL
import cn.alex.commonmodule.router.manager.ARouterPathManager.ROUTER_URL_S
import com.alibaba.android.arouter.launcher.ARouter
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * 通过远程路由（xyqb://...）找到本地路由地址（/path/...）
 */
fun findLocalPathWithRemotePath(remotePath: String?): String? {
    if (!remotePath.hasValue()) return null
    val schemaData = RouterConfiguration.requestItems.find {
        // 解析本地 ARouter 路径，找到其 Path 内容，与服务端返回的路径比较
        val aRouteMatcher = it.path?.aRouterMatch()
        val localPath = if (aRouteMatcher?.find() == true) {
            aRouteMatcher.group(2) ?: ""
        } else ""
        localPath == remotePath
    }
    return schemaData?.path
}

/**
 * 拿到 Fragment 对象
 */
fun getFragmentWithLocalPath(
    localPath: String?,
    params: Map<String, String>? = null,
    bundle: Bundle? = null,
): Fragment? {
    if (!localPath.hasValue() || !localPath!!.startsWith("/")) return null
    val myParams = HashMap<String, String>()
    params?.value { myParams.putAll(it) }
    val postCard = ARouter.getInstance().build(localPath)
    // 本地传递参数
    for (p in myParams) {
        postCard.withString(p.key, p.value)
    }
    bundle?.value {
        postCard.with(bundle)
    }
    return postCard.navigation() as? Fragment
}


/**
 * 从服务端返回的 Schema 中找到本地 ARouter 路径，用 ARouter 跳转
 */
fun getSchemaPathFromARouterPath(schemaPath: String?): String {
    if (!schemaPath.hasValue()) return ""
    val remoteUrlMatcher: Matcher? = when {
        schemaPath!!.startsWith(ROUTER_URL) -> schemaPath.matchHttp()
        schemaPath.startsWith(NATIVE_XYQB) -> schemaPath.matchXyqb()
        schemaPath.startsWith(NATIVE_FLUTTER) -> schemaPath.matchFlutter()
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
 * https://、http://
 * 解析 http、https 后面的内容
 */
fun String.matchHttp(): Matcher? {
    return Pattern.compile("($ROUTER_URL|$ROUTER_URL_S)://([^?]+)\\??(.+)?").matcher(this)
}

/**
 * xyqb://
 * 解析 xyqb 后面的内容
 */
fun String.matchXyqb(): Matcher? {
    return Pattern.compile("($NATIVE_XYQB)://([^?]+)\\??(.+)?").matcher(this)
}

/**
 * flutter://
 * 解析 flutter 后面的内容
 */
fun String.matchFlutter(): Matcher? {
    return Pattern.compile("($NATIVE_FLUTTER)://([^?]+)\\??(.+)?").matcher(this)
}
