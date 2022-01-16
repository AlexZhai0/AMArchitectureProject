package cn.alex.basemodule.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import cn.alex.basemodule.R
import cn.alex.basemodule.utils.hasValue
import cn.alex.basemodule.utils.instantiateFragment

/**
 * 专门用来装载 Fragment 的 Activity。
 * 必须用到 Activity 的地方用继承 BaseActivity 的方式（待补充）：
 * 1、Fragment 页面需要监听判断返回键
 *
 * 使用：
 * ContainerActivity.startWithName(context, XXFragment::class.java.name,
 * Bundle().apply { putString("no_xxxx", "xxxx") })
 */
//@AndroidEntryPoint
//class ContainerActivity : BaseBackActivity() {
class ContainerActivity : FragmentActivity() {

    companion object {

        private const val PAGE_FRAG_NAME = "page_frag_name"

        /**
         * 如果直接传递 Fragment，会使用静态变量，这样会造成内存泄漏
         * 使用 Fragment 全路径名称，然后反射拿到当前 Fragment 对象，避免内存泄漏
         */
        fun startWithFragName(context: Context, fragmentName: String, args: Bundle? = null) {
            context.startActivity(Intent(context, ContainerActivity::class.java).apply {
                putExtra(PAGE_FRAG_NAME, fragmentName)
                args?.let { putExtras(it) }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_activity_container)
        val fragFullName = intent.getStringExtra(PAGE_FRAG_NAME)
        if (fragFullName.hasValue()) {
            val args = intent.extras
            val fragment = instantiateFragment(this, fragFullName!!, args)
            fragment?.let {
                if (it.isAdded) {
                    supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
                }
                supportFragmentManager.beginTransaction().add(R.id.base_container, it).commitAllowingStateLoss()
            }
        } else {
//            toast("页面不存在")
        }
    }
}