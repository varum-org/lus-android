package com.vtnd.lus.ui.main.container.idolDetail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.base.ItemViewHolder
import com.vtnd.lus.data.model.RoomJsonAdapter
import com.vtnd.lus.data.repository.source.remote.api.response.IdolResponse
import com.vtnd.lus.databinding.FragmentIdolDetailBinding
import com.vtnd.lus.di.GlideApp
import com.vtnd.lus.di.MainApplication
import com.vtnd.lus.shared.AnimateType
import com.vtnd.lus.shared.constants.Constants.BASE_IMAGE_URL
import com.vtnd.lus.shared.decoration.HorizontalMarginItemDecoration
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.auth.AuthActivity
import com.vtnd.lus.ui.main.container.idolDetail.adapter.CartAdapter
import com.vtnd.lus.ui.main.container.idolDetail.adapter.GalleryAdapter
import com.vtnd.lus.ui.main.container.idolDetail.adapter.ServiceAdapter
import com.vtnd.lus.ui.main.container.message.MessageFragment
import com.vtnd.lus.ui.main.container.room.RoomFragment
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_idol_detail.*
import kotlinx.android.synthetic.main.layout_cart_bottom.*
import kotlinx.android.synthetic.main.layout_information_idol.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class IdolDetailFragment : BaseFragment<FragmentIdolDetailBinding, IdolDetailViewModel>(), View.OnClickListener {

    lateinit var socket: Socket
    override val viewModel: IdolDetailViewModel by viewModel()
    private val galleryAdapter = GalleryAdapter {}
    private val cartAdapter = CartAdapter { ActionType, itemData ->
        viewModel.handleHoursService(ActionType, itemData)
    }
    private val serviceAdapter = ServiceAdapter { d, p ->
        viewModel.addServiceToCard(d, p)
    }
    private var calendar = getCalendar()
    private var idolResponse: IdolResponse? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun inflateViewBinding(inflater: LayoutInflater) =
            FragmentIdolDetailBinding.inflate(inflater)

    override fun initialize() {
        socket = (activity?.application as MainApplication).socket
        setupDismissKeyBoard(activity, layout)
        arguments?.apply {
            getParcelable<IdolResponse>(ARGUMENT_IDOL)?.let {
                idolImage.transitionName = it.idol.id
                idolResponse = it
                viewModel.addServicesIdol(it.idol.services)
            } ?: apply {
                getString(ARGUMENT_IDOL_ID)?.let {
                    idolImage.transitionName = it
                    viewModel.getIdolDetail(it)
                }
            }
        }
        setupBottomSheet()
        listenToViews(
            messageFAB,
            backImageButton,
            addFavoriteFAB,
            toRentButton,
            startDateImage,
            noteText
        )
        setupRecyclerView()
        binDataToView(idolResponse)
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        room.observeLiveData(viewLifecycleOwner) {
            replaceFragment(
                R.id.container, MessageFragment.newInstance(it), addToBackStack = true,
                animateType = AnimateType.SLIDE_TO_RIGHT
            )
        }
        roomJson.observeLiveData(viewLifecycleOwner){
            socket.emit(RoomFragment.SOCKET_CREATE_ROOM, it)
        }
        startDate.observeLiveData(viewLifecycleOwner) { date ->
            calendar.time = date
            context?.let {
                startDateText.text = calendar.time.toStringDefaultDate(it)
            }
        }
        note.observeLiveData(viewLifecycleOwner) {
            noteText.text = it
        }
        idolServicesLiveData.observeLiveData(viewLifecycleOwner) {
            serviceAdapter.submitList(it)
        }
        idolResponseLiveData.observeLiveData(viewLifecycleOwner) {
            idolResponse = it
            viewModel.addServicesIdol(it.idol.services)
            binDataToView(it)
        }
        cardServicesLiveData.observeLiveData(viewLifecycleOwner) {
            badgeText.text = it.size.toString()
            serviceTotal.text = "${it.sumPrice()}"
            cartAdapter.submitList(it)
            bottomSheetBehavior.apply {
                if (it.isNotEmpty()) {
                    if (state == BottomSheetBehavior.STATE_HIDDEN)
                        state = BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    noteText.text = null
                    viewModel.startDate.postValue(Date())
                    state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.backImageButton -> this.goBackFragment()
            R.id.addFavoriteFAB -> {
            }
            R.id.toRentButton -> viewModel.checkLogin() {
                if (it) showAlert() else authentication()
            }
            R.id.startDateImage -> onStartDateClick()
            R.id.noteText -> onNoteClick()
            R.id.messageFAB -> viewModel.checkLogin() {
                if (it) idolResponse?.user?.id?.let { id -> viewModel.getRoom(id) }
                else authentication()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        onHideSoftKeyBoard()
    }

    private fun showAlert() {
        showAlertDialog {
            title(getString(R.string.rent))
            message(getString(R.string.are_you_sure_to_rent))
            leftButton(getString(R.string.no))
            rightButton(getString(R.string.yes)) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                showLoading(true)
                delayTask({
                    showLoading(false)
                }, 800)
                // action oder
            }
        }
    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(cartBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        headerCartLayout.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                iconArrowImage.rotation = slideOffset * 180
            }
        })
    }

    private fun onStartDateClick() {
        pickDateTime(calendar) {
            if (!it.time.before(getCalendar().time)) {
                calendar.time = it.time
                viewModel.startDate.postValue(calendar.time)
            } else showError(getString(R.string.invalid) + ": " + getString(R.string.start_date))
        }
    }

    private fun onNoteClick() {
        showNoteAlertDialog(noteText.text.toString()) {
            title(getString(R.string.note))
            leftButton(getString(R.string.cancel))
            rightButton(getString(R.string.apply)) { note ->
                viewModel.note.postValue(note)
            }
        }
    }

    private fun authentication() {
        activity?.apply {
            showLoading(true)
            delayTask({
                showLoading(false)
                startActivity(Intent(this, AuthActivity::class.java))
                overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
            }, 800)
        }
    }

    private fun binDataToView(idolResponse: IdolResponse?) {
        setupViewPager2()
        setupIndicators()
        setupCurrentIndicator(0)
        idolGalleryViewPager2.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupCurrentIndicator(position)
            }
        })
        idolResponse?.apply {
            GlideApp.with(this@IdolDetailFragment)
                .load(BASE_IMAGE_URL + idol.imageGallery[0])
                .placeholder(R.color.pink_50)
                .error(R.color.red_a400)
                .dontAnimate()
                .into(idolImage)
            idolNameText.text = user?.birthday?.let {
                getString(R.string.nick_name, idol.nickName, it.getAge())
            } ?: idol.nickName
            serviceNameText.text = getString(R.string.present_to, idol.nickName)
            idolDescriptionText.text = idol.description
            idolLocationText.text = context?.getString(R.string.live_in, "Đà Nẵng")
            idolAddressText.text = getString(R.string.idol_address, idol.address)
            idolRelationshipText.text = idol.relationship
            distinctionText.text = getString(R.string.crush_national_citizen)
        }
    }

    private fun setupViewPager2() {
        idolResponse?.let {
            idolGalleryViewPager2.apply {
                adapter = galleryAdapter.apply {
                    it.idol.imageGallery.run { subList(1, this.size) }
                        .let { submitList(it.map { imagePath -> ItemViewHolder(imagePath) }) }
                }
                addItemDecoration(HorizontalMarginItemDecoration(context, R.dimen.dp_16))
                offscreenPageLimit = 1
            }
        }
    }

    private fun setupRecyclerView() {
        idolServiceRecyclerView.apply {
            setHasFixedSize(false)
            adapter = serviceAdapter
        }
        cartRecyclerView.apply {
            setHasFixedSize(true)
            adapter = cartAdapter
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(galleryAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.indicator_active
                        )
                )
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.indicator_inactive
                        )
                )
            }
        }
    }

    companion object {
        private const val ARGUMENT_IDOL = "ARGUMENT_IDOL"
        private const val ARGUMENT_IDOL_ID = "ARGUMENT_IDOL_ID"

        fun newInstance(idol: IdolResponse? = null, id: String) =
            IdolDetailFragment().apply {
                arguments = bundleOf(ARGUMENT_IDOL to idol, ARGUMENT_IDOL_ID to id)
            }
    }
}
