package com.example.moneymate.domain

interface CurrencyRepository {
    fun getAllCurrencies() : List<String>

    fun convertCurrency(currency: String, value: Float) : Float
}