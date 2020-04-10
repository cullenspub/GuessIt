package com.example.android.guesstheword

import android.app.Application
import timber.log.Timber

class GameApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}