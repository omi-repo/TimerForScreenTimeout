package kost.romi.timerforscreentimeout.data

import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kotlinx.coroutines.Dispatchers
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

    fun getHistory() = timerDAO.getTimerHistory()

//    suspend fun getHistory() {
//        withContext(Dispatchers.IO) {
//            timerDAO.getTimerHistory()
//        }
//    }

}