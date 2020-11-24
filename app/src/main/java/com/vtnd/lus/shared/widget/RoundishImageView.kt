package com.vtnd.lus.shared.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import com.vtnd.lus.R

class RoundishImageView : ImageView {
    private val path: Path = Path()
    private var cornerRadius: Int
    private var roundedCorners: Int

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a= context.obtainStyledAttributes(attrs, R.styleable.RoundishImageView)
        cornerRadius = a.getDimensionPixelSize(R.styleable.RoundishImageView_cornerRadius, 0)
        roundedCorners = a.getInt(R.styleable.RoundishImageView_roundedCorners, CORNER_NONE)
        a.recycle()
    }

    fun setCornerRadius(radius: Int) {
        if (cornerRadius != radius) {
            cornerRadius = radius
            setPath()
            invalidate()
        }
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun setRoundedCorners(corners: Int) {
        if (roundedCorners != corners) {
            roundedCorners = corners
            setPath()
            invalidate()
        }
    }

    fun isCornerRounded(corner: Int): Boolean {
        return roundedCorners and corner == corner
    }

     override fun onDraw(canvas: Canvas) {
        if (!path.isEmpty) {
            canvas.clipPath(path)
        }
        super.onDraw(canvas)
    }

     override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setPath()
    }

    private fun setPath() {
        path.rewind()
        if (cornerRadius >= 1f && roundedCorners != CORNER_NONE) {
            val radii = FloatArray(8)
            for (i in 0..3) {
                if (isCornerRounded(CORNERS[i])) {
                    radii[2 * i] = cornerRadius.toFloat()
                    radii[2 * i + 1] = cornerRadius.toFloat()
                }
            }
            path.addRoundRect(RectF(0F, 0F, width.toFloat(), height.toFloat()),
                radii, Path.Direction.CW)
        }
    }

    companion object {
        const val CORNER_NONE = 0
        private const val CORNER_TOP_LEFT = 1
        private const val CORNER_TOP_RIGHT = 2
        private const val CORNER_BOTTOM_RIGHT = 4
        private const val CORNER_BOTTOM_LEFT = 8
        const val CORNER_ALL = 15
        private val CORNERS = intArrayOf(
            CORNER_TOP_LEFT,
            CORNER_TOP_RIGHT,
            CORNER_BOTTOM_RIGHT,
            CORNER_BOTTOM_LEFT)
    }
}