package kost.romi.timerforscreentimeout

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kost.romi.timerforscreentimeout.databinding.FragmentSetTimerBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 *
 */
class SetTimerFragment : Fragment(R.layout.fragment_set_timer) {

    var tempFlag =
        false  // flag so lockNow wont run everytime the app started back up through the lifecycle.

    private var setTimerFragmentBinding: FragmentSetTimerBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSetTimerBinding.bind(requireView())
        setTimerFragmentBinding = binding

        setTimerFragmentBinding?.numberPicker?.minValue = 1
        setTimerFragmentBinding?.numberPicker?.maxValue = 60

        setTimerFragmentBinding?.numberPicker?.setOnValueChangedListener { picker, oldVal, newVal ->
            Toast.makeText(
                requireContext(),
                "${picker.value}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onStart() {
        super.onStart()

//        lifecycleScope.launch { screenTimeout() }

//        if (tempFlag != true) {
//            var policy =
//                requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
//            policy!!.lockNow()
//            tempFlag = true
//        }
    }

    suspend fun screenTimeout() {
        delay(5000L)
        val pm = requireActivity().getSystemService(Context.POWER_SERVICE) as PowerManager?
        if (pm!!.isScreenOn) {
            val policy =
                requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
            policy!!.lockNow()
        }
    }

}