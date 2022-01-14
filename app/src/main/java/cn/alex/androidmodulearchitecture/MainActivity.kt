package cn.alex.androidmodulearchitecture

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.alex.androidmodulearchitecture.databinding.ActivityMainBinding
import cn.alex.basemodule.BaseMainActivity
import cn.alex.basemodule.inflate
import cn.alex.basemodule.initViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // val binding = ActivityMainBinding.inflate(layoutInflater)
        // setContentView(binding.root)


        binding?.btnLogin?.setOnClickListener {
            Log.d("alexxx", "按钮点击")

            ARouter.getInstance()
                .build("/login/main/page")
                .withString("url", "http://www.baidu.com")
                .navigation()
        }

        binding?.btnMine?.setOnClickListener {
            Log.d("alexxx", "按钮点击")

            ARouter.getInstance()
                .build("/mine/main/page")
                .withString("url", "http://www.baidu.com")
                .navigation()
        }
    }
}
