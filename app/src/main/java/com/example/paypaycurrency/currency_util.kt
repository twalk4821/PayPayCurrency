package com.example.paypaycurrency

// base currency for API request is USD,
// so conversion rates are relative to USD
fun getConversionRate(targetCurrency: String, quotes: Quotes): Float =
    when (targetCurrency) {
        "CNY" -> quotes.CNY
        "JPY" -> quotes.JPY
        "KRW" -> quotes.KRW
        else -> quotes.USD
    }

// get the converted amount in targetCurrency using
// the conversion rate for baseCurrency. Since conversion
// rates are relative to USD, first convert the amount to USD (if it's not
// already), then convert to the target currency.
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