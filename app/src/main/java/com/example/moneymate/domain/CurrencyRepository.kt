package com.example.moneymate.domain

interface CurrencyRepository {
    fun getAllCurrencies() : List<String>

    suspend fun convertCurrency(from: String, to: String, value: Float) : Float
}