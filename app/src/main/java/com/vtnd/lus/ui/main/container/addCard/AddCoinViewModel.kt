package com.vtnd.lus.ui.main.container.addCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vtnd.lus.base.BaseViewModel
import com.vtnd.lus.data.UserRepository
import com.vtnd.lus.shared.ValidateError
import com.vtnd.lus.shared.liveData.SingleLiveData
import com.vtnd.lus.shared.type.ValidateErrorType

class AddCoinViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    private val validateInput = ValidateError()
    val addCoinLiveData = SingleLiveData<Any>()
    private val _coinNameError =
        MutableLiveData<ValidateErrorType.BaseErrorType>()
    val coinNameError: LiveData<ValidateErrorType.BaseErrorType> get() = _coinNameError

    fun addCoin(coin: String?) {
        val coinValidate = validateInput.validateBase(coin)
        _coinNameError.value = coinValidate.second as ValidateErrorType.BaseErrorType
        if (coinValidate.first) {
            viewModelScope(addCoinLiveData,
                onRequest = { userRepository.addCoin(coin!!.toInt()) },
                onSuccess = { addCoinLiveData.postValue(it) },
                onError = { exception.postValue(it) })
        }
    }
}
