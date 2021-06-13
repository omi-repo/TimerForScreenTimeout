package kost.romi.timerforscreentimeout

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // To initialize Timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())



    }
}