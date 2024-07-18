package com.example.moneymate.presentation

import androidx.lifecycle.ViewModel
import com.example.moneymate.domain.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    fun getAllCurrencies() : List<String>{
        return currencyRepository.getAllCurrencies()
    }

}