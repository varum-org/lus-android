package com.vtnd.lus.shared.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.vtnd.lus.R
import com.vtnd.lus.shared.extensions.setClickListenerToDialogButton
import kotlinx.android.synthetic.main.custom_notification_dialog_view.view.*

@SuppressLint("InflateParams")
class NotificationAlertDialog(private val context: Context) {
    private var builder: AlertDialog.Builder
    private var alertDialog: AlertDialog
    private val dialogView by lazy {
        LayoutInflater.from(context).inflate(R.layout.custom_notification_dialog_view, null)
    }
    private val iconImage by lazy { dialogView.iconImage }
    private val statusText by lazy { dialogView.statusText }
    private val button by lazy { dialogView.button }

    init {
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

    fun icon(id: Int) {
        iconImage.setBackgroundResource(id)
    }

    fun statusMessage(status: CharSequence) {
        statusText.text = status
    }

    fun button(text: CharSequence, completion: (() -> Unit)? = null) = with(button) {
        this.text = text
        setClickListenerToDialogButton {
            completion?.invoke()
            alertDialog.dismiss()
        }
    }

    fun show() {
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog.dismiss()
    }
}
