package com.vtnd.lus.ui.main.container.search

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentSearchBinding
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.decoration.VerticalSpaceItemDecoration
import com.vtnd.lus.shared.extensions.replaceFragment
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.idolDetail.IdolDetailFragment
import com.vtnd.lus.ui.main.container.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_toolbar_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    private val searchAdapter by lazy {
        SearchAdapter { item ->
            val fragment = IdolDetailFragment.newInstance(id = item.id)
            fragment.sharedElementEnterTransition = MaterialContainerTransform()
            replaceFragment(
                containerId = R.id.container,
                fragment = fragment,
                animateType = AnimateType.SLIDE_TO_RIGHT,
                addToBackStack = true
            )
        }
    }

    override val viewModel: SearchViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
            FragmentSearchBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, searchLayout)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.search(s.toString())
            }
        })
        searchRecyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_1)))
            adapter = searchAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        searchIdols.observeLiveData(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) showChildNoDataFragment(R.id.searchLayout)
            else hideChildNoDataFragment()
            searchAdapter.submitList(it.map { itemData -> ItemViewHolder(itemData) })
        }
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
