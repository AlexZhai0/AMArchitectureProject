package cn.alex.viewmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.alex.basemodule.inflate
import cn.alex.minemodule.databinding.ActivityViewMainBinding

class ViewMainActivity : AppCompatActivity() {

    private val binding: ActivityViewMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.viewModuleTitle?.text = "View Module"
    }
}