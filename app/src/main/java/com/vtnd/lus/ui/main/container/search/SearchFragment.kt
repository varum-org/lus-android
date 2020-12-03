package com.vtnd.lus.ui.main.container.search

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentSearchBinding
import com.vtnd.lus.shared.decoration.VerticalSpaceItemDecoration
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.search.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>() {
    private val searchAdapter by lazy {
        SearchAdapter {

        }
    }

    override val viewModel: SearchViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
            FragmentSearchBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(requireActivity(), searchLayout)
        searchRecyclerView.apply {
            setHasFixedSize(true)
            Timber.i(resources.getDimensionPixelOffset(R.dimen.dp_1).toString())
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_1)))
            adapter = searchAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        searchidols.observeLiveData(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                showChildNoDataFragment(R.id.searchLayout)
            } else {
                searchAdapter.submitList(it.map { itemData -> ItemViewHolder(itemData) })
            }
        }
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
