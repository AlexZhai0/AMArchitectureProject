package cn.alex.commonmodule.router.module

import cn.alex.commonmodule.router.manager.ARouterPathManager.routerItem

class LoginModuleRouter {

    companion object {
        // 登录 Module
        private const val RM_LOGIN = "/loginModule/"
        const val RM_LOGIN_PWD = RM_LOGIN + "pwd/page"
    }

    init {
        routerItem {
            path = RM_LOGIN_PWD
            className = "LoginActivity"
            classTitle = "登录页"
            param = "phone"
        }
    }
}