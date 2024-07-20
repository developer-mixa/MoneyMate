package com.example.moneymate.data.repositories

import android.content.Context
import android.widget.AutoCompleteTextView.Validator
import com.example.moneymate.BuildConfig
import com.example.moneymate.R
import com.example.moneymate.data.models.RetrofitConfig
import com.example.moneymate.data.utils.BaseRetrofitSource
import com.example.moneymate.data.utils.InvalidCurrencyException
import com.example.moneymate.domain.CurrencyApi
import com.example.moneymate.domain.CurrencyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCurrencyRepository @Inject constructor(
    private val retrofitConfig: RetrofitConfig
) : BaseRetrofitSource(retrofitConfig.moshi), CurrencyRepository {

    private val currencyApi = retrofitConfig.retrofit.create(CurrencyApi::class.java)

    override suspend fun getAllCurrencies(): List<String> {
        return currencyApi.getAllowCurrencies(BuildConfig.EXCHANGE_RATES_KEY).symbols.keys.toList()
    }

    override suspend fun convertCurrency(
        from: String,
        to: String,
        value: Float
    ): Float = wrapRetrofitExceptions {
        val currencyResponse = currencyApi.convertCurrency(from, BuildConfig.EXCHANGE_RATES_KEY)
        val currencyValue = currencyResponse.rates[to] ?: throw InvalidCurrencyException(
            "There is no currency with $to"
        )
        return@wrapRetrofitExceptions currencyValue * value
    }
}