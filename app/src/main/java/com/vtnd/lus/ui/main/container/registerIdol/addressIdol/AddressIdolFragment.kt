package com.vtnd.lus.ui.main.container.registerIdol.addressIdol

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseFragment
import com.vtnd.lus.databinding.FragmentAddressIdolBinding
import com.vtnd.lus.shared.extensions.*
import com.vtnd.lus.shared.liveData.observeLiveData
import com.vtnd.lus.ui.main.container.registerIdol.RegisterIdolViewModel
import kotlinx.android.synthetic.main.fragment_address_idol.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

@Suppress("DEPRECATION")
class AddressIdolFragment : BaseFragment<FragmentAddressIdolBinding, RegisterIdolViewModel>() {
    private var _googleMap: GoogleMap? = null
    private var marker: Marker? = null
    private var autocompleteSupportFragment: AutocompleteSupportFragment? = null

    override val viewModel by lazy(LazyThreadSafetyMode.NONE) { requireParentFragment().getViewModel<RegisterIdolViewModel>() }

    override fun inflateViewBinding(inflater: LayoutInflater) =
        FragmentAddressIdolBinding.inflate(inflater)

    @SuppressLint("MissingPermission") // Already ensured permission
    override fun initialize() {
        setupDismissKeyBoard(activity, addressLayout)
        lifecycleScope.launch {
            if (requestLocationPermission()) {
                viewModel.getCurrentLocation()
            } else requireActivity().toast("You need grant location permission to retrieve current location!")
        }
        currentLocationFab.setOnClickListener {
            lifecycleScope.launch {
                if (requestLocationPermission()) {
                    viewModel.getCurrentLocation()
                } else requireActivity().toast("You need grant location permission to retrieve current location!")
            }
        }
        setupMapFragment()
        setupAutoCompleteFragment()
    }

    override fun registerLiveData() = with(viewModel) {
        super.registerLiveData()
        locationLiveData.observeLiveData(viewLifecycleOwner) {
            moveCameraToLocation(it)
        }
        addressLiveData.observeLiveData(viewLifecycleOwner) {
            autocompleteSupportFragment?.setText(it)
        }
    }

    override fun onStop() {
        onHideSoftKeyBoard()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _googleMap?.setOnMapLongClickListener(null)
        _googleMap = null
    }


    private fun setupMapFragment() {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).apply {
            if (_googleMap === null) {
                getMapAsync { googleMap ->
                    _googleMap = googleMap
                    googleMap.uiSettings.run {
                        isCompassEnabled = true
                        isZoomControlsEnabled = true
                    }
                    googleMap.setOnMapLongClickListener { latLng ->
                        requireActivity().showAlertDialog {
                            title("Select clicked address")
                            message("Do you want to select the recently clicked address on the map?")
                            negativeAction("Cancel") { _, _ -> }
                            positiveAction("OK") { _, _ ->
                                viewModel.setLocation(
                                    DomainLocation(
                                        latitude = latLng.latitude,
                                        longitude = latLng.longitude
                                    ),
                                    null
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAutoCompleteFragment() {
        autocompleteSupportFragment =
            (childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment)
        autocompleteSupportFragment?.run {
            setPlaceFields(
                listOf(
                    Place.Field.LAT_LNG,
                    Place.Field.NAME,
                    Place.Field.ADDRESS
                )
            )
            setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    val latLng = place.latLng
                        ?: return Timber.i("Error%s", "Cannot get latitude and longitude")
                    viewModel.setLocation(
                        DomainLocation(
                            latitude = latLng.latitude,
                            longitude = latLng.longitude
                        ),
                        place.address
                    )
                }

                override fun onError(status: Status) {
                    if (status != Status.RESULT_CANCELED) {
                        Timber.i("Error%s", status.statusMessage)
                    }
                }
            })
        }
    }

    private fun moveCameraToLocation(domainLocation: DomainLocation) {
        _googleMap?.run {
            val latLng = LatLng(domainLocation.latitude, domainLocation.longitude)
            marker?.remove()
            marker = MarkerOptions()
                .position(latLng)
                .title("Picked location")
                .apply {
                    requireContext().getDrawableBy(id = R.drawable.ic_location_bold)
                        ?.toBitmap()
                        ?.let { icon(BitmapDescriptorFactory.fromBitmap(it)) }
                }.let(::addMarker)
            CameraUpdateFactory.newLatLngZoom(latLng, 17f).let(::moveCamera)
        }
    }

    companion object {
        fun newInstance() = AddressIdolFragment()
    }
}
