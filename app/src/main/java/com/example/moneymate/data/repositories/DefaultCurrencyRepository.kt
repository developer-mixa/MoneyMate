package com.example.moneymate.data.repositories

import com.example.moneymate.BuildConfig
import com.example.moneymate.data.models.Currency
import com.example.moneymate.data.models.RetrofitConfig
import com.example.moneymate.data.utils.BaseRetrofitSource
import com.example.moneymate.data.utils.InvalidCurrencyException
import com.example.moneymate.domain.CurrencyApi
import com.example.moneymate.domain.CurrencyRepository
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
    ): Currency = wrapRetrofitExceptions {
        val currencyResponse = currencyApi.convertCurrency(from, BuildConfig.EXCHANGE_RATES_KEY)
        val currencyValue = currencyResponse.rates[to] ?: throw InvalidCurrencyException(
            "There is no such currency: $to"
        )
        return@wrapRetrofitExceptions Currency(currencyValue * value, currencyResponse.date)
    }
}