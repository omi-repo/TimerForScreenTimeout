package kost.romi.timerforscreentimeout.data

data class CurrTime(
    var lastTime: Int = 0,
    var currentTime: Int = 0,
    var state: TimerState = TimerState.READY
)

enum class TimerState {
    READY,
    STARTED,
    PAUSED,
    RESUME,
    STOPED
}