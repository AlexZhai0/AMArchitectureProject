package cn.alex.commonmodule.sp

import cn.alex.basemodule.utils.hasValue
import cn.alex.commonmodule.constants.Constant

/**
 * 整个应用公用的 SharedPrefs
 */
object SharedPrefsCommon : BaseSharedPrefs(Constant.SP_COMMON_FILE_NAME) {

    val isLogin: Boolean
        get() {
            return appToken.hasValue()
        }

    var appToken: String by Preference(sharedPrefs, "app_token", DEFAULT_SP_STRING)
}