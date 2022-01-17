package cn.alex.minemodule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.alex.basemodule.initViewBinding
import cn.alex.commonmodule.router.module.MineModuleRouter.Companion.RM_MINE_M
import cn.alex.minemodule.databinding.FragmentMineMainBinding
import com.alibaba.android.arouter.facade.annotation.Route

@Route(path = RM_MINE_M)
class MineFragment: Fragment() {

    var binding: FragmentMineMainBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val extra = arguments?.getString("url11")
        val extra2 = arguments?.getString("kkk")
        Log.e("alexxx", "参数：$extra, $extra2")
        binding = initViewBinding(container)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.contentTitle?.text = "linshi"
    }
}