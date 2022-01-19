package cn.alex.commonmodule.router.manager

import cn.alex.commonmodule.router.module.AppModuleRouter
import cn.alex.commonmodule.router.module.LoginModuleRouter
import cn.alex.commonmodule.router.module.MineModuleRouter
import cn.alex.commonmodule.router.module.WebModuleRouter

/**
 * 帮助 ARouter 结合 Schema 控制路由跳转
 */
object ARouterPathManager {
    const val ROUTER_URL = "http"
    const val ROUTER_URL_S = "https"
    const val NATIVE_XYQB = "xyqb"
    const val NATIVE_FLUTTER = "flutter"

    /**
     * 初始化所有页面 ARouter 路径，并添加到 List 中
     */
    fun registerModulePath() {
        RouterConfiguration.register(AppModuleRouter())
        RouterConfiguration.register(LoginModuleRouter())
        RouterConfiguration.register(MineModuleRouter())
        RouterConfiguration.register(WebModuleRouter())
    }
}