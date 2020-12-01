package com.vtnd.lus.shared.extensions

import android.widget.Button
import com.google.android.material.button.MaterialButton

fun Button.setClickListenerToDialogButton(completion: (() -> Unit)?) {
    safeClick { completion?.invoke() }
}

fun MaterialButton.setClickListenerToDialogButton(completion: (() -> Unit)?) {
    safeClick { completion?.invoke() }
}
