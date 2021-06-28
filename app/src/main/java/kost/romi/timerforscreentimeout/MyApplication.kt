package kost.romi.timerforscreentimeout

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/* @HiltAndroidApp triggers Hilt's code generation, including a base class for your application
 that serves as the application-level dependency container.
 This generated Hilt component is attached to the Application object's lifecycle and provides
  dependencies to it. Additionally, it is the parent component of the app, which means that other
   components can access the dependencies that it provides.*/
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // To initialize Timber
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }
}