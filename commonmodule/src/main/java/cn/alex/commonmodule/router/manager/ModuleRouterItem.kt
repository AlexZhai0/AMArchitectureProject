package cn.alex.commonmodule.router.manager

open class ModuleRouterItem {

    val moduleItems = mutableListOf<SchemaItemData>()

    fun routerItem(closure: SchemaItemData.() -> Unit) {
        moduleItems.add(SchemaItemData().apply(closure))
    }
}