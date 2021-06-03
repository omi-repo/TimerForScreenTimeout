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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *
 */
class SetTimerFragment : Fragment() {

    private var mDevicePolicyManager: DevicePolicyManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_timer, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDevicePolicyManager =
            context.getSystemService(Activity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override fun onDetach() {
        mDevicePolicyManager = null
        super.onDetach()
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch { screenTimeout() }

    }

    suspend fun screenTimeout() {
        delay(5000L)
//        val manager =
//            requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val pm = requireActivity().getSystemService(Context.POWER_SERVICE) as PowerManager?
        if (pm!!.isScreenOn) {
            val policy =
                requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
            policy!!.lockNow()
        }
    }

}