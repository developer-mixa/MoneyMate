package com.example.moneymate.data.models.responses

data class CurrencyResponse(
    val base: String,
    val rates: Map<String, Float>
)