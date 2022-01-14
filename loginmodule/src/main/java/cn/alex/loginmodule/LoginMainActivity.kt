package cn.alex.loginmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.alex.basemodule.inflate
import cn.alex.loginmodule.databinding.ActivityLoginMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/login/main/page")
class LoginMainActivity : AppCompatActivity() {

    private val binding: ActivityLoginMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra = intent?.getStringExtra("url")
        binding?.contentTitle?.text = "LoginMainActivity: $extra"
        binding?.btnMine?.setOnClickListener {
            ARouter.getInstance()
                .build("/mine/main/page")
                .withString("url", "来自 LoginMainActivity")
                .navigation()
        }
    }
}