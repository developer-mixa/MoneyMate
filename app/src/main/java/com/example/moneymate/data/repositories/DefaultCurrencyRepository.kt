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
    @ApplicationContext private val appContext: Context,
    private val retrofitConfig: RetrofitConfig
) : BaseRetrofitSource(retrofitConfig.moshi), CurrencyRepository {

    private val currencyApi = retrofitConfig.retrofit.create(CurrencyApi::class.java)

    override fun getAllCurrencies(): List<String> {
        val inputStream = appContext.resources.openRawResource(R.raw.common_currency)
        val bytes = ByteArray(inputStream.available())

        inputStream.read(bytes)

        val currencies = ArrayList<String>()
        val jsonCurrencies = JSONObject(String(bytes)).names()

        if (jsonCurrencies != null) {
            for (i in 0 until jsonCurrencies.length()) {
                currencies.add(jsonCurrencies.getString(i))
            }
        }

        return currencies
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