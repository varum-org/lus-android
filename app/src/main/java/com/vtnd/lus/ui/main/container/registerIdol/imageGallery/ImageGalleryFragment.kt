package com.vtnd.lus.ui.main.container.registerIdol.imageGallery

import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.databinding.FragmentImageGalleryBinding
import com.vtnd.lus.shared.decoration.FlexibleGridSpacingItemDecoration
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.shared.extensions.showError
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import com.vtnd.lus.ui.main.container.registerIdol.imageGallery.adapter.GalleryAdapter2
import kotlinx.android.synthetic.main.fragment_image_gallery.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class ImageGalleryFragment : BaseFragment<FragmentImageGalleryBinding, RegisterIdolViewModel>() {
    private val imageGalleryAdapter2 by lazy {
        GalleryAdapter2 {}
    }

    override val viewModel by lazy(LazyThreadSafetyMode.NONE) { requireParentFragment().getViewModel<RegisterIdolViewModel>() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentImageGalleryBinding.inflate(inflater)

    override fun initialize() {
        initView()
        addImageAction()
    }

    private fun initView() {
        imageGalleryRecyclerView.apply {
            adapter = imageGalleryAdapter2
            addItemDecoration(
                FlexibleGridSpacingItemDecoration(
                    top = resources.getDimensionPixelOffset(R.dimen.dp_8),
                    bottom = resources.getDimensionPixelOffset(R.dimen.dp_8),
                    left = resources.getDimensionPixelOffset(R.dimen.dp_16),
                    right = resources.getDimensionPixelOffset(R.dimen.dp_16),
                    middle = resources.getDimensionPixelOffset(R.dimen.dp_8)
                )
            )
        }
    }

    private fun addImageAction() {
        imageGalleryFAB.safeClick {
            val launcher =
                registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                    uri?.let {
                        viewModel.postImage(it)
                        Timber.i("$it")
                    }
                }
            launcher.launch(arrayOf("image/*"))
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        galleryLiveData.observeLiveData(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) showChildNoDataFragment(R.id.imageGalleryLayout)
            else hideChildNoDataFragment()
            imageGalleryAdapter2.submitList(ArrayList(it.map { uri -> ItemViewHolder(uri) }).toList())
        }
        urisError.observeLiveData(viewLifecycleOwner) {
            it.message?.let { message -> showError(message) }
        }
    }

    companion object {
        fun newInstance() = ImageGalleryFragment()
    }
}
