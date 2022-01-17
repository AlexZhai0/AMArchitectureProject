package cn.alex.commonmodule.sp

import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class Preference<T>(
    private val sharedPrefs: MMKV,
    private val name: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    /**
     * 查找数据 返回给调用方法一个具体的对象
     * 如果查找不到类型就采用反序列化方法来返回类型
     * default是默认对象 以防止会返回空对象的异常
     * 即如果name没有查找到value 就返回默认的序列化对象，然后经过反序列化返回
     */
    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T = with(sharedPrefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default) ?: ""
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> getString(name, default as? String) ?: ""
        }
        res as T
    }

    private fun putPreference(name: String, value: T) = with(sharedPrefs) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> putString(name, value.toString())
        }
    }
}