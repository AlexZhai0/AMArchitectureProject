package cn.alex.loginmodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import cn.alex.basemodule.inflate
import cn.alex.basemodule.utils.*
import cn.alex.commonmodule.login.ActionManager
import cn.alex.commonmodule.router.module.LoginModuleRouter
import cn.alex.loginmodule.databinding.ActivityLoginMainBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = LoginModuleRouter.RM_LOGIN_P)
class LoginMainActivity : AppCompatActivity() {

    private val binding: ActivityLoginMainBinding? by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val extra = intent?.getStringExtra("url11")
        val extra2 = intent?.getStringExtra("kkk")
        binding?.contentTitle?.text = "LoginMainActivity: $extra, $extra2"

        val color = appContext?.setColor(R.color.black)

        val colorss23 = getFloat(R.dimen.dialog_bg_color)

//        toast("$colorss23")

        val bundle = Bundle()
        bundle.putString("key123", "valu222")
        bundle.putString("key666", "valu8")
        bundle.putString("key888", "valu27")

        // 登录成功后自动执行下一步
        ui(2000) {
            val actionItems = ActionManager.getActionItemList(ActionManager.ACTION_LOGIN)
            if (actionItems.isNotEmpty()) {
                //场景:登录完成后,跳转到首页
                //事件惦挂处理,事件登录成功后执行
                //授权,授信完成后,回退到首页
                for (actionItem in actionItems) {
                    val loginActionItem = actionItem as ActionManager.LoginActionItem
                    //执行事件
                    loginActionItem.runPreAction()
                    loginActionItem.action.invoke()
                    //执行后置事件
                    loginActionItem.runPostAction()
                }
            }
        }

        binding?.btnMine?.setOnClickListener {
            ARouter.getInstance()
                .build("/mine/main/page")
                .with(bundle)
                .withString("url", "来自 LoginMainActivity")
                .navigation()
        }
    }
}