package com.example.moneymate.domain

import com.example.moneymate.data.models.Currency

interface CurrencyRepository {
    suspend fun getAllCurrencies() : List<String>

    suspend fun convertCurrency(from: String, to: String, value: Float) : Currency
}