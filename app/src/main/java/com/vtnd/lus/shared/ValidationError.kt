package com.vtnd.lus.shared

import android.net.Uri
import android.util.Patterns
import com.vtnd.lus.shared.type.ValidateErrorType.*

typealias ValidateResult = Pair<Boolean, ErrorType<String?>>

class ValidateError {

    fun validateEmail(email: String?) = when {
        email.isNullOrBlank() ->
            ValidateResult(false, EmailErrorType.EMAIL_EMPTY)
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
            ValidateResult(false, EmailErrorType.INVALID_EMAIL)
        else -> ValidateResult(true, EmailErrorType.NONE)
    }

    fun validatePassword(password: String?) = when {
        password.isNullOrBlank() ->
            ValidateResult(false, PasswordErrorType.PASSWORD_EMPTY)
        password.length < LEAST_NUMBER_CHARACTER ->
            ValidateResult(false, PasswordErrorType.LEAST_CHARACTER)
        else -> ValidateResult(true, PasswordErrorType.NONE)
    }

    fun validateUserName(userName: String?) = when {
        userName.isNullOrBlank() ->
            ValidateResult(false, UserNameErrorType.USER_NAME_EMPTY)
        userName.length < LEAST_NUMBER_CHARACTER ->
            ValidateResult(false, UserNameErrorType.LEAST_CHARACTER)
        else -> ValidateResult(true, UserNameErrorType.NONE)
    }

    fun validateEmpty(value: String?) = when {
        value.isNullOrBlank() -> ValidateResult(false, BaseErrorType.IS_EMPTY)
        else -> ValidateResult(true, BaseErrorType.NONE)
    }

    fun validateUriEmpty(value: Uri?) = when (value) {
        null -> ValidateResult(false, BaseErrorType.IS_EMPTY)
        else -> ValidateResult(true, BaseErrorType.NONE)
    }

    fun validatePrice(value: Double?) = when (value) {
        null -> ValidateResult(false, BaseErrorType.IS_EMPTY)
        else -> ValidateResult(true, BaseErrorType.NONE)
    }

    fun validatePhone(phone: String?) = when {
        phone.isNullOrBlank() ->
            ValidateResult(false, PhoneErrorType.PHONE_EMPTY)
        !Patterns.PHONE.matcher(phone).matches() ->
            ValidateResult(false, PhoneErrorType.INVALID_PHONE)
        else -> ValidateResult(true, PhoneErrorType.NONE)
    }

    companion object {
        const val LEAST_NUMBER_CHARACTER = 6
    }
}
