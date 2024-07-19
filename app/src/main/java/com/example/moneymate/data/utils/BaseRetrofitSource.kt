package com.example.moneymate.data.utils

import com.example.moneymate.data.models.responses.ErrorCurrencyResponseBody
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import java.io.IOException

open class BaseRetrofitSource(moshi: Moshi) {

    private val errorAdapter = moshi.adapter(ErrorCurrencyResponseBody::class.java)

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: JsonDataException) {
            throw ParseJsonException(e)
        } catch (e: JsonEncodingException) {
            throw ParseJsonException(e)
        } catch (e: HttpException) {
            throw createServerException(e)
        } catch (e: IOException) {
            throw ConnectionException(e)
        } catch (e: Exception) {
            throw e
        }
    }

    private fun createServerException(e: HttpException): Exception {
        return try {
            val errorBody: ErrorCurrencyResponseBody = errorAdapter.fromJson(
                e.response()!!.errorBody()!!.string()
            )!!
            BackendException(errorBody.toString())
        } catch (e: Exception) {
            println(e)
            throw ParseJsonException(e)
        }
    }

}