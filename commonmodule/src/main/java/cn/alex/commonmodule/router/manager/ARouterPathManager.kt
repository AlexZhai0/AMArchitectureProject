package cn.alex.commonmodule.router.manager

import cn.alex.commonmodule.router.module.AppModuleRouter
import cn.alex.commonmodule.router.module.LoginModuleRouter
import java.util.regex.Pattern

/**
 * 帮助 ARouter 结合 Schema 控制路由跳转
 */
object ARouterPathManager {
    const val URL_ITEM = "http"
    const val NATIVE_ITEM = "xyqb"
    const val NATIVE_ITEM_VCC = "vcczxh"
    const val NATIVE_ITEM_FLUTTER = "flutter"

    val items = mutableListOf<SchemaItemData>()

    fun routerItem(closure: SchemaItemData.() -> Unit) {
        val item = SchemaItemData().apply(closure)
        if (item.path == null) return
        val matcher = Pattern.compile("([^?]+)\\??(.+)?").matcher(StringBuilder(item.path!!))
        if (matcher.find()) {
            item.path = matcher.group(1)
        }
        items.add(item)
    }

    // 初始化所有页面路径，并添加到 List 中
    init {
        AppModuleRouter()
        LoginModuleRouter()
    }
}