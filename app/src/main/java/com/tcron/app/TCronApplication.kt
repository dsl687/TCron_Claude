package com.tcron.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TCronApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}