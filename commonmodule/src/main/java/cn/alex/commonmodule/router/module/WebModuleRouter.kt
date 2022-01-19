package cn.alex.commonmodule.router.module

import cn.alex.commonmodule.router.manager.ModuleRouterItem

class WebModuleRouter: ModuleRouterItem() {

    companion object {
        // 主 Module
        private const val RM_WEB = "/web/"
        const val RM_WEB_ACT = RM_WEB + "webAct"
    }

    init {
        routerItem {
            path = RM_WEB_ACT
            className = "WebActivity"
            classTitle = "WebView页面"
            param = "turl"
        }
    }
}