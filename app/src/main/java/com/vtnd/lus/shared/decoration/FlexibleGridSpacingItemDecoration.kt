package com.vtnd.lus.shared.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlexibleGridSpacingItemDecoration(
    private val top: Int = 0,
    private val right: Int = 0,
    private val bottom: Int = 0,
    private val left: Int = 0,
    private val middle: Int = 0
) : RecyclerView.ItemDecoration() {
    private var horizontalSpaces: IntArray? = null

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager as GridLayoutManager
        val spanCount = getSpanCount(layoutManager)
        val position = parent.getChildAdapterPosition(view) // item position
        val column = layoutManager.spanSizeLookup.getSpanIndex(position, spanCount)
        val row = layoutManager.spanSizeLookup.getSpanGroupIndex(position, spanCount)

        if (horizontalSpaces == null) {
            val recyclerWith = parent.width
            horizontalSpaces = initHorizontalSpaces(spanCount, recyclerWith, left, right, middle)
        }
        outRect.left = horizontalSpaces!![column * 2]
        outRect.right = horizontalSpaces!![column * 2 + 1]
        if (!isFirstRow(row)) {
            outRect.top = middle
        }
        if (isFirstRow(row)) {
            outRect.top = top
        }
        if (isLastRow(parent, row)) {
            outRect.bottom = bottom
        }
    }

    private fun getSpanCount(layoutManager: GridLayoutManager): Int {
        return layoutManager.spanCount
    }

    private fun isFirstRow(row: Int): Boolean {
        return row == 0
    }

    private fun isLastRow(recyclerView: RecyclerView, row: Int): Boolean {
        return row == getLastRow(recyclerView)
    }

    private fun getLastRow(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val spanCount = layoutManager.spanCount
        return layoutManager.spanSizeLookup
            .getSpanGroupIndex(layoutManager.itemCount - 1, spanCount)
    }

    private fun initHorizontalSpaces(
        spanCount: Int,
        recyclerWidth: Int,
        start: Int,
        end: Int,
        middle: Int
    ): IntArray {
        val horizontalSpaces = IntArray(spanCount * 2)
        val itemWidthFull = recyclerWidth / spanCount
        val itemWidthAfterAddSpacing =
            (recyclerWidth - start - end - middle * (spanCount - 1)) / spanCount

        var i = 0
        while (i < horizontalSpaces.size) {
            if (i == 0) {
                horizontalSpaces[i] = start
            } else {
                horizontalSpaces[i] = middle - horizontalSpaces[i - 1]
            }
            horizontalSpaces[i + 1] = itemWidthFull - itemWidthAfterAddSpacing - horizontalSpaces[i]
            i += 2
        }
        return horizontalSpaces
    }
}
