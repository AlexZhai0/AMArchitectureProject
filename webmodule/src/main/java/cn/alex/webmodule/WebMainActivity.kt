package cn.alex.webmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.alex.basemodule.inflate
import cn.alex.basemodule.utils.toast
import cn.alex.commonmodule.router.module.WebModuleRouter
import cn.alex.webmodule.databinding.ActivityWebMainBinding
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = WebModuleRouter.RM_WEB_ACT)
class WebMainActivity : AppCompatActivity() {

    private val binding: ActivityWebMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.webModuleTitle?.text = "Web Module"

        val url = intent?.getStringExtra("turl")
        toast(url)
    }
}