package com.vtnd.lus.shared.extensions

import android.widget.Button

fun Button.setClickListenerToDialogButton(completion: (() -> Unit)?) {
    safeClick { completion?.invoke() }
}
