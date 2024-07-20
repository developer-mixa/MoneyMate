package com.example.moneymate.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymate.domain.CurrencyRepository
import com.example.moneymate.presentation.models.Container
import com.example.moneymate.presentation.models.ErrorContainer
import com.example.moneymate.presentation.models.Exchange
import com.example.moneymate.presentation.models.PendingContainer
import com.example.moneymate.presentation.models.SuccessContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _exchange: MutableLiveData<Exchange> = MutableLiveData(Exchange.DEFAULT)
    val exchange: LiveData<Exchange> = _exchange

    private val _exchangeValue: MutableLiveData<Container<Float>> = MutableLiveData()
    val exchangeValue: LiveData<Container<Float>> = _exchangeValue

    fun setInputExchange(exchange: String) {
        _exchange.value = _exchange.value?.copy(inputExchange = exchange)
    }

    fun setOutputExchange(exchange: String) {
        _exchange.value = _exchange.value?.copy(outputExchange = exchange)
    }

    fun getAllCurrencies(): List<String> {
        return currencyRepository.getAllCurrencies()
    }

    fun convertCurrency(value: Float) = viewModelScope.launch {
        try {
            _exchangeValue.value = PendingContainer()

            val currency = currencyRepository.convertCurrency(
                _exchange.value!!.inputExchange,
                _exchange.value!!.outputExchange,
                value
            )
            _exchangeValue.value = SuccessContainer(currency)
        } catch (e: Exception) {
            _exchangeValue.value = ErrorContainer(e)
        }
    }

    fun swipe() {
        _exchange.value = _exchange.value?.copy(
            inputExchange = _exchange.value!!.outputExchange,
            outputExchange = _exchange.value!!.inputExchange
        )
    }

}