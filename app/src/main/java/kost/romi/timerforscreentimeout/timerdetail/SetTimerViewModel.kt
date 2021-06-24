package kost.romi.timerforscreentimeout.timerdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kost.romi.timerforscreentimeout.data.CurrTime
import kost.romi.timerforscreentimeout.data.TimerDataRepository
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.TimerState
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for SetTimerFragment.
 */
@HiltViewModel
class SetTimerViewModel @Inject internal constructor(
    savedStateHandle: SavedStateHandle,
    private val timerDataRepository: TimerDataRepository
) : ViewModel() {

    var timerState: CurrTime = CurrTime()

    fun setupTimer() {
        timerState.dateTimerAt = 0
        timerState.currentTime = 0
        timerState.pausedAt = 0
        timerState.state = TimerState.READY
    }


    fun saveTimerToDB() {
        viewModelScope.launch {
            timerDataRepository.saveToDB(
                TimerEntity(
                    System.currentTimeMillis(),
                    timerState.currentTime,
                    timerState.pausedAt,
                    timerState.startAt,
                    timerState.state
                )
            )
        }
    }

}
