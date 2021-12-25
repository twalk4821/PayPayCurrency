package com.example.paypaycurrency

import android.content.SharedPreferences
import com.google.gson.Gson

interface CacheObject<T> {
    val maxAge: Int
    val timestamp: Int
    val data: T
    fun isStale(): Boolean {
        val now = System.currentTimeMillis() / 1000
        return now - timestamp > maxAge
    }
}

data class CurrencyDataCached(override val timestamp: Int,
                              override val maxAge: Int,
                              override val data: CurrencyData): CacheObject<CurrencyData>

const val CURRENCY_DATA_KEY = "CURRENCY_DATA_KEY"
// cache live 30min
const val CURRENCY_CACHE_MAX_AGE = 30 * 60

class CurrencyServiceCache {

    fun putCurrencyData(currencyData: CurrencyData, application: MainApplication) {
        val cacheObject = CurrencyDataCached(
            (System.currentTimeMillis() / 1000).toInt(),
            CURRENCY_CACHE_MAX_AGE,
            currencyData
        )

        Gson().toJson(cacheObject).let { json ->
            application.sharedPreferences.edit().apply {
                putString(CURRENCY_DATA_KEY, json)
                apply()
            }
        }
    }

    fun getCurrencyData(application: MainApplication): CurrencyDataCached? =
        application.sharedPreferences.getString(CURRENCY_DATA_KEY, null)?.let {
            Gson().fromJson(it, CurrencyDataCached::class.java)
        }

}

val cache = CurrencyServiceCache()