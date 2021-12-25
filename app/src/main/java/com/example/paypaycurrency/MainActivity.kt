package com.example.paypaycurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val currenciesList: List<String> = SUPPORTED_CURRENCIES.split(',')

    var recyclerView: RecyclerView? = null
    var editText: EditText? = null

    var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currenciesSpinner = findViewById<Spinner>(R.id.spinner)
        ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            currenciesList
        ).let { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currenciesSpinner.adapter = adapter
            currenciesSpinner.onItemSelectedListener = this
        }


        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.adapter = CurrenciesListAdapter()
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        editText = findViewById(R.id.edit_text)
        editText!!.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s.toString().toFloatOrNull()?.let { currencyAmount ->
                    (recyclerView!!.adapter as CurrenciesListAdapter).apply {
                        updateCurrencyAmount(currencyAmount)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    override fun onStart() {
        super.onStart()

        val observable = getCurrencyDataWithCache(application as MainApplication)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnSuccess {
                   it?.let { currencyData ->
                       (recyclerView!!.adapter as CurrenciesListAdapter).apply {
                           updateQuotes(currencyData.quotes)
                       }
                   }
            }
            .subscribe()

        disposable.add(observable)
    }

    private fun updateCurrencies(selectedCurrency: String) {
        (recyclerView!!.adapter as CurrenciesListAdapter).apply {
            updateSelectedCurrency(selectedCurrency)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateCurrencies(currenciesList[position])
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
        disposable = CompositeDisposable()
    }
}