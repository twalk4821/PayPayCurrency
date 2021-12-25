package com.example.paypaycurrency

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CurrencyServiceTest {
    val quotes = Quotes(
        1F,
        114.4F,
        6.3F,
        1186.27F
    )

    @Test
    fun does_conversion_from_USD() {
        val result = getConversion(1F, "USD", "JPY", quotes)
        assertEquals(result, 114.4F)
    }

    @Test
    fun does_conversion_to_USD() {
        val result = getConversion(114.4F, "JPY", "USD", quotes)
        assertEquals(result, 1F)
    }

    @Test
    fun does_conversion_from_USD_to_USD() {
        val result = getConversion(1F, "USD", "USD", quotes)
        assertEquals(result, 1F)
    }

    @Test
    fun does_conversion_from_non_USD_to_non_USD() {
        val result = getConversion(114.4F, "JPY", "CNY", quotes)
        assertEquals(result, 6.3F)
    }
}