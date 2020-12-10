package com.vtnd.lus.shared.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.vtnd.lus.R
import com.vtnd.lus.data.model.Service
import com.vtnd.lus.shared.extensions.indexOf
import com.vtnd.lus.shared.extensions.setClickListenerToDialogButton
import com.vtnd.lus.shared.extensions.toast
import kotlinx.android.synthetic.main.custom_add_service_dialog_view.view.*

@SuppressLint("InflateParams")
class AddServiceAlertDialog(private val context: Context) : AdapterView.OnItemSelectedListener {
    private var builder: AlertDialog.Builder
    private var alertDialog: AlertDialog
    private val dialogView by lazy {
        LayoutInflater.from(context).inflate(R.layout.custom_add_service_dialog_view, null)
    }
    private val titleTextView by lazy { dialogView.titleTextView }
    private val editText by lazy { dialogView.priceServiceEdit }
    private val servicesSpinner by lazy { dialogView.spinnerServices }
    private val leftButton by lazy { dialogView.leftButton }
    private val rightButton by lazy { dialogView.rightButton }
    private lateinit var arrayAdapter: ArrayAdapter<String?>
    private lateinit var services: List<Service>
    private lateinit var service: Service

    init {
        servicesSpinner.onItemSelectedListener = this
        builder = AlertDialog.Builder(context).apply {
            setView(dialogView)
        }
        alertDialog = builder.create()
        alertDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        service = services[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun initAddService(services: List<Service>?, oldService: Service) {
        if (services != null) {
            this.services = services
            arrayAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                services.map { it.serviceName })
            servicesSpinner.adapter = arrayAdapter
            oldService.serviceCode?.let { serviceCode ->
                servicesSpinner.setSelection(
                    services.indexOf { it.serviceCode == serviceCode },
                    true
                )
            }
        }
        editText.setText(oldService.servicePrice?.toString() ?: "")
    }

    fun title(title: CharSequence) {
        titleTextView.text = title
    }

    fun leftButton(text: CharSequence, completion: (() -> Unit)? = null) = with(leftButton) {
        this.text = text
        setClickListenerToDialogButton {
            completion?.invoke()
            alertDialog.dismiss()
        }
    }

    fun rightButton(
        text: CharSequence,
        onDateSelected: (Service) -> Unit
    ) = with(rightButton) {
        this.text = text
        setClickListenerToDialogButton {
            if (editText.text.isNullOrBlank()) {
                context.toast(context.getString(R.string.please_enter_your_price))
            } else {
                onDateSelected.invoke(service.copy(servicePrice = editText.text.toString().toInt()))
                alertDialog.dismiss()
            }
        }
    }

    fun show() {
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }
}
