package kost.romi.timerforscreentimeout.data.source.local

import androidx.room.*
import kost.romi.timerforscreentimeout.DATABASE_NAME
import kost.romi.timerforscreentimeout.data.TimerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDAO {
    /**
     * Get all item in table.
     */
    @Query("SELECT * FROM $DATABASE_NAME ORDER BY id DESC")
    fun getAllTimerHistory(): Flow<List<TimerEntity>>

    /**
     * Insert a new item to table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimerToHistory(timerEntity: TimerEntity)

    /**
     * Delete everything in table.
     */
    @Query("DELETE FROM $DATABASE_NAME")
    suspend fun clearEverythingInTimerHistory(): Int

    @Query("UPDATE $DATABASE_NAME SET id=:idInt WHERE id != 0")
    suspend fun updateIdToZero(idInt: Int)

    @Delete
    suspend fun deleteTable(timerEntity: TimerEntity)
}