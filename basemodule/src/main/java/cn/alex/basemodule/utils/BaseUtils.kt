package cn.alex.basemodule.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import java.io.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// 当前日期：20200202
const val currentDate = "yyyyMMdd"

// 当前时间，精确到秒
const val currentSecondTime = "yyyyMMddHHmmss"
fun getCurrentDate(date: String, d: Date): String {
    return try {
        SimpleDateFormat(date, Locale.ENGLISH).format(d)
    } catch (e: Exception) {
        log(LOG_MARK, e.toString())
        "-1"
    }
}

// 正则：除去特殊符号
val REGEX_LETTER_NUMBER = "^[a-z0-9A-Z]+$".toRegex()

// NFC 是否可用
@Suppress("unused")
fun nfcVilible(context: Context?): Boolean {
    return context?.packageManager?.hasSystemFeature(
        PackageManager.FEATURE_NFC
    ) ?: false
}

/**
 * 照片转byte二进制
 * @param imagePath 需要转byte的照片路径
 * @return 已经转成的byte
 */
fun readStream(imagePath: String): ByteArray? {
    lateinit var fileInputStream: FileInputStream
    lateinit var outStream: ByteArrayOutputStream
    try {
        fileInputStream = FileInputStream(imagePath)
        outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (-1 != fileInputStream.read(buffer).also { len = it }) {
            outStream.write(buffer, 0, len)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        outStream.close()
        fileInputStream.close()
    }
    return outStream.toByteArray()
}

/*
 * 向本地写入文件
 */
fun writeString2LocalFile(content: String, pathFile: String) {
    try {
        val file = File(pathFile)
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        val randomAccessFile = RandomAccessFile(file, "rwd")
        randomAccessFile.setLength(0)
        randomAccessFile.seek(file.length())
        randomAccessFile.write(content.toByteArray(Charsets.UTF_8))
        randomAccessFile.close()
    } catch (e: IOException) {
        logE(LOG_MARK, e.message.toString())
    }
}

fun copyBankNumberToClip(context: Context, bankNumber: String?) {
    bankNumber?.let {
        //获取剪贴板管理器：
        val cm: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val mClipData = ClipData.newPlainText("bank_card", bankNumber)
        cm.setPrimaryClip(mClipData)
//    Toast.showToast("已成功帮您复制卡号到剪贴板！")
    }
}

/**
 * 返回 唯一的虚拟 ID
 * 由于使用场景的限制，不能使用imei/imsi/mac等权限相关的数据，包括无法获取的高版本问题
 * 所以这里只通过常用的设备字段来模拟一个id
 */
@Suppress("DEPRECATION")
fun getUniquePsuedoID(): String {
    val szDevIDShort = "71".plus(Build.BOARD.length % 10)
        .plus(Build.BRAND.length % 10)
        .plus(Build.CPU_ABI.length % 10)
        .plus(Build.DEVICE.length % 10)
        .plus(Build.DISPLAY.length % 10)
        .plus(Build.HOST.length % 10)
        .plus(Build.MANUFACTURER.length % 10)
        .plus(Build.MODEL.length % 10)
        .plus(Build.PRODUCT.length % 10)
        .plus(Build.TAGS.length % 10)
        .plus(Build.TYPE.length % 10)
        .plus(Build.USER.length % 10)

    // API >= 9 的设备才有 android.os.Build.SERIAL
    // http://developer.android.com/reference/android/os/Build.html#SERIAL
    // 如果用户更新了系统或 root 了他们的设备，该 API 将会产生重复记录
    var serial: String?
    try {
        serial = android.os.Build::class.java.getField("SERIAL").get(null)?.toString() ?: "serial"
        return UUID(szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
    } catch (exception: Exception) {
        serial = "serial"
    }

    // 最后，组合上述值并生成 UUID 作为唯一 ID
    return UUID(szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
}

/**
 * 把 xxxxx.xx 的数字转成 xx,xxx.xx
 */
fun Any.formatAccount(): String {
    return try {
        DecimalFormat("#,##0.00").format(this)
    } catch (e: Exception) {
        this.toString()
    }
}

inline fun tryCatch(action: () -> Unit?) {
    try {
        action.invoke()
    } catch (e: Exception) {
        logE(LOG_MARK, "exception my method: ${e.printStackTrace()}")
    }
}

inline fun <T> tryCatchT(action: (() -> T)): T? {
    return try {
        action.invoke()
    } catch (e: java.lang.Exception) {
        logE(LOG_MARK, "exception my method: ${e.printStackTrace()}")
        null
    }

}

fun Context.copy(content: String?) {
    if (content.isNullOrBlank() || content.isNullOrEmpty()) return
    val cm: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("text", content)
    cm.setPrimaryClip(mClipData)
//  Toast.showToast("复制成功")
}

// 如果只是简单地延时操作，没有重复执行情况，就是用这种方法
// 否则是用 RXJava timer() 的方式，因为此种方式可以避免重复执行
fun ui(delay: Long? = 0L, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        action.invoke()
    }, delay!!)
}

fun thread(action: () -> Unit) {
    Thread(Runnable {
        action.invoke()
    }).start()
}

// 判空。可以不用 ?. 调用，直接使用 .hasValue() 调用
fun <T> T?.hasValue(): Boolean {
    if (this == null) return false
    when (this) {
        is Boolean -> return this
        is String -> if (this.isBlank() || this.isEmpty()) return false
        is Collection<*> -> if (this.isEmpty()) return false
        is Map<*, *> -> if (this.isEmpty()) return false
        is Array<*> -> if (this.isEmpty()) return false
    }
    return true
}

// 如果是null、空就不执行。直接使用 .value{} 调用
inline fun <T> T?.value(action: (T) -> Unit): T? {
    if (!this.hasValue()) return null
    action(this!!)
    return this
}

// 强转成最初的 List
inline fun <reified T : Any> List<*>.asList() =
    this.filterIsInstance<T>().takeIf { this.size == it.size }

// 转成 List 并返回
inline fun <reified T : Any> Any?.asList(): List<T>? =
    if (this is List<*>) this.filterIsInstance<T>().takeIf { this.size == it.size } else null

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> List<*>.asList2() =
    if (all { it is T })
        this as List<T>
    else null


fun run(action: Runnable?) {
    try {
        action?.run()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        logE(LOG_MARK, e.message ?: "")
    }
}


