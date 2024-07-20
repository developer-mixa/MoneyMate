package com.example.moneymate.domain

import com.example.moneymate.data.models.responses.AllowCurrenciesResponse
import com.example.moneymate.data.models.responses.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyApi {

    @GET("exchangerates_data/latest")
    suspend fun convertCurrency(
        @Query("base") fromCurrency: String,
        @Header("apiKey") apiKey: String
    ): CurrencyResponse

    @GET("exchangerates_data/symbols")
    suspend fun getAllowCurrencies(@Header("apiKey") apiKey: String): AllowCurrenciesResponse

}