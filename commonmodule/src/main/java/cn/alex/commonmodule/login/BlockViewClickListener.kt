package cn.alex.commonmodule.login

import android.view.View

/**
 * Created by cz on 2017/9/14.
 * 点击阻塞包装对象
 */
class BlockViewClickListener(
    val listener: View.OnClickListener,
    var blockTime: Int = 1000
) : View.OnClickListener by listener {
    var clickTime: Long = 0L
    override fun onClick(v: View) {
        if (System.currentTimeMillis() - clickTime > blockTime) {
            clickTime = System.currentTimeMillis()
            listener.onClick(v)
        }
    }
}

fun View.onBlockClick(listener: (View) -> Unit) {
    setOnClickListener(BlockViewClickListener(View.OnClickListener { listener(it) }))
}

fun View.onBlockClickByTime(listener: (View) -> Unit, blockTime: Int = 300) {
    setOnClickListener(BlockViewClickListener(View.OnClickListener { listener(it) }, blockTime))
}