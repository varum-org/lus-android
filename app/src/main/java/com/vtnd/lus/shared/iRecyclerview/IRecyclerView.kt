package com.vtnd.lus.shared.iRecyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.vtnd.lus.R

class IRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    private var loadMoreEnabled = false
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var loadMoreFooterContainer: FrameLayout? = null
    private val onLoadMoreScrollListener: OnLoadMoreScrollListener =
        object : OnLoadMoreScrollListener() {
            override fun onLoadMore(recyclerView: RecyclerView?) {
                onLoadMoreListener?.onLoadMore()
            }
        }

    var loadMoreFooterLayoutRes = 0
    var loadMoreFooterView: View? = null
        private set

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.IRecyclerView, defStyle, 0)
        val loadMoreEnabled: Boolean
        try {
            loadMoreEnabled =
                typedArray.getBoolean(R.styleable.IRecyclerView_loadMoreEnabled, false)
            loadMoreFooterLayoutRes =
                typedArray.getResourceId(R.styleable.IRecyclerView_loadMoreFooterLayout, 0)
        } finally {
            typedArray.recycle()
        }
        setLoadMoreEnabled(loadMoreEnabled)
        if (loadMoreFooterLayoutRes != 0) {
            setLoadMoreFooterView(loadMoreFooterLayoutRes)
        }
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        loadMoreEnabled = enabled
        if (loadMoreEnabled) {
            addOnScrollListener(onLoadMoreScrollListener)
        } else {
            removeOnScrollListener(onLoadMoreScrollListener)
        }
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener?) {
        onLoadMoreListener = listener
    }

    private fun setLoadMoreFooterView(loadMoreFooterView: View) {
        if (this.loadMoreFooterView != null) {
            loadMoreFooterContainer?.removeView(loadMoreFooterView)
        }
        if (this.loadMoreFooterView != loadMoreFooterView) {
            this.loadMoreFooterView = loadMoreFooterView
            ensureLoadMoreFooterContainer()
            loadMoreFooterContainer?.addView(loadMoreFooterView)
        }
    }

    private fun setLoadMoreFooterView(@LayoutRes loadMoreFooterLayoutRes: Int) {
        ensureLoadMoreFooterContainer()
        val loadMoreFooter = LayoutInflater.from(context)
            .inflate(loadMoreFooterLayoutRes, loadMoreFooterContainer, false)
        loadMoreFooter?.let { setLoadMoreFooterView(it) }
    }

    var iAdapter: Adapter<*>?
        set(value) {
            ensureLoadMoreFooterContainer()
            adapter = value?.let {
                WrapperAdapter(it, loadMoreFooterContainer)
            }
        }
        get() {
            val wrapperAdapter = adapter as WrapperAdapter?
            return wrapperAdapter?.adapter
        }

    private fun ensureLoadMoreFooterContainer() {
        if (loadMoreFooterContainer == null) {
            loadMoreFooterContainer = FrameLayout(context)
            loadMoreFooterContainer?.layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}
