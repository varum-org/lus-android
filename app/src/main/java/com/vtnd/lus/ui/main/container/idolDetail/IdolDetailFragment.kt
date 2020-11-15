package com.vtnd.lus.ui.main.container.idolDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.data.repository.source.remote.api.request.IdolResponse
import com.vtnd.lus.databinding.FragmentIdolDetailBinding
import kotlinx.android.synthetic.main.fragment_idol_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class IdolDetailFragment : BaseFragment<FragmentIdolDetailBinding, IdolDetailViewModel>() {

    override val viewModel: IdolDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentIdolDetailBinding.inflate(inflater)

    override fun initialize() {
        arguments?.apply {
            getParcelable<IdolResponse>(ARGUMENT_IDOL)?.let {
                idolImage.transitionName = it.idol.id
                Toast.makeText(activity, it.idol.nickName, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun registerLiveData() {
        super.registerLiveData()
    }

    companion object {
        private const val ARGUMENT_IDOL = "ARGUMENT_IDOL"

        fun newInstance(idol: IdolResponse) = IdolDetailFragment().apply {
            arguments = bundleOf(ARGUMENT_IDOL to idol)
        }
    }
}
