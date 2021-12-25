package com.example.paypaycurrency

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"

class MainApplication: Application() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    }
}