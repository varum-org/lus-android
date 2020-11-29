package com.vtnd.lus.shared.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vtnd.lus.R
import com.vtnd.lus.di.GlideApp
import kotlinx.android.synthetic.main.progress_dialog_view.*

class ProgressDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
