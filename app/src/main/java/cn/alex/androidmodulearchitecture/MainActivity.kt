package cn.alex.androidmodulearchitecture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cn.alex.androidmodulearchitecture.databinding.ActivityMainBinding
import cn.alex.basemodule.inflate
import cn.alex.commonmodule.router.manager.aRouterNavigation
import cn.alex.commonmodule.router.module.MineModuleRouter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // val binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(binding.root)


        binding?.btnLogin?.setOnClickListener {
            val params = HashMap<String, String>()
            params["url11"] = "http:ssssss"
            params["kkk"] = "333333"
//            aRouterNavigation(this, LoginModuleRouter.RM_LOGIN_P, params)
            aRouterNavigation(this, "xyqb://login/main/page", params)
        }

        binding?.btnMine?.setOnClickListener {
            Log.d("alexxx", "按钮点击")
//            ARouter.getInstance()
//                .build("/mine/main/page")
//                .withString("url", "http://www.baidu.com")
//                .navigation()

            val params = HashMap<String, String>()
            params["url11"] = "00000"
            params["kkk"] = "888888"
            aRouterNavigation(this, MineModuleRouter.RM_MINE_M, params)
        }
    }
}
