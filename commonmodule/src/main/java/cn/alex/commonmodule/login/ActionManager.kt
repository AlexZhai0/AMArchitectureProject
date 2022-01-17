package cn.alex.commonmodule.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import cn.alex.commonmodule.router.manager.aRouterNavigation
import cn.alex.commonmodule.router.module.LoginModuleRouter
import cn.alex.commonmodule.sp.SharedPrefsCommon

/**
 * Created by cz on 10/27/16.
 */
object ActionManager {
    const val ACTION_LOGIN = 1
    val actionItems = mutableMapOf<Int, MutableList<ActionItem>>()
    var size: Int = 0
        get() = actionItems.size


    fun getActionItemList(type: Int): List<ActionItem> {
        return actionItems.getOrPut(type) { mutableListOf<ActionItem>() }
    }

    fun remove(type: Int): List<ActionItem> {
        val items = actionItems.getOrPut(type) { mutableListOf<ActionItem>() }
        actionItems.remove(type)
        return items
    }

    fun addActionItem(type: Int, actionItem: ActionManager.ActionItem) {
        var actions =
            ActionManager.actionItems.getOrPut(type) { mutableListOf<ActionManager.ActionItem>() }
        actions.add(actionItem)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getAction(type: Int): MutableList<ActionItem> {
        return actionItems.getOrDefault(type, mutableListOf<ActionItem>())
    }

    open class ActionItem(var action: () -> Unit)

    class LoginActionItem(action: () -> Unit) : ActionItem(action) {
        companion object {
            fun create(action: () -> Unit): LoginActionItem {
                // val item=LoginActionItem(action)
                // item.postAction={ActivityManagers.removeLastTo(MainActivity::class.java)}
                return LoginActionItem(action)
            }
        }

        var extras: Bundle? = null
        var preAction: (() -> Unit)? = null
        var postAction: (() -> Unit)? = null

        fun runPreAction() = preAction?.invoke()

        fun runPostAction() = postAction?.invoke()
    }
}

class LoginItem {
    internal lateinit var action: () -> Unit
    var extras: Bundle? = null
    var preAction: (() -> Unit)? = null
    var postAction: (() -> Unit)? = null
    fun action(action: () -> Unit) {
        this.action = action
    }

    fun preAction(preAction: () -> Unit) {
        this.preAction = preAction
    }

    fun postAction(postAction: () -> Unit) {
        this.postAction = postAction
    }
}

fun View.onLoginClick(action: () -> Unit) {
    setOnClickListener(BlockViewClickListener({
        addLoginAction(context, ActionManager.LoginActionItem.create(action))
    }))
}

//验证用户是否登录的fragment操作
fun Fragment.loginAction(action: () -> Unit) {
    addLoginAction(context, ActionManager.LoginActionItem.create(action))
}

/**
 * 检测是否需要登录,如果需要登录则跳转登录,不需要执行事件
 */
fun checkLoginAction(context: Context, action: () -> Unit) {
    if (!SharedPrefsCommon.isLogin) {
        aRouterNavigation(context, LoginModuleRouter.RM_LOGIN_P)
    } else {
        action()
    }
}

fun loginActionItem(context: Context?, action: LoginItem.() -> Unit) {
    val actionItem = LoginItem().apply(action)
    val loginActionItem = ActionManager.LoginActionItem(actionItem.action)
    loginActionItem.extras = actionItem.extras
    loginActionItem.preAction = actionItem.preAction
    loginActionItem.postAction = actionItem.postAction
    addLoginAction(context, loginActionItem)
}

/**
 * 通过条件判断是否要登录
 */
fun Context.loginActionWithCondition(needLogin: Boolean?, action: () -> Unit) {
    if (needLogin == true) {
        loginAction(action)
    } else {
        action.invoke()
    }
}

/**
 * 需要登录
 */
fun Context.loginAction(action: () -> Unit) {
    addLoginAction(this, ActionManager.LoginActionItem.create(action))
}

class OnLoginClickListener(val listener: View.OnClickListener) : View.OnClickListener by listener {
    override fun onClick(v: View) {
        addLoginAction(v.context, ActionManager.LoginActionItem.create { listener.onClick(v) })
    }
}

private fun addLoginAction(context: Context?, actionItem: ActionManager.LoginActionItem) {
    ActionManager.remove(ActionManager.ACTION_LOGIN)
    if (!SharedPrefsCommon.isLogin) {
        aRouterNavigation(context, LoginModuleRouter.RM_LOGIN_P, bundle = actionItem.extras)
        ActionManager.addActionItem(ActionManager.ACTION_LOGIN, actionItem)
    } else actionItem.action.invoke()
}


