package cn.alex.loginmodule.sp

import cn.alex.commonmodule.constants.Constant
import cn.alex.commonmodule.sp.BaseSharedPrefs
import cn.alex.commonmodule.sp.Preference

/**
 * 登录模块的 SharedPrefs
 */
object SharedPrefsLogin : BaseSharedPrefs(Constant.SP_COMMON_FILE_NAME) {

    var userId: String by Preference(sharedPrefs, "login_user_id", DEFAULT_SP_STRING)
}