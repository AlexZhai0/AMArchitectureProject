package cn.alex.commonmodule.router.module

import cn.alex.commonmodule.router.manager.ARouterPathManager.routerItem

class AppModuleRouter {

    companion object {
        // 主 Module
        private const val RM_APP = "/app/"
        const val RM_APP_WEB = RM_APP + "WebActivity"
    }

    init {
        routerItem {
            path = RM_APP_WEB
            className = "WebBrowserActivity"
            classTitle = "WebView页面"
        }
    }
}