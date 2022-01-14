package cn.alex.minemodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.alex.basemodule.inflate
import cn.alex.minemodule.databinding.ActivityMineMainBinding
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = "/mine/main/page")
class MineMainActivity : AppCompatActivity() {

    private val binding: ActivityMineMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra = intent?.getStringExtra("url")
        binding?.contentTitle?.text = "MineMainActivity: $extra"
    }
}