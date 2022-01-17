package cn.alex.loginmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.alex.basemodule.inflate
import cn.alex.commonmodule.router.module.LoginModuleRouter
import cn.alex.loginmodule.databinding.ActivityLoginMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = LoginModuleRouter.RM_LOGIN_P)
class LoginMainActivity : AppCompatActivity() {

    private val binding: ActivityLoginMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra = intent?.getStringExtra("url11")
        val extra2 = intent?.getStringExtra("kkk")
        binding?.contentTitle?.text = "LoginMainActivity: $extra, $extra2"
        binding?.btnMine?.setOnClickListener {
            ARouter.getInstance()
                .build("/mine/main/page")
                .withString("url", "来自 LoginMainActivity")
                .navigation()
        }
    }
}