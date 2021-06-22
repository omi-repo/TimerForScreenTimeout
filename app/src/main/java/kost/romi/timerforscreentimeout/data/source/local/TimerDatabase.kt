package kost.romi.timerforscreentimeout.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kost.romi.timerforscreentimeout.DATABASE_NAME
import kost.romi.timerforscreentimeout.data.TimerEntity

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [TimerEntity::class], version = 1, exportSchema = false)
abstract class TimerDatabase : RoomDatabase() {

    abstract fun timerDao(): TimerDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var instance: TimerDatabase? = null

        fun getInstance(context: Context): TimerDatabase {
            synchronized(this) {
                var instance = instance

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimerDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    this.instance = instance
                }
                return instance
            }
        }

    }

}