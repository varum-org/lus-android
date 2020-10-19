package com.vtnd.lus.shared.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.vtnd.lus.shared.BaseViewHolder
import com.vtnd.lus.shared.RecyclerType
import com.vtnd.lus.shared.RecyclerViewItem
import com.vtnd.lus.shared.TYPE_UNKNOWN

/**
 * Use this adapter for temporary fixing some issues of [ListAdapter].
 *
 * Try to use [BaseAdapter] if possible
 */
@Suppress("UNCHECKED_CAST")
abstract class BasicAdapter : RecyclerView.Adapter<BaseViewHolder<in RecyclerViewItem>>() {

    private var data = mutableListOf<RecyclerViewItem>()

    fun set(item: RecyclerViewItem, index: Int) {
        data[index] = item
        notifyItemChanged(index)
    }

    fun set(items: List<RecyclerViewItem>) {
        data.apply {
            clear()
            addAll(items)
            notifyDataSetChanged()
        }
    }

    /**
     * @param forceAnim: do animation of changed rows if needed
     */
    fun set(items: List<RecyclerViewItem>, index: Int, forceAnim: Boolean = true) {
        if (isValidPosition(index)) {
            data = data.dropLast(items.size - index).toMutableList()
        }
        data.apply {
            addAll(items)
            if (forceAnim) {
                notifyItemRangeChanged(index, items.size)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun add(item: RecyclerViewItem, index: Int) {
        data.add(index, item)
        notifyItemInserted(index)
    }

    fun add(items: List<RecyclerViewItem>) {
        val currentSize = data.size
        data.addAll(items)
        notifyItemRangeInserted(currentSize, items.size)
    }

    fun remove(position: Int) {
        if (isValidPosition(position).not()) return
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getItem(position: Int) = if (isValidPosition(position)) data[position] else null

    @RecyclerType
    final override fun getItemViewType(position: Int) = getItem(position)?.type ?: TYPE_UNKNOWN

    override fun getItemCount() = data.size

    protected fun inflateView(@LayoutRes layoutRes: Int, parent: ViewGroup): View =
        LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

    private fun isValidPosition(position: Int) = data.size > position

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RecyclerViewItem> {
        val holder = customViewHolder(parent, viewType) as? BaseViewHolder<*>
            ?: throw ClassCastException(
                "Please create BaseViewHolder for a child class of RecyclerViewItem !"
            )
        return holder as BaseViewHolder<in RecyclerViewItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<in RecyclerViewItem>, position: Int) {
        getItem(position)?.apply {
            holder.bind(this)
        }
    }

    abstract fun customViewHolder(parent: ViewGroup, viewType: Int): Any
}
