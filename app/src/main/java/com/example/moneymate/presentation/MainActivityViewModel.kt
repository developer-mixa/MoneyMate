package com.example.moneymate.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymate.data.models.Currency
import com.example.moneymate.domain.CurrencyRepository
import com.example.moneymate.presentation.models.Container
import com.example.moneymate.presentation.models.ErrorContainer
import com.example.moneymate.presentation.models.Exchange
import com.example.moneymate.presentation.models.PendingContainer
import com.example.moneymate.presentation.models.SuccessContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private val _exchange: MutableLiveData<Exchange> = MutableLiveData(Exchange.DEFAULT)
    val exchange: LiveData<Exchange> = _exchange

    private val _exchangeValue: MutableLiveData<Container<Currency>> = MutableLiveData()
    val exchangeValue: LiveData<Container<Currency>> = _exchangeValue

    private val _allowCurrencies: MutableLiveData<Container<List<String>>> = MutableLiveData()
    val allowCurrencies: LiveData<Container<List<String>>> = _allowCurrencies

    init {
        getAllowCurrencies()
    }

    private var convertCurrencyJob: Job? = null

    fun getAllowCurrencies() = viewModelScope.launch {
        try {
            _allowCurrencies.value = PendingContainer()
            _allowCurrencies.value = SuccessContainer(currencyRepository.getAllCurrencies())
        }catch (e: Exception){
            _allowCurrencies.value = ErrorContainer(e)
        }
    }

    fun setInputExchange(exchange: String) {
        _exchange.value = _exchange.value?.copy(inputExchange = exchange)
    }

    fun setOutputExchange(exchange: String) {
        _exchange.value = _exchange.value?.copy(outputExchange = exchange)
    }

    fun convertCurrency(value: Float) = viewModelScope.launch {
        convertCurrencyJob?.cancel()

        convertCurrencyJob = launch {
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
    }

    fun swap() {
        _exchange.value = _exchange.value?.copy(
            inputExchange = _exchange.value!!.outputExchange,
            outputExchange = _exchange.value!!.inputExchange
        )
    }

}