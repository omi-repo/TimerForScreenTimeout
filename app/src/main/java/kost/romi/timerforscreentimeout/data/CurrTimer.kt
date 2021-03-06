package kost.romi.timerforscreentimeout.data

/**
 * For temp variables in SetTimerFragment.
 */
data class CurrTime(
    var dateTimerAt: Long = System.currentTimeMillis(),
    var currentTime: Long = 0,
    var startAt: Long = 0,
    var state: TimerState = TimerState.READY
)

enum class TimerState {
    READY,
    STARTED,
    PAUSED,
    RESUME,
    STOPPED,
    FINISH
}