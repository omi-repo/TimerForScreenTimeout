package kost.romi.timerforscreentimeout.timerdetail

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.CountDownTimer
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kost.romi.timerforscreentimeout.data.CurrTime
import kost.romi.timerforscreentimeout.data.TimerState
import timber.log.Timber
import kotlin.coroutines.coroutineContext

class SetTimerViewModel : ViewModel() {

    var timerState: MutableLiveData<CurrTime> = MutableLiveData()
    init {
        timerState.value = CurrTime()
    }

    fun setupTimer() {
        timerState.value!!.time = 0
        timerState.value!!.minutes = 0
        timerState.value!!.seconds = 0
        timerState.value!!.miliseconds = 0
        timerState.value!!.state = TimerState.READY
    }

}