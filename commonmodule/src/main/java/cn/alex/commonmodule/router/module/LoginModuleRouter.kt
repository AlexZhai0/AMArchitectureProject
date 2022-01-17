package cn.alex.commonmodule.router.module

import cn.alex.commonmodule.router.manager.ModuleRouterItem

class LoginModuleRouter: ModuleRouterItem() {

    companion object {
        // 登录 Module
        private const val RM_LOGIN = "/loginModule/"
        const val RM_LOGIN_P = RM_LOGIN + "login/main/page"
    }

    init {
        routerItem {
            path = RM_LOGIN_P
            className = "LoginMainActivity"
            classTitle = "登录页"
            param = "phone"
        }
    }
}