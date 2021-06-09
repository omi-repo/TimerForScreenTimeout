package kost.romi.timerforscreentimeout.data

data class CurrTime(
    var lastTime: Long = 0,
    var currentTime: Long = 20000,
    var state: TimerState = TimerState.READY
)

enum class TimerState {
    READY,
    STARTED,
    PAUSED,
    RESUME,
    STOPED
}