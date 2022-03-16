package com.example.lululemonassessment

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LuluApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialize Rudder SDK here
    }
}
