package kost.romi.timerforscreentimeout.timerhistory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO

//class TimerHistoryModelFactory(
//    private val dataSource: TimerDAO,
//    private val application: Application
//) : ViewModelProvider.Factory {
//    @Suppress("unchecked_cast")
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TimerHistoryViewModel::class.java)) {
//            return TimerHistoryViewModel(dataSource, application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}