package com.example.moneymate.presentation.models

data class Exchange(
    val inputExchange: String,
    val outputExchange: String
){
    companion object{
        val DEFAULT = Exchange("RUB", "USD")
    }
}
