package com.sanket.donatetoday

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DonateTodayApplication: Application() {
    override fun onCreate() {
        super.onCreate()

    }
}