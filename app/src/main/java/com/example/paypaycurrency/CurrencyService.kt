package com.example.paypaycurrency

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val API_KEY = "1eecccfeb39d06f006e10fb20b2f0859"
const val BASE_URL = "http://api.currencylayer.com/"
const val SUPPORTED_CURRENCIES = "USD,JPY,CNY,KRW"

data class CurrencyData(
    val success: Boolean,
    val terms: String,
    val privacy: String,
    val timestamp: Int,
    val source: String,
    val quotes: Quotes
)

data class Quotes(
    @SerializedName("USDUSD") val USD: Float,
    @SerializedName("USDJPY") val JPY: Float,
    @SerializedName("USDCNY") val CNY: Float,
    @SerializedName("USDKRW") val KRW: Float
)

interface CurrencyService {
    @GET("live?access_key=${API_KEY}&currencies=${SUPPORTED_CURRENCIES}&format=1")
    fun getCurrencyData(): Single<CurrencyData>
}

private var retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .build()

val currencyService: CurrencyService = retrofit.create(CurrencyService::class.java)

fun getCurrencyDataWithCache(application: MainApplication): Single<CurrencyData> {
    val currencyData = cache.getCurrencyData(application)
    return if (currencyData != null && !currencyData.isStale()) {
        Single.just(currencyData.data)
    } else {
        currencyService.getCurrencyData()
            .doOnSuccess {
                cache.putCurrencyData(it, application)
            }
    }
}