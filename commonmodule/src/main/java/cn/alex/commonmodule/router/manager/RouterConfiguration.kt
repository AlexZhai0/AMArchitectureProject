package cn.alex.commonmodule.router.manager

/**
 * 本地页面路由配置汇总
 */
object RouterConfiguration {

    val requestItems = mutableListOf<SchemaItemData>()

    fun register(item: ModuleRouterItem) {
        requestItems.addAll(item.moduleItems)
    }

    //重载运算符 get 可在使用RequestManager["action"]获取请求条目
    operator fun get(path: String): SchemaItemData? = requestItems.find { it.path == path }

}