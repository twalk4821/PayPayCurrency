package com.example.paypaycurrency

fun getConversionRate(targetCurrency: String, quotes: Quotes): Float =
    when (targetCurrency) {
        "CNY" -> quotes.CNY
        "JPY" -> quotes.JPY
        "KRW" -> quotes.KRW
        else -> quotes.USD
    }

fun getConversion(
    amount: Float,
    baseCurrency: String,
    targetCurrency: String,
    quotes: Quotes
): Float {
    if (baseCurrency == targetCurrency) {
        return amount
    }

    return if (targetCurrency != "USD") {
        val amountInDollars = getConversion(amount, baseCurrency, "USD", quotes)
        val conversionRate = getConversionRate(targetCurrency, quotes)
        amountInDollars * conversionRate
    } else {
        val conversionRate = getConversionRate(baseCurrency, quotes)
        amount * 1 / conversionRate
    }
}