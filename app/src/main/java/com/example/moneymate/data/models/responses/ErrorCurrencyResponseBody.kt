package com.example.moneymate.data.models.responses

data class ErrorCurrencyResponseBody(
    val error: Error
)

data class Error(
    val code: String,
    val message: String
)