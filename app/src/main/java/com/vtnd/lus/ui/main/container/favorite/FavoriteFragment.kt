package com.vtnd.lus.ui.main.container.favorite

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentFavoriteBinding
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.favorite.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding,
        FavoriteViewModel>() {
    private val favoriteAdapter by lazy {
        FavoriteAdapter {

        }
    }

    override val viewModel: FavoriteViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
            FragmentFavoriteBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(requireActivity(), favoriteLayout)
        favoriteRecyclerView?.apply {
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        favorites.observeLiveData(viewLifecycleOwner) {
            if(it.isNullOrEmpty()){
                showChildNoDataFragment(R.id.favoriteLayout)
            }else{
                hideChildNoDataFragment()
                favoriteAdapter.submitList(it.map { itemData ->
                    ItemViewHolder(itemData)
                })
            }
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
