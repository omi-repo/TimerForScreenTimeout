package kost.romi.timerforscreentimeout.timerhistory

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kost.romi.timerforscreentimeout.data.TimerDataRepository
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimerHistoryViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val timerDataRepository: TimerDataRepository
) : ViewModel() {

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
            timerEntityLiveData.postValue(timerDataRepository.getHistory())
        }
    }
    
}