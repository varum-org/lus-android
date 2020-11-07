package com.vtnd.lus.shared.extensions

import android.os.SystemClock
import android.view.View

fun View.isVisible() = visibility == View.VISIBLE
fun View.isGone() = visibility == View.GONE
fun View.isInvisible() = this.visibility == View.INVISIBLE

fun View.visible() {
    if (!isVisible()) this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun visibleViews(vararg views: View?) {
    views.map { it?.visible() }
}

fun invisibleViews(vararg views: View?) {
    views.map { it?.invisible() }
}

fun goneViews(vararg views: View?) {
    views.map { it?.gone() }
}

fun View.safeClick(blockInMillis: Long = 1000L, onClick: (View) -> Unit) {
    var lastClickTime = 0L
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        onClick(this)
    }
}

fun View.OnClickListener.listenToViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}
