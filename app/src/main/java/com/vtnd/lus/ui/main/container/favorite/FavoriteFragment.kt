package com.vtnd.lus.ui.main.container.favorite

import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding,
        FavoriteViewModel>() {

    override val viewModel: FavoriteViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentFavoriteBinding.inflate(inflater)

    override fun initialize() {}

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        fun newInstance() = FavoriteFragment()
    }
}
