package kost.romi.timerforscreentimeout.timerdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kost.romi.timerforscreentimeout.data.CurrTime
import kost.romi.timerforscreentimeout.data.TimerState

class SetTimerViewModel : ViewModel() {

    var timerState: MutableLiveData<CurrTime> = MutableLiveData()

    init {
        timerState.value = CurrTime()
    }

    fun setupTimer() {
        timerState.value!!.lastTime = 0
        timerState.value!!.currentTime = 0
        timerState.value!!.state = TimerState.READY
    }

}