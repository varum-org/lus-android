package com.vtnd.lus.shared

import android.content.Context
import androidx.preference.PreferenceManager
import com.vtnd.lus.R
import java.util.*

object LocaleHelper {

    private const val KEY_SELECTED_LANGUAGE = "KEY_SELECTED_LANGUAGE"

    fun onAttach(context: Context): Context? {
        val lang = getPersistedData(context, context.getString(R.string.locate))
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String): Context? {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String?): Context? {
        persist(context, language)
        return updateResources(context, language)
    }

    private fun getPersistedData(context: Context,
                                 defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(KEY_SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(KEY_SELECTED_LANGUAGE, language)
        editor.apply()
    }

    private fun updateResources(context: Context, language: String?) =
        language?.run {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val configuration = context.resources.configuration
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        }
}
