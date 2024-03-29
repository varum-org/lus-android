package com.vtnd.lus.shared.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity2
import com.vtnd.lus.shared.AnimateType
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import kotlin.math.roundToInt


fun AppCompatActivity.replaceFragmentInActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE
) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        replace(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.addFragmentToActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType? = AnimateType.FADE
) {
    supportFragmentManager.transact({
        if (addToBackStack) {
            addToBackStack(tag)
        }
        add(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.goBackFragment(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
    if (isShowPreviousPage) {
        supportFragmentManager.popBackStackImmediate()
    }
    return isShowPreviousPage
}

fun AppCompatActivity.startActivity(
    @NonNull intent: Intent, flags: Int? = null
) {
    flags.notNull {
        intent.flags = it
    }
    startActivity(intent)
}

fun AppCompatActivity.startActivityForResult(
    @NonNull intent: Intent, requestCode: Int, flags: Int? = null
) {
    flags.notNull {
        intent.flags = it
    }
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.isExistFragment(fragment: Fragment): Boolean {
    return supportFragmentManager.findFragmentByTag(fragment::class.java.simpleName) != null
}

fun AppCompatActivity.switchFragment(
    @IdRes containerId: Int,
    currentFragment: Fragment,
    newFragment: Fragment,
    addToBackStack: Boolean = false,
    tag: String = newFragment::class.java.simpleName
) {
    supportFragmentManager.transact({
        setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
        if (isExistFragment(newFragment)) {
            hide(currentFragment).show(
                newFragment
            )
        } else {
            hide(currentFragment)
            if (addToBackStack) {
                addToBackStack(tag)
            }
            add(containerId, newFragment, tag)
        }
    })
}

fun AppCompatActivity.removeFragment(
    tag: String?, isPopBackStack: Boolean = true
) {
    supportFragmentManager.findFragmentByTag(tag).notNull {
        supportFragmentManager.beginTransaction().apply {
            remove(it)
            commit()
        }
        if (isPopBackStack) supportFragmentManager.popBackStack()
    }
}

fun AppCompatActivity.clearAllFragment() {
    repeat((0..supportFragmentManager.backStackEntryCount).count()) { supportFragmentManager.popBackStack() }
}

fun Application.getContext(): Context {
    this.resources.isNull { return this }
    val config = Configuration(this.resources.configuration)
    return this.createConfigurationContext(config)
}

fun Activity.showError(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(findViewById(android.R.id.content), message, duration).show()
}

fun AppCompatActivity.handleDefaultApiError(apiError: Exception,unauthorized: AppCompatActivity.() -> Unit) {
        when (apiError) {
            is HttpException -> {
                Timber.i("aaaa")
                getErrorMessage(apiError,unauthorized)?.let {
                    Timber.i(it)
                    showError(it)
                }
            }
            is SocketTimeoutException -> {
                showError(message = getString(R.string.msg_error_time_out))
            }
            is IOException -> {
                showError(message = getString(R.string.msg_error_no_internet))
            }
            else -> {
                showError(message = getString(R.string.msg_error_data_parse))
            }
        }
}

fun AppCompatActivity.getErrorMessage(e: Exception,unauthorized: AppCompatActivity.() -> Unit): String? {
    val responseBody = (e as HttpException).response()?.errorBody()
    val errorCode = e.response()?.code()
    if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
        unauthorized.invoke(this)
        // TODO reLogin
    }

    return responseBody?.let {
        try {
            // Handle get message error when request api, depend on format json api
            val jsonObject = JSONObject(responseBody.string())
            val message = jsonObject.getString("messages")
            Timber.i(message)
            if (!message.isNullOrBlank())
                message else getString(R.string.msg_error_data_parse)
        } catch (ex: Exception) {
            e.message
        }
    } ?: getString(R.string.msg_error_data_parse)
}

@Suppress("DEPRECATION")
fun Activity.transparentStatusBar(isTransparent: Boolean) {
    window.apply {
        val statusBarColor = statusBarColor
        if (isTransparent) {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                (decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        } else {
            this.statusBarColor = statusBarColor
            decorView.systemUiVisibility =
                (decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}

fun Activity.getHeightStatusBar() = Rect().run {
    window.decorView.getWindowVisibleDisplayFrame(this)
    this.top
}


fun Context.dpToPx(dp: Int) = this.run {
    (dp * (resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

@Suppress("nothing_to_inline")
@ColorInt
inline fun Context.getColorBy(@ColorRes id: Int) = ContextCompat.getColor(this, id)

@Suppress("nothing_to_inline")
inline fun Context.getDrawableBy(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)


@Suppress("nothing_to_inline")
inline fun Context.toast(
    @StringRes messageRes: Int,
    short: Boolean = true
) = this.toast(getString(messageRes), short)

@Suppress("nothing_to_inline")
inline fun Context.toast(
    message: String,
    short: Boolean = true
) =
    Toast.makeText(
        this,
        message,
        if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    ).apply { show() }!!
