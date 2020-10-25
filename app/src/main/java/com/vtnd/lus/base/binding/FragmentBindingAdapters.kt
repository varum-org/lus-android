package com.vtnd.lus.base.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.vtnd.lus.shared.constants.Constants
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.qualifier.named

/**
 * Binding adapters that work with a fragment instance should be here.
 */
class FragmentBindingAdapters(private val fragment: Fragment) : KoinComponent {
    private val baseUrl = get(named(Constants.KEY_BASE_URL)) as String

    @BindingAdapter(
        value = ["imageUrl", "imageDefault", "imageRequestListener"],
        requireAll = false
    )
    fun bindImage(
        imageView: ImageView,
        imageDefault: Int,
        url: String?,
        listener: RequestListener<Drawable?>?
    ) {
        Glide.with(fragment)
            .load(baseUrl + url)
            .listener(listener)
            .placeholder(imageDefault)
            .into(imageView)
    }
}
