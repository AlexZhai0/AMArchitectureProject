package cn.alex.commonmodule.router.manager

import cn.alex.commonmodule.router.data.SchemaItemData

open class ModuleRouterItem {

    val moduleItems = mutableListOf<SchemaItemData>()

    fun routerItem(closure: SchemaItemData.() -> Unit) {
        moduleItems.add(SchemaItemData().apply(closure))
    }
}