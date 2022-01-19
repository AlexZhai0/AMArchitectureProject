package cn.alex.commonmodule.router.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.alex.basemodule.container.ContainerActivity
import cn.alex.basemodule.utils.LOG_MARK
import cn.alex.basemodule.utils.hasValue
import cn.alex.basemodule.utils.logE
import cn.alex.basemodule.utils.value
import cn.alex.commonmodule.router.data.RemoteUrlData
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_XYQB
import cn.alex.commonmodule.router.manager.ARouterPathManager.NATIVE_FLUTTER
import cn.alex.commonmodule.router.manager.ARouterPathManager.ROUTER_URL
import cn.alex.commonmodule.router.manager.ARouterPathManager.ROUTER_URL_S
import cn.alex.commonmodule.router.manager.findLocalPathWithRemotePath
import cn.alex.commonmodule.router.module.AppModuleRouter
import cn.alex.commonmodule.router.module.LoginModuleRouter
import cn.alex.commonmodule.sp.SharedPrefsCommon
import com.alibaba.android.arouter.facade.enums.RouteType
import com.alibaba.android.arouter.launcher.ARouter
import java.net.URLDecoder
import java.util.regex.Matcher
import java.util.regex.Pattern

object RouterNavigation {

    /**
     * 使用页面路径打开对应页面
     * 路径包括：xyqb://..、/login/..
     */
    fun openPageWithUrl(
        context: Context?,
        navUrl: String?,
        params: Map<String, String>? = null,
        bundle: Bundle? = null,
        needLogin: Boolean = false
    ) {
        if (context == null || !navUrl.hasValue()) return
        val myParams = HashMap<String, String>()
        params?.value { myParams.putAll(it) }
        val remoteUrlData = parseUrl(navUrl!!)
        val localPath = when {
            (remoteUrlData?.isNeedLogin.hasValue() || needLogin) && !SharedPrefsCommon.isLogin -> {
                // 没登录时，判断是否需要登录，需要就跳转到登录页
                LoginModuleRouter.RM_LOGIN_P
            }
            // Web 页
            navUrl.startsWith("/") -> {
                // 本地跳转
                navUrl
            }
            // Web 页
            navUrl.startsWith(ROUTER_URL) -> {
                // WebView 页面
                myParams["turl"] = navUrl
                AppModuleRouter.RM_APP_WEB
            }
            else -> findLocalPathWithRemotePath(remoteUrlData?.path)
        }
        if (!localPath.hasValue()) {
            // TODO 本地没有匹配的路径，显示错误页面
            return
        }
        val postCard = ARouter.getInstance().build(localPath)
        // 远程路径中的参数
        remoteUrlData?.params.value {
            for (p in it) {
                postCard.withString(p.key, p.value)
            }
        }
        // 本地传递参数
        for (p in myParams) {
            postCard.withString(p.key, p.value)
        }
        bundle?.value {
            postCard.with(bundle)
        }
        postCard.navigation()

        // 单独解析 Fragment
        if (postCard.type == RouteType.FRAGMENT) {
            // frag 已经是 Fragment 实例，怎么使用 Activity 打开而不内存泄漏？
            val frag = postCard.navigation() as? Fragment
            if (!frag.hasValue()) return
            val b = if (bundle.hasValue()) {
                bundle
            } else {
                Bundle()
            }
            for (p in myParams) {
                b?.putString(p.key, p.value)
            }
            ContainerActivity.startWithFragName(context, frag!!.javaClass.name, b)
        }
    }

    /**
     * 解析 Url，获取 Host、Path、Params
     */
    private fun parseUrl(sourceUrl: String?, decode: Boolean = true): RemoteUrlData? {
        // 如果 url 为空，不解析
        if (!sourceUrl.hasValue()) return null
        // 默认解码 Url
        val url = if (decode) URLDecoder.decode(sourceUrl, "UTF-8") else sourceUrl!!
        val matcher: Matcher? = when {
            url.startsWith(ROUTER_URL) ->
                Pattern.compile("($ROUTER_URL|$ROUTER_URL_S)://([^?]+)\\??(.+)?").matcher(url)
            url.startsWith(NATIVE_XYQB) ->
                Pattern.compile("($NATIVE_XYQB)://([^?]+)\\??(.+)?").matcher(url)
            url.startsWith(NATIVE_FLUTTER) ->
                Pattern.compile("($NATIVE_FLUTTER)://([^?]+)\\??(.+)?").matcher(url)
            else -> null
        }
        // 如果没有匹配成功，说明不是应用内部的页面
        if (!matcher.hasValue() || !matcher!!.find()) return null
        val host = matcher.group(1) ?: ""
        val path = matcher.group(2) ?: ""
        val param = matcher.group(3) ?: ""
        val params = getSchemaParams(param)
        val nLogin = params["needLogin"]
        val needLogin = nLogin == "1" || nLogin == "true"
        return RemoteUrlData(url, host, path, params, needLogin)
    }

    private fun getSchemaParams(paramValue: String?): MutableMap<String, String> {
        val items = mutableMapOf<String, String>()
        if (!paramValue.hasValue()) return items
        val paramPattern = Pattern.compile("(\\w+)=([^=&]+)&?")
        val matcher1 = paramPattern.matcher(paramValue!!)
        while (matcher1.find()) {
            try {
                val key = matcher1.group(1)
                val value = matcher1.group(2)
                if (key.hasValue() && value.hasValue()) {
                    items[key!!] = value!!
                }
            } catch (e: Exception) {
                logE(LOG_MARK, e.message ?: "")
            }
        }
        //自然排序
        return items.toSortedMap()
    }
}
