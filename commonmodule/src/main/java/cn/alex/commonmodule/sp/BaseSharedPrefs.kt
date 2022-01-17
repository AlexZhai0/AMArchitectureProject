package cn.alex.commonmodule.sp

import com.tencent.mmkv.MMKV

abstract class BaseSharedPrefs(private val preferenceName: String) {

    companion object {
        const val DEFAULT_SP_STRING = ""
    }

    val sharedPrefs: MMKV by lazy {
        MMKV.mmkvWithID("${preferenceName}_mmkv")
    }

}