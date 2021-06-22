package kost.romi.timerforscreentimeout.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTimerDatabase(@ApplicationContext context: Context): TimerDatabase {
        return TimerDatabase.getInstance(context)
    }

    @Provides
    fun provideTimerDAO(timerDatabase: TimerDatabase): TimerDAO {
        return timerDatabase.timerDao()
    }

}