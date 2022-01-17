package cn.alex.commonmodule.application

import cn.alex.basemodule.application.BaseApplication
import cn.alex.commonmodule.BuildConfig.DEBUG
import cn.alex.commonmodule.router.manager.ARouterPathManager
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.mmkv.MMKV

open class CommonApplication: BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        // 初始化 ARouter
        if (DEBUG) {
            ARouter.openLog()//开启日志
            ARouter.openDebug()//开启debug模式
        }
        ARouter.init(this)
        ARouterPathManager.registerModulePath()

        // 初始化 MMKV
        MMKV.initialize(this)
    }
}