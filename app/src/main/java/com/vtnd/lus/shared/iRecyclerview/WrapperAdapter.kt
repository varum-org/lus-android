package com.vtnd.lus.shared.iRecyclerview

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class WrapperAdapter(
    val adapter: RecyclerView.Adapter<*>,
    private val loadMoreFooterContainer: FrameLayout?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val adapterDataObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() = notifyDataSetChanged()

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) =
            notifyItemRangeChanged(positionStart, itemCount)

        override fun onItemRangeChanged(
            positionStart: Int,
            itemCount: Int,
            payload: Any?
        ) = notifyItemRangeChanged(positionStart, itemCount, payload)

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) =
            notifyItemRangeInserted(positionStart, itemCount)

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) =
            notifyItemRangeRemoved(positionStart, itemCount)

        override fun onItemRangeMoved(
            fromPosition: Int,
            toPosition: Int,
            itemCount: Int
        ) = notifyDataSetChanged()
    }

    init {
        adapter.registerAdapterDataObserver(adapterDataObserver)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int) = run {
                    val wrapperAdapter = recyclerView.adapter as WrapperAdapter
                    when {
                        isFullSpanType(wrapperAdapter.getItemViewType(position)) -> layoutManager.spanCount
                        spanSizeLookup != null -> spanSizeLookup.getSpanSize(position)
                        else -> 1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.adapterPosition
        val type = getItemViewType(position)
        if (isFullSpanType(type)) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }
    }

    private fun isFullSpanType(type: Int): Boolean {
        return type == LOAD_MORE_FOOTER
    }

    override fun getItemCount(): Int {
        return adapter.itemCount + 1
    }

    override fun getItemViewType(position: Int) = when {
        position < adapter.itemCount -> adapter.getItemViewType(position)
        position == adapter.itemCount -> LOAD_MORE_FOOTER
        else -> throw IllegalArgumentException("Wrong type! Position = $position")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder = when (viewType) {
        LOAD_MORE_FOOTER -> LoadMoreFooterContainerViewHolder(loadMoreFooterContainer)
        else -> adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any>
    ) {
        if (position < adapter.itemCount) {
            @Suppress("UNCHECKED_CAST")
            (adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>).onBindViewHolder(
                holder, position, payloads
            )
        }
    }

    internal class LoadMoreFooterContainerViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!)

    companion object {
        private const val LOAD_MORE_FOOTER = Int.MAX_VALUE
    }
}
