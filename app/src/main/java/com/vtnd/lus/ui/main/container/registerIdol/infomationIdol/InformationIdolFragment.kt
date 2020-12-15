package com.vtnd.lus.ui.main.container.registerIdol.infomationIdol

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentInformationIdolBinding
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.extensions.showError
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import kotlinx.android.synthetic.main.fragment_information_idol.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class InformationIdolFragment : BaseFragment<FragmentInformationIdolBinding,
        RegisterIdolViewModel>() {

    override val viewModel by lazy(LazyThreadSafetyMode.NONE) { requireParentFragment().getViewModel<RegisterIdolViewModel>() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentInformationIdolBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, informationLayout)
        setupOnTextChange()
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        nickNameError.observeLiveData(viewLifecycleOwner) {
            editNickName.error = it.message
        }
        relationshipError.observeLiveData(viewLifecycleOwner) {
            editRelationship.error = it.message
        }
        descriptionError.observeLiveData(viewLifecycleOwner) {
            editDescription.error = it.message
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    private fun setupOnTextChange() {
        editNickName.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.postNickNameText(s.toString())
            }
        })
        editDescription.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.postDescriptionText(s.toString())
            }
        })
        editRelationship.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.postRelationshipText(s.toString())
            }
        })
    }


    companion object {
        fun newInstance() = InformationIdolFragment()
    }
}
