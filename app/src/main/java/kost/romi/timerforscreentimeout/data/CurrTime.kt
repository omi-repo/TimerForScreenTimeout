package kost.romi.timerforscreentimeout.data

data class CurrTime(
    var time: Int = 0,
    var minutes: Int = 0,
    var seconds: Int = 0,
    var miliseconds: Int = 0,
    var state: TimerState = TimerState.READY
)

enum class TimerState {
    READY,
    STARTED,
    PAUSED,
    RESUME,
    STOPED
}