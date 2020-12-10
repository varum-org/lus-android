package com.vtnd.lus.ui.main.container.registerIdol.serviceIdol

import android.view.LayoutInflater
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.databinding.FragmentServiceIdolBinding
import com.vtnd.lus.shared.extensions.safeClick
import com.vtnd.lus.shared.extensions.setupDismissKeyBoard
import com.vtnd.lus.shared.extensions.showAddServiceAlertDialog
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import com.vtnd.lus.ui.main.container.registerIdol.serviceIdol.adapter.AddServiceAdapter
import kotlinx.android.synthetic.main.fragment_service_idol.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ServiceIdolFragment : BaseFragment<FragmentServiceIdolBinding, RegisterIdolViewModel>() {
    private var services = emptyList<Service>()
    private val serviceAdapter = AddServiceAdapter({
        showAddServiceAlertDialog(services, it) {
            title(getString(R.string.add_service))
            leftButton(getString(R.string.cancel))
            rightButton(getString(R.string.add)) { service ->
                viewModel.postService(service)
            }
        }
    }, {
        viewModel.deleteService(it)
    })
    override val viewModel by lazy(LazyThreadSafetyMode.NONE) { requireParentFragment().getViewModel<RegisterIdolViewModel>() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentServiceIdolBinding.inflate(inflater)

    override fun initialize() {
        setupDismissKeyBoard(activity, serviceLayout)
        initView()
        showAddService()
    }

    private fun showAddService() {
        serviceFAB.safeClick {
            showAddServiceAlertDialog(services, Service()) {
                title(getString(R.string.add_service))
                leftButton(getString(R.string.cancel))
                rightButton(getString(R.string.add)) { service ->
                    viewModel.postService(service)
                }
            }
        }
    }

    private fun initView() {
        serviceRecyclerView.apply {
            adapter = serviceAdapter
        }
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        servicesLiveData.observeLiveData(viewLifecycleOwner) {
            services = it
        }
        idolRequestLiveData.observeLiveData(viewLifecycleOwner) {
            if (it.services.isNullOrEmpty())
                showChildNoDataFragment(R.id.serviceLayout)
            else hideChildNoDataFragment()
            serviceAdapter.submitList(ArrayList(it.services.map { service ->
                ItemViewHolder(service)
            }).toList())
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    companion object {
        fun newInstance() = ServiceIdolFragment()
    }
}
