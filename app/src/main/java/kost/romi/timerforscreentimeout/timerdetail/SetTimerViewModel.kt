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

    /*
    /* Set minutes and Second var from NumberPicker.  */
    private var _minutesNumberPickerInt = MutableLiveData<Int>()
    val minutesNumberPickerInt: LiveData<Int>
        get() = _minutesNumberPickerInt

    fun setMinutesNumberPickerInt(i: Int) {
        _minutesNumberPickerInt.value = i
    }

    private var _secondsNumberPickerInt = MutableLiveData<Int>()
    val secondsNumberPickerInt: LiveData<Int>
        get() = _secondsNumberPickerInt

    fun setSecondsNumberPickerInt(i: Int) {
        _secondsNumberPickerInt.value = i
    }


    fun setMinutesAndSecondNumberPicker(i: Int, n: Int) {
        _minutesNumberPickerInt.value = i
        _secondsNumberPickerInt.value = n
    }

    private var _startAtCountDown = MutableLiveData<Long>()
    val startAtCountDown: LiveData<Long>
        get() = _startAtCountDown

    fun setStartAtCountDown(long: Long) {
        _startAtCountDown.value = long
    }

    private var _endAtCountDown = MutableLiveData<Long>()
    val endAtCountDown: LiveData<Long>
        get() = _endAtCountDown

    fun setEndAtCountDown(long: Long) {
        _endAtCountDown.value = long
    }

    private var _currentTimerState = MutableLiveData<TimerState>()
    val currentTimerState: LiveData<TimerState>
        get() = _currentTimerState

    fun setCurrentTimerStateToReady() {
        _currentTimerState.value = TimerState.READY
    }

    fun setCurrentTimerStateToStarted() {
        _currentTimerState.value = TimerState.STARTED
    }

    fun setCurrentTimerStateToPause() {
        _currentTimerState.value = TimerState.PAUSED
    }

    fun setCurrentTimerStateToResume() {
        _currentTimerState.value = TimerState.RESUME
    }

    fun setCurrentTimerStateToFinish() {
        _currentTimerState.value = TimerState.FINISH
    }


    fun startNewCountDownTimer() {
        val numberPickValue =
            (_secondsNumberPickerInt.value!! * 1000).toLong() + (_minutesNumberPickerInt.value!! * 60000).toLong()
        _timerState.value!!.currentTime = numberPickValue
        _timerState.value!!.pausedAt = numberPickValue
        _timerState.value!!.startAt = numberPickValue
        _currentTimerState.value = TimerState.STARTED
        newCountDownTimerInstance(numberPickValue)
    }

    private var _onTickValueInString = MutableLiveData<String>()
    val onTickValueInString: LiveData<String>
        get() = _onTickValueInString

    private var _onTickValueInLong = MutableLiveData<Long>()
    val onTickValueInLong: LiveData<Long>
        get() = _onTickValueInLong

    var timer: CountDownTimer? = null
    fun newCountDownTimerInstance(value: Long = timerState.value!!.currentTime) {
        timer = object : CountDownTimer(value, 10) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.d(millisUntilFinished.toString())
                /*val f = DecimalFormat("00")
                val minutes = (millisUntilFinished / 60000) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val m = DecimalFormat("000")
                val millis = millisUntilFinished % 1000
                val str = "${f.format(minutes)} : ${f.format(seconds)} : ${m.format(millis)}"
                Timber.d("$str")
                _onTickValueInString.value = str*/
                _timerState.value?.currentTime = value
                _onTickValueInLong.value = millisUntilFinished
            }

            override fun onFinish() {
                Timber.d("Count Down FINISH !!!")
                _timerState.value!!.currentTime = value
                _currentTimerState.value = TimerState.FINISH
            }
        }
    }

    fun startTimer() {
        timer?.start()
    }

    fun pausedTimer() {
        timer?.cancel()
    }

    fun stopTimer() {
        timer?.cancel()
    }
    */

}
