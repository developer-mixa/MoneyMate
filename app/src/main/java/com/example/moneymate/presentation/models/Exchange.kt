package com.example.moneymate.presentation.models

data class Exchange(
    val inputExchange: String,
    val outputExchange: String,
    val date: String
){
    companion object{
        val DEFAULT = Exchange("USD", "RUB", "")
    }
}
