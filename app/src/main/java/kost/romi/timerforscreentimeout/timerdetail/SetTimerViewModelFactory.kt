package kost.romi.timerforscreentimeout.timerdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kost.romi.timerforscreentimeout.data.source.local.TimerDAO

/**
 * This is pretty much boiler plate code for ViewModel Factory.
 *
 * Provides the TimerDAO and context to the ViewModel.
 */
class SetTimerViewModelFactory(
    private val dataSource: TimerDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetTimerViewModel::class.java)) {
            return SetTimerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}