package com.vtnd.lus.shared.extensions

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.vtnd.lus.R


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

fun View.setTint(color: Int) =
    DrawableCompat.wrap(background).apply {
        DrawableCompat.setTint(this, ContextCompat.getColor(context, color))
    }

fun View.OnClickListener.listenToViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

fun ImageView.randomAvatar() {
    setImageResource(
        listOf(
            R.drawable.people_1,
            R.drawable.people_2,
            R.drawable.people_3,
            R.drawable.people_4,
            R.drawable.people_5,
            R.drawable.people_6,
            R.drawable.people_7,
            R.drawable.people_8,
            R.drawable.people_9
        ).random()
    )
}
