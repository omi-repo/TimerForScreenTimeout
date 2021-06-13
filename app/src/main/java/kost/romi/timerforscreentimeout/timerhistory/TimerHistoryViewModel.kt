package kost.romi.timerforscreentimeout.timerhistory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimerHistoryViewModel(val dataSource: TimerDAO, application: Application) :
    AndroidViewModel(application) {

    var timerEntityLiveData = MutableLiveData<List<TimerEntity?>>()

    init {
        initializeEntity()
    }

    fun initializeEntity() {
        viewModelScope.async {
            getHistory()
        }
    }

    suspend fun getHistory() {
        withContext(Dispatchers.IO) {
            timerEntityLiveData.postValue(dataSource.getTimerHistory())
        }
    }

//    private lateinit var entity

//    var entity = dataSource.getAllTimerHistory()

}