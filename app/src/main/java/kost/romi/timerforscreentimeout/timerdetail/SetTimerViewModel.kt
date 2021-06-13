package kost.romi.timerforscreentimeout.timerdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kost.romi.timerforscreentimeout.data.CurrTime
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.TimerState
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * ViewModel for SetTimerFragment.
 */
class SetTimerViewModel(val database: TimerDAO, application: Application) :
    AndroidViewModel(application) {

    var timerState: CurrTime = CurrTime()

    fun setupTimer() {
        timerState!!.dateTimerAt = 0
        timerState!!.currentTime = 0
        timerState!!.pausedAt = 0
        timerState!!.state = TimerState.READY
    }

    fun saveTimerToDB() {
        viewModelScope.launch {
            savetoDB(
                TimerEntity(
                    System.currentTimeMillis(),
                    timerState!!.currentTime,
                    timerState!!.pausedAt,
                    timerState!!.startAt,
                    timerState!!.state
                )
            )
        }
    }

    suspend fun savetoDB(timerEntity: TimerEntity) {
        withContext(Dispatchers.IO) {
            database.insertTimerToHistory(timerEntity)
        }
    }

}
