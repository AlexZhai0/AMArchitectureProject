package cn.alex.basemodule.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.alex.basemodule.R
import cn.alex.basemodule.application.BaseApplication


/**
 * 通过 Context 获取当前 FragmentActivity，父类统一使用 FragmentActivity
 */
fun Context.getActivity(): FragmentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is FragmentActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

// 通过 View 获取当前 FragmentActivity
fun View.getActivity(): FragmentActivity? {
    return context.getActivity()
}

// 主应用中的 ApplicationContext
val appContext: Context? = application?.applicationContext

// 主应用中的 Application
val application: BaseApplication?
    @SuppressLint("PrivateApi")
    get() {
        return tryCatchT {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as? BaseApplication
        }
    }

fun Activity?.isDestroy() = this == null || this.isFinishing || this.isDestroyed

/**
 * 设置全屏
 */
fun setFullScreen(activity: Activity?) {
    activity?.window?.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

/**
 * 从当前状态中只取消全屏状态，即恢复到原来的状态
 */
fun setNormalScreen(activity: Activity?) {
    val attrs = activity?.window?.attributes
    attrs?.flags = attrs?.flags?.and(WindowManager.LayoutParams.FLAG_FULLSCREEN.inv())
    activity?.window?.attributes = attrs
}


fun setWindowBackground(context: Context?, alpha: Float) {
    val activity = context as? Activity
    val layoutParams = activity?.window?.attributes
    layoutParams?.alpha = alpha
    activity?.window?.attributes = layoutParams
}

/**
 * 对 「¥0.00」中加划线
 */
fun TextView.addTextUnderLine(source: String) {
    this.text = SpannableString(source).apply {
        setSpan(StrikethroughSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}

/**
 * 显示软键盘
 */
fun EditText?.showSoftInput() {
    this?.requestFocus()
    this?.getActivity()?.window
        ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    val softInputManager =
        this?.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    softInputManager?.toggleSoftInputFromWindow(this?.windowToken, 0, 0)
}

// 关闭软键盘 1
fun EditText?.hideSoftInput() {
    val softInputManager =
        this?.getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    this?.getActivity()?.window
        ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    softInputManager?.hideSoftInputFromWindow(this?.windowToken, 0)
}

// 关闭软键盘 2
fun Activity?.hideSoftInput() {
    val softInputManager =
        this?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val decorView = this?.window?.decorView
    softInputManager?.hideSoftInputFromWindow(decorView?.windowToken, 0)
}

/**
 * 软键盘是否已经弹出了
 */
fun Activity?.isSoftInputShowing(): Boolean {
    return this?.window?.attributes?.softInputMode ==
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
}

/**
 * 设置 TextView 中的文字可以点击
 * @param content TextView 上显示的全部文字
 * @param clickContent TextView 上需要设置点击事件的文本数组，是可变参数
 * @param clickListener 点击事件文本对应的点击事件
 * @param textColor 点击文本颜色

textView2?.textContentClick("需要的的点击事件，和点击按钮显示",
arrayOf("点击事件", "点击按钮"),
arrayOf<(view: View) -> Unit>(
{
toast("第一个点击事件")
}, {
toast("第er个点击事件")
}
))
 */
fun TextView?.textContentClick(
    content: String,
    clickContent: Array<String>,
    clickListener: Array<(view: View) -> Unit>,
    textColor: Int? = null
) {
    if (this == null) return
    val spannable = SpannableStringBuilder()
    spannable.append(content)
    if (!clickContent.hasValue()) return
    for ((index, c) in clickContent.withIndex()) {
        val startIndex = content.indexOf(c)
        if (startIndex == -1) continue
        val endIndex = startIndex + c.length
        val listener = if (index < clickListener.size) clickListener[index] else null
        val clickTime = BlockListener<View>({
            listener?.invoke(it)
        })
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                clickTime.setBlockEvent(view)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        // 添加文字的点击事件
        spannable.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val foregroundColor = ForegroundColorSpan(textColor ?: this.context.setColor(R.color.black))
        // 添加文字的背景色
        spannable.setSpan(foregroundColor, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }
    text = spannable
}

/**
 * 如果重写 View 的点击事件比较麻烦，就是用这个方法
val clickTime = BlockListener<View>({ view ->
// do something
})
view?.setOnClickListener { clickTime.setBlockEvent(it) }
 */
class BlockListener<T>(
    val listener: (T) -> Unit,
    private val blockTime: Int = 1000
) {
    var clickTime: Long = 0L
    fun setBlockEvent(t: T) {
        if (System.currentTimeMillis() - clickTime > blockTime) {
            clickTime = System.currentTimeMillis()
            listener(t)
        }
    }
}

/*
 * 对每个 View 类型设置 FullSpan，除了瀑布流其余都是默认一列
 */
fun getTypeViewHolder(viewParent: ViewGroup, layoutId: Int, isFullSpan: Boolean? = true): View {
    val view = LayoutInflater.from(viewParent.context).inflate(layoutId, viewParent, false)
    val params = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams
    if (isFullSpan == true && params != null) {
        // 占满全屏
        params.isFullSpan = true
        view.layoutParams = params
    }
    return view
}

/**
 * 用 View 的宽和高的比例，重新设置宽高，ratio = Width/Height，Height = Width/ratio
 */
fun View?.resetParamsWithWHRatio(realWidth: Float, ratio: Float) {
    val param = this?.layoutParams
    param?.width = realWidth.toInt()
    param?.height = (realWidth / ratio).toInt()
    this?.layoutParams = param
}

// 使用实际的宽高来设置 View，可以单独设置宽或高
fun View?.resetParamsWithWH(realWidth: Float? = null, realHeight: Float? = null) {
    val param = this?.layoutParams
    realWidth?.let { param?.width = it.toInt() }
    realHeight?.let { param?.height = it.toInt() }
    this?.layoutParams = param
}

// 使用 View 的 Margin
fun View?.resetMargin(
    leftMargin: Int? = null,
    topMargin: Int? = null,
    rightMargin: Int? = null,
    bottomMargin: Int? = null
) {
    val param = this?.layoutParams as? ViewGroup.MarginLayoutParams
    leftMargin?.value { param?.leftMargin = it }
    topMargin?.value { param?.topMargin = it }
    rightMargin?.value { param?.rightMargin = it }
    bottomMargin?.value { param?.bottomMargin = it }
    this?.layoutParams = param
}

/**
 * 改变金额的后缀和前缀，字体的最大值可以设置，随着金额变大，字体大小随之变小
 *
 * @param amount 金额
 * @param isSuffix 是否是后缀（元），否则是前缀（￥）
 * @param amountMaxSize 金额字体大小的最大值，根据UI大小配置
 * @param minLength 设置金额长度的最小值，字体的大小按照这个数字为基准缩小
 * @param reduceRatio 设置金额大小缩小系数，金额长度每加1，字体就会缩小系数的倍数
 */
fun TextView?.dealAmountSuffixValue(
    amount: String?,
    isSuffix: Boolean = true,
    amountMaxSize: Int = 32,
    minLength: Int = 5, // 例如 22.22 的长度，长度每加一就会更改金额大小
    reduceRatio: Int = 2, // 字体缩小系数，长度每加一，字体就会缩小系数的倍数
    symbolSuffix: String? = null // ￥、元、+ 等
) {
    if (this == null || !amount.hasValue()) return
    // 后缀：22.22元，前缀：￥22.22

    val source = if (isSuffix) {
        val symbol = if (symbolSuffix.hasValue()) symbolSuffix else "元"
        "${amount}$symbol"
    } else {
        val symbol = if (symbolSuffix.hasValue()) symbolSuffix else "¥"
        "$symbol$amount"
    }
    var typeSize = when {
        source.length - 1 <= minLength -> amountMaxSize // 22.22
        source.length - 1 == minLength + 1 -> amountMaxSize - reduceRatio // 222.22
        source.length - 1 == minLength + 2 -> amountMaxSize - reduceRatio * 2 // 2222.22
        source.length - 1 == minLength + 3 -> amountMaxSize - reduceRatio * 3 // 22222.22
        source.length - 1 == minLength + 4 -> amountMaxSize - reduceRatio * 4 // 222222.22
        source.length - 1 == minLength + 5 -> amountMaxSize - reduceRatio * 5 // 2222222.22
        else -> amountMaxSize - reduceRatio * 8
    }
    typeSize = if (typeSize <= 0) 10 else typeSize
    val spannable = SpannableStringBuilder()
    spannable.append(source)
    val amountStart: Int
    val amountEnd: Int
    val suffixStart: Int
    val suffixEnd: Int
    if (isSuffix) {
        amountStart = 0
        amountEnd = source.length - 1
        suffixStart = source.length - 1
        suffixEnd = source.length
    } else {
        amountStart = 1
        amountEnd = source.length
        suffixStart = 0
        suffixEnd = 1
    }
    // 金额
    spannable.setSpan(
        AbsoluteSizeSpan(typeSize, true), amountStart, amountEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    // 后缀、前缀
    spannable.setSpan(
        AbsoluteSizeSpan(typeSize / 2, true), suffixStart, suffixEnd,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannable
}

/**
 *
 * @param amount 金额
 * @param symbolSuffix 前缀或后缀的符号，￥、元、+ 等,null则不添加任何前后缀
 * @param isSuffix 是否是后缀
 * @param integerSize 整数部分的字号
 * @param decimalSize 小数部分的字号，包含.
 * @param suffixSize 前后缀的符号字号
 * @param maxOriginalLength 不缩小的最大长度，金额加符号的长度，超过该值时，进行整体字符串缩小
 * @param integerBold 整数部分是否加粗
 * @param decimalBold 小数部分是否加粗
 * @param suffixBold 前后缀符号是否加粗
 */
fun TextView?.amountDecimalUnitValue(
    amount: String?,
    symbolSuffix: String? = null,
    isSuffix: Boolean = true,
    integerSize: Int = 18,
    decimalSize: Int = 11,
    suffixSize: Int = 10,
    maxOriginalLength: Int = 7,
    integerBold: Boolean = false,
    decimalBold: Boolean = false,
    suffixBold: Boolean = false
) {
    if (this == null || !amount.hasValue()) return

    val integerStart: Int?
    val integerEnd: Int?
    val suffixStart: Int?
    val suffixEnd: Int?
    val decimalStart: Int?
    val decimalEnd: Int?
    //金额和符号拼接成一个字符串
    val source = if (symbolSuffix.hasValue()) {
        if (isSuffix)
            "$amount$symbolSuffix"
        else
            "$symbolSuffix$amount"
    } else {
        amount!!
    }

    val spannable = SpannableStringBuilder()
    spannable.append(source)

    val pointIndex = source.lastIndexOf(".")

    //计算整数、小数、符号、的起始位置
    if (symbolSuffix.hasValue()) {
        if (isSuffix) {
            suffixStart = source.length - symbolSuffix!!.length
            suffixEnd = source.length
            integerStart = if (pointIndex == 0) null else 0
            integerEnd = if (pointIndex == -1) suffixStart else pointIndex
            decimalStart = if (pointIndex == -1) null else pointIndex
            decimalEnd = suffixStart
        } else {
            suffixStart = 0
            suffixEnd = symbolSuffix!!.length
            integerStart = if (pointIndex == suffixEnd) null else suffixEnd
            integerEnd = if (pointIndex == -1) source.length else pointIndex
            decimalStart = if (pointIndex == -1) null else pointIndex
            decimalEnd = source.length
        }
    } else {
        suffixStart = null
        suffixEnd = null
        integerStart = if (pointIndex == 0) null else 0
        integerEnd = if (pointIndex == -1) source.length else pointIndex
        decimalStart = if (pointIndex == -1) null else pointIndex
        decimalEnd = source.length
    }

    //设置整数部分的字号和加粗
    if (integerStart.hasValue()) {
        spannable.setSpan(
            AbsoluteSizeSpan(integerSize, true),
            integerStart!!,
            integerEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (integerBold) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                integerStart!!,
                integerEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    //设置小数部分的字号和加粗
    if (decimalStart.hasValue()) {
        spannable.setSpan(
            AbsoluteSizeSpan(decimalSize, true),
            decimalStart!!,
            decimalEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (decimalBold) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                decimalStart!!,
                decimalEnd,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    //设置符号部分的字号和加粗
    if (suffixStart.hasValue() && suffixEnd.hasValue()) {
        spannable.setSpan(
            AbsoluteSizeSpan(suffixSize, true),
            suffixStart!!,
            suffixEnd!!,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (suffixBold) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                suffixStart!!,
                suffixEnd!!,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    //设置超出maxOriginalLength后的整体缩小
    if (source.length > maxOriginalLength) {
        spannable.setSpan(
            RelativeSizeSpan((1f / source.length) / (1f / maxOriginalLength)),
            0, source.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    text = spannable
}

/**
 * 设置 TextView 及其子类的 drawableStart/End/Top/Bottom
 * @param drawable 对应 Drawable
 * @param orientation 1 Start, 2 Top, 3 End, 4 Bottom
 */
fun TextView?.setDrawableCompound(
    @DrawableRes drawable: Int,
    orientation: Int = 1,
    padding: Int = 0
) {
    val d = this?.context?.getDrawable(drawable)
    when (orientation) {
        1 -> {
            this?.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
        }
        2 -> {
            this?.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null)
        }
        3 -> {
            this?.setCompoundDrawablesWithIntrinsicBounds(null, null, d, null)
        }
        4 -> {
            this?.setCompoundDrawablesWithIntrinsicBounds(null, null, null, d)
        }
    }
    this?.compoundDrawablePadding = padding
}

/**
 * 设置文字中的其他文字颜色
 */
fun TextView.dealDiffSizeAndColor(
    source: String,
    lightText: Array<String>? = null,
    lightColor: Int
) {
    val spannable = SpannableStringBuilder()
    spannable.append(source)
    var lastTextLengthIndex = -1
    lightText.value {
        for (text in it) {
            val startIndex = if (lastTextLengthIndex != -1) {
                // 为了防止需要处理的文字在 source 中有重复，后面的文字就找不对位置问题
                val currentText = source.subSequence(lastTextLengthIndex, source.length)
                val currentTextStartIndex = currentText.indexOf(text)
                if (currentTextStartIndex == -1) continue
                currentTextStartIndex + lastTextLengthIndex
            } else {
                source.indexOf(text)
            }
            if (startIndex == -1) continue
            val endIndex = startIndex + text.length
            lastTextLengthIndex = endIndex
            // 添加文字的背景色
            spannable.setSpan(
                ForegroundColorSpan(lightColor),
                startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    text = spannable
}

/**
 * 根据 View 距离来重新设置当前 View 的透明度，用于标题跟着列表滑动透明度从透明变成不透明
 *
 * @param color 默认颜色
 * @param distance 手势滑动距离
 */
fun View?.resetViewBackgroundColor(
    color: Int,
    distance: Float
) {
    if (this == null || this.height == 0) return
    val height = this.height
    val ratio = distance / height
    val alphaDefault = Color.alpha(color)
    val alpha = (Color.alpha(color) * ratio).toInt()
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    val realColor = if (distance < height) {
        Color.argb(alpha, red, green, blue)
    } else {
        // 已经完全设置了颜色，快速滑动时也要能设置完成
        Color.argb(alphaDefault, red, green, blue)
    }
    setBackgroundColor(realColor)
}