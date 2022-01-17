package cn.alex.androidmodulearchitecture.application
import cn.alex.androidmodulearchitecture.BuildConfig.DEBUG
import cn.alex.commonmodule.application.CommonApplication
import cn.alex.commonmodule.router.manager.ARouterPathManager
import com.alibaba.android.arouter.launcher.ARouter

class AppApplication: CommonApplication() {

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) {
            ARouter.openLog()//开启日志
            ARouter.openDebug()//开启debug模式
        }
        ARouter.init(this)
        ARouterPathManager.registerModulePath()

    }
}