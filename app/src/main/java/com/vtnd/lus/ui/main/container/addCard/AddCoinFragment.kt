package com.vtnd.lus.ui.main.container.addCard

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentAddCoinBinding
import com.vtnd.lus.shared.extensions.goBackFragment
import com.vtnd.lus.shared.extensions.initToolbarBase
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.shared.extensions.showNotificationAlertDialog
import com.vtnd.lus.shared.liveData.observeLiveData
import kotlinx.android.synthetic.main.fragment_add_coin.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCoinFragment : BaseFragment<FragmentAddCoinBinding,
        AddCoinViewModel>() {

    override val viewModel: AddCoinViewModel by viewModel()

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentAddCoinBinding.inflate(inflater)

    override fun initialize() {
        initToolbarBase(
            getString(R.string.add_coin),
            isShowIconLeft = true
        )
        button_add.safeClick {
            viewModel.addCoin(edit_coin.editText?.text.toString())
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        coinNameError.observeLiveData(viewLifecycleOwner) {
            edit_coin.editText?.error = it.message
        }
        addCoinLiveData.observeLiveData(viewLifecycleOwner) {
            showNotificationAlertDialog {
                icon(R.drawable.ic_check)
                statusMessage(getString(R.string.success))
                button(getString(R.string.ok2)) {
                    goBackFragment()
                }
            }
        }
    }

    companion object {
        fun newInstance() = AddCoinFragment()
    }
}
