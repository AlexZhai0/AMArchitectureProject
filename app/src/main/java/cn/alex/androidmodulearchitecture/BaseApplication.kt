package cn.alex.androidmodulearchitecture
import android.app.Application
import cn.alex.androidmodulearchitecture.BuildConfig.DEBUG
import com.alibaba.android.arouter.launcher.ARouter

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (DEBUG) {
            ARouter.openLog()//开启日志
            ARouter.openDebug()//开启debug模式
        }
        ARouter.init(this)

    }
}