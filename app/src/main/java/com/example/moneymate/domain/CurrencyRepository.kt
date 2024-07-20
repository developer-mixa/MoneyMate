package com.example.moneymate.domain

interface CurrencyRepository {
    suspend fun getAllCurrencies() : List<String>

    suspend fun convertCurrency(from: String, to: String, value: Float) : Float
}