package cn.alex.minemodule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import cn.alex.basemodule.initViewBinding
import cn.alex.basemodule.utils.toast
import cn.alex.basemodule.utils.ui
import cn.alex.commonmodule.login.ActionManager
import cn.alex.commonmodule.login.onBlockClick
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
//        binding = initViewBinding(container)
        val view = inflater.inflate(R.layout.fragment_mine_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val extra = arguments?.getString("url11")
        val extra2 = arguments?.getString("kkk")
        Log.e("alexxx", "参数：$extra, $extra2")
        toast("参数：$extra, $extra2")

        val textView = view.findViewById<TextView>(R.id.content_title)

//        binding?.contentTitle?.text = "linshi"
    }
}