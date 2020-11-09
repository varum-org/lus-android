package com.vtnd.lus.shared.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.vtnd.lus.R
import com.vtnd.lus.base.BaseActivity
import com.vtnd.lus.shared.AnimateType
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

fun AppCompatActivity.replaceFragmentInActivity(
    @IdRes containerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = true,
    tag: String = fragment::class.java.simpleName,
    animateType: AnimateType = AnimateType.FADE
) {
    supportFragmentManager.transact({
        if (addToBackStack) addToBackStack(tag)
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
        if (addToBackStack) addToBackStack(tag)
        add(containerId, fragment, tag)
    }, animateType)
}

fun AppCompatActivity.goBackFragment(): Boolean {
    val isShowPreviousPage = supportFragmentManager.backStackEntryCount > 0
    if (isShowPreviousPage) supportFragmentManager.popBackStackImmediate()
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
            if (addToBackStack) addToBackStack(tag)
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
    Snackbar.make(findViewById(R.id.content), message, duration).show()
}

fun BaseActivity<*, *>?.handleDefaultApiError(apiError: Exception) {
    this?.let {
        when (apiError) {
            is HttpException -> {
                getErrorMessage(apiError)?.let {
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
}

fun BaseActivity<*, *>.getErrorMessage(e: Exception): String? {
    val responseBody = (e as HttpException).response()?.errorBody()
    val errorCode = e.response()?.code()
    if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
        // TODO reLogin
    }

    return responseBody?.let {
        try {
            // Handle get message error when request api, depend on format json api
            val jsonObject = JSONObject(responseBody.string())
            val message = jsonObject.getString("messages")
            if (!message.isNullOrBlank()) {
                message
            } else {
                getString(R.string.msg_error_data_parse)
            }
        } catch (ex: Exception) {
            e.message
        }
    } ?: kotlin.run {
        getString(R.string.msg_error_data_parse)
    }
}
