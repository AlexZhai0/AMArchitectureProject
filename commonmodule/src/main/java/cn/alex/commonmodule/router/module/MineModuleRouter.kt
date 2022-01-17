package cn.alex.commonmodule.router.module

import cn.alex.commonmodule.router.manager.ModuleRouterItem

class MineModuleRouter: ModuleRouterItem() {

    companion object {
        // 我的 Module
        private const val RM_MINE = "/mineModule/"
        const val RM_MINE_M = RM_MINE + "m/page"
    }

    init {
        routerItem {
            path = RM_MINE_M
            className = "MineFragment"
            classTitle = "我的页面"
        }
    }
}