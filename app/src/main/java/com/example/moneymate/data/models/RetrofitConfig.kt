package com.example.moneymate.data.models

import com.squareup.moshi.Moshi
import retrofit2.Retrofit

data class RetrofitConfig(
    val retrofit: Retrofit,
    val moshi: Moshi
)