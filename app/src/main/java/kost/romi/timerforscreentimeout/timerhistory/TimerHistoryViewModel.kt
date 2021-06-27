package kost.romi.timerforscreentimeout.timerhistory

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kost.romi.timerforscreentimeout.data.TimerDataRepository
import kost.romi.timerforscreentimeout.data.TimerEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerHistoryViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val timerDataRepository: TimerDataRepository
) : ViewModel() {

    val getAllHistory: LiveData<List<TimerEntity>> = timerDataRepository.getAllHistory.asLiveData()

    fun clearCountdownHistory() {
        viewModelScope.launch {
            timerDataRepository.clearCountdownHistory()
        }
    }

}