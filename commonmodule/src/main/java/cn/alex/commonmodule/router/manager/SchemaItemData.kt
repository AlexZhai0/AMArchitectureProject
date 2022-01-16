package cn.alex.commonmodule.router.manager

/**
 * 页面路由实体
 */
data class SchemaItemData(
        // 目标页面的 ARouter 路径
        var path: String? = null,

        // 目标页面的名称，仅为注释
        var className: String? = null,
        // 目标页面的标题，仅为注释
        var classTitle: String? = null,
        // 参数在目标页面接收即可，Schema 中会把参数添加进去，这里的字段仅为注释
        var param: String? = null
)
