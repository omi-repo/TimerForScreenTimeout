package kost.romi.timerforscreentimeout.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kost.romi.timerforscreentimeout.DATABASE_NAME
import java.util.*

@Entity(tableName = "$DATABASE_NAME")
class TimerEntity(
    @ColumnInfo(name = "dateTimerAt") var dateTimerAt: Long = 0,
    @ColumnInfo(name = "currentTime") var currentTime: Long = 0,
    @ColumnInfo(name = "startAt") var startAt: Long = 0,
    @ColumnInfo(name = "state") var state: TimerState = TimerState.READY,
    @ColumnInfo(name = "screenLockSwitch") var screenLockSwitch: Boolean = false,
    @ColumnInfo(name = "id")
@PrimaryKey(autoGenerate = true)
var id: Int = 0
) {
}