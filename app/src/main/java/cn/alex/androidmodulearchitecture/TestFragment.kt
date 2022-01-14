package cn.alex.androidmodulearchitecture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.alex.androidmodulearchitecture.databinding.FragTestBinding
import cn.alex.basemodule.initViewBinding
import kotlin.properties.ReadOnlyProperty

class TestFragment: Fragment() {

    var binding: FragTestBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = FragTestBinding.inflate(inflater, container, false)
        binding = initViewBinding(container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.testBtn?.setOnClickListener {
            Log.d("alexxx", "TestFragment 按钮点击")
        }

//        ReadOnlyProperty

    }

}