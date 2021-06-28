package kost.romi.timerforscreentimeout.data

import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerDataRepository @Inject constructor(
    private val timerDAO: TimerDAO
) {

    suspend fun saveToDB(timerEntity: TimerEntity) {
        withContext(Dispatchers.IO) {
            timerDAO.insertTimerToHistory(timerEntity)
        }
    }

    val getAllHistory: Flow<List<TimerEntity>> = timerDAO.getAllTimerHistory()

    suspend fun clearCountdownHistory() {
        withContext(Dispatchers.IO) {
            timerDAO.clearEverythingInTimerHistory()
            timerDAO.updateIdToZero(0)
        }
    }

    suspend fun deleteTable(timerEntity: TimerEntity) {
        withContext(Dispatchers.IO) {
            timerDAO.deleteTable(timerEntity)
        }
    }

}