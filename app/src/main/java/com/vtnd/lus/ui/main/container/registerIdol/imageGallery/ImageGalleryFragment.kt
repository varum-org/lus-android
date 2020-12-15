package com.vtnd.lus.ui.main.container.registerIdol.imageGallery

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentImageGalleryBinding
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import com.vtnd.lus.ui.main.container.registerIdol.imageGallery.adapter.GalleryAdapter2
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ImageGalleryFragment : BaseFragment<FragmentImageGalleryBinding, RegisterIdolViewModel>() {
    private val imageGallery by lazy {
        GalleryAdapter2 {}
    }

    override val viewModel by lazy(LazyThreadSafetyMode.NONE) { requireParentFragment().getViewModel<RegisterIdolViewModel>() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentImageGalleryBinding.inflate(inflater)

    override fun initialize() {

    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        galleryLiveData.observeLiveData(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) showChildNoDataFragment(R.id.imageGalleryLayout)
            else hideChildNoDataFragment()
        }
    }

    companion object {
        fun newInstance() = ImageGalleryFragment()
    }
}
