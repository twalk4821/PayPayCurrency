package com.example.paypaycurrency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CurrenciesListAdapter: RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {

    private var selectedCurrency = "USD"
    private var selectedAmount = 1F
    private var quotes: Quotes? = null
    private var currenciesList: List<String> = currenciesListGetter()
        get() = currenciesListGetter()

    private fun currenciesListGetter() = SUPPORTED_CURRENCIES.split(',').filter {
        it != selectedCurrency
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyLabel: TextView = view.findViewById(R.id.currency_label)
        val currencyAmount: TextView = view.findViewById(R.id.currency_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.currencies_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currenciesList[position]
        holder.currencyLabel.text = currency
        quotes?.let { liveQuotes ->
            holder.currencyAmount.text = getConversion(selectedAmount, selectedCurrency, currency, liveQuotes).toString()
        }
    }

    override fun getItemCount() = currenciesList.size

    fun updateSelectedCurrency(currency: String) {
        selectedCurrency = currency
        notifyDataSetChanged()
    }

    fun updateCurrencyAmount(amount: Float) {
        selectedAmount = amount
        notifyDataSetChanged()
    }

    fun updateQuotes(newQuotes: Quotes) {
        quotes = newQuotes
        notifyDataSetChanged()
    }

}