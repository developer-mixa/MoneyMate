package com.example.moneymate.data

import android.content.Context
import com.example.moneymate.R
import com.example.moneymate.domain.CurrencyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCurrencyRepository @Inject constructor(
    @ApplicationContext private val appContext: Context
) : CurrencyRepository {

    override fun getAllCurrencies(): List<String> {
        val inputStream = appContext.resources.openRawResource(R.raw.common_currency)
        val bytes = ByteArray(inputStream.available())

        inputStream.read(bytes)

        val currencies = ArrayList<String>()
        val jsonCurrencies = JSONObject(String(bytes)).names()

        if (jsonCurrencies != null) {
            for (i in 0 until jsonCurrencies.length()){
                currencies.add(jsonCurrencies.getString(i))
            }
        }

        return currencies
    }

    override fun convertCurrency(currency: String, value: Float): Float {
        return 0.2f
    }
}