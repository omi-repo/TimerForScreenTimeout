package kost.romi.timerforscreentimeout.timerdetail

import android.os.CountDownTimer
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kost.romi.timerforscreentimeout.data.CurrTime
import kost.romi.timerforscreentimeout.data.TimerDataRepository
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
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

    fun saveTimerToDB(
        currentTime: Long = 0,
        startTime: Long = 0,
        state: TimerState = TimerState.READY
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            timerDataRepository.saveToDB(
                TimerEntity(
                    System.currentTimeMillis(),
                    currentTime,
                    startTime,
                    state
                )
            )
        }
    }

    private var _startTimerBoolean = MutableLiveData<Boolean>(false)
    val startTimerBoolean: LiveData<Boolean>
        get() = _startTimerBoolean

    fun setStartTimerBoolean(bool: Boolean) {
        _startTimerBoolean.value = bool
    }

    private var _onTickBoolean = MutableLiveData<Boolean>(false)
    val onTickBoolean: LiveData<Boolean>
        get() = _onTickBoolean

    fun onTickBoolean(bool: Boolean) {
        _onTickBoolean.value = bool
    }

    private lateinit var timer: CountDownTimer
    private var _onTickValueString = MutableLiveData<String>("")
    val onTickValueString: LiveData<String>
        get() = _onTickValueString
    private var _millisOnCountDownTimer = MutableLiveData<Long>(0)
    val millisOnCountDownTimer: LiveData<Long>
        get() = _millisOnCountDownTimer

    fun startNewCountDownTimer(long: Long) {
        newCountDownTimerInstance(long)
        timer.start()
    }

    private var _startTimerAtLong = MutableLiveData<Long>()
    val startTimerAtLong: LiveData<Long>
        get() = _startTimerAtLong

    fun setStartTimerAtLong(long: Long) {
        _startTimerAtLong.value = long
    }

    fun stopCountDownTimer() {
        timer.cancel()
    }

    private fun newCountDownTimerInstance(value: Long) {
        timer = object : CountDownTimer(value, 10) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.i(millisUntilFinished.toString())

                onTickBoolean(true)
                setStartTimerBoolean(true)

                val f = DecimalFormat("00")
                val minutes = (millisUntilFinished / 60000) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val m = DecimalFormat("000")
                val millis = millisUntilFinished % 1000
                _onTickValueString.value =
                    "${f.format(minutes)} : ${f.format(seconds)} : ${m.format(millis)}"

                _millisOnCountDownTimer.value = millisUntilFinished

            }

            override fun onFinish() {
                Timber.i("Count Down FINISH !!!")
                onTickBoolean(false)
                _millisOnCountDownTimer.value = 0
            }
        }
    }

}
