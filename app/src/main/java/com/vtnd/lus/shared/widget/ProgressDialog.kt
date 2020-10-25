package com.vtnd.lus.shared.widget

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.vtnd.lus.R

class ProgressDialog(context: Context): Dialog(context) {

    init {
        initView()
    }

    private fun initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.progress_dialog_view)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }
}
