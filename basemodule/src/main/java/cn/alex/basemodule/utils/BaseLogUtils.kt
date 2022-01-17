package cn.alex.basemodule.utils

import android.app.Activity
import android.util.Log
import androidx.fragment.app.Fragment
import cn.alex.basemodule.BuildConfig

// 是否显示 Log，在测试数据的时候打开
val isShowLog = BuildConfig.DEBUG

// Log 中的 tag 后缀，方便在几个页面测试时过滤 Log
const val LOG_MARK = "-Mark"

// =======  D  =======
fun Activity?.logI(msg: String) {
    if (!isShowLog) return
    log(this?.javaClass?.simpleName, msg)
}

fun Fragment?.logI(msg: String) {
    if (!isShowLog) return
    log(this?.javaClass?.simpleName, msg)
}

fun Any?.logI(msg: String) {
    if (!isShowLog) return
    log(this?.javaClass?.simpleName, msg)
}

fun logI(tag: String?, msg: String?) {
    if (!isShowLog) return
    log(tag, msg)
}

fun logE(tag: String, message: String) {
    if (!BuildConfig.DEBUG) return
    log(tag, message, 1)
}

fun log(tag: String?, msg: String?, tagType: Int = 0) {
    logInfo(tag + LOG_MARK, msg, tagType)
}

// 防止编译器的控制台有单行日志字数限制，输出完整 Log
private fun logInfo(tag: String?, msg: String?, tagType: Int = 0) {
    try {
        if (msg != null) {
            val bytes = msg.toByteArray()
            val length = bytes.size
            if (length <= 4000) {
                realLogI(tag, msg, tagType)
            } else {
                var index = 0
                while (index < length - 4000) {
                    val lastIndexOfLF = lastIndexOfLF(bytes, index)
                    val chunkLength = lastIndexOfLF - index
                    realLogI(tag, String(bytes, index, chunkLength), tagType)
                    index = if (chunkLength < 4000) {
                        lastIndexOfLF + 1
                    } else {
                        lastIndexOfLF
                    }
                }
                if (length > index) {
                    realLogI(tag, String(bytes, index, length - index), tagType)
                }
            }
        } else {
            realLogI(tag, null as String?, tagType)
        }
    } catch (var8: Exception) {
        realLogI(tag, var8.stackTrace.toString(), 1)
    }
}

private fun lastIndexOfLF(bytes: ByteArray, fromIndex: Int): Int {
    val index = (fromIndex + 4000).coerceAtMost(bytes.size - 1)
    for (i in index downTo index - 4000 + 1) {
        val num = 10.toByte()
        if (bytes[i] == num) {
            return i
        }
    }
    return index
}

private fun realLogI(tag: String?, msg: String?, tagType: Int = 0) {
    when (tagType) {
        0 -> Log.i(tag, msg ?: "")
        1 -> Log.e(tag, msg ?: "")
    }
}