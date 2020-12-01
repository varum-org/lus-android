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
import kotlinx.android.synthetic.main.custom_base_dialog_view.view.*

@SuppressLint("InflateParams")
class BaseAlertDialog(private val context: Context) {
    private var builder: AlertDialog.Builder
    private var alertDialog: AlertDialog
    private val dialogView by lazy {
        LayoutInflater.from(context).inflate(R.layout.custom_base_dialog_view, null)
    }
    private val titleTextView by lazy { dialogView.titleTextView }
    private val messageTextView by lazy { dialogView.messageTextView }
    private val leftButton by lazy { dialogView.leftButton }
    private val rightButton by lazy { dialogView.rightButton }

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

    fun message(msg: CharSequence) {
        messageTextView.text = msg
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

    fun rightButton(text: CharSequence, completion: (() -> Unit)? = null) = with(rightButton) {
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
