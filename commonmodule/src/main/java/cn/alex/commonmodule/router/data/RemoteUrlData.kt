package cn.alex.commonmodule.router.data

/**
 * 解析 Url
 */
data class RemoteUrlData(
    val url: String,
    val host: String,
    val path: String,
    val params: MutableMap<String, String> = mutableMapOf(),
    // 1、true 需要登录，0、false 不需要登录
    val isNeedLogin: Boolean = false,
)