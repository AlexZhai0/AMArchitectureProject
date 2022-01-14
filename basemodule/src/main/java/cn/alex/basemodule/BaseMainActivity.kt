package cn.alex.basemodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BaseMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_main)
    }
}