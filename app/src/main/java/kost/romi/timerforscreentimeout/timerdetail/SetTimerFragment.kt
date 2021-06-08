package kost.romi.timerforscreentimeout.timerdetail

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import kost.romi.timerforscreentimeout.databinding.FragmentSetTimerBinding
import timber.log.Timber


/**
 *
 */
class SetTimerFragment : Fragment() {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var viewDataBinding: FragmentSetTimerBinding? = null

    private val viewModel: SetTimerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding = FragmentSetTimerBinding.inflate(inflater, container, false)
        return viewDataBinding!!.root
    }

    var tempFlag =
        false  // flag so lockNow wont run everytime the app started back up through the lifecycle.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = this.viewLifecycleOwner
//        viewDataBinding.textView.text = viewModel.timerState
        startPauseStopTimer()
    }

    private fun startPauseStopTimer() {
        viewDataBinding!!.startPauseStopFab.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Consider not storing the binding instance in a field, if not needed.
        viewDataBinding = null
    }

    val timer = object : CountDownTimer(20000, 10) {
        override fun onTick(millisUntilFinished: Long) {
            Timber.i(millisUntilFinished.toString())
            viewDataBinding?.textView?.text = millisUntilFinished.toString()
        }

        override fun onFinish() {
            Timber.i("Count Down FINISH !!!")
            lockScreenNow()
        }
    }

    fun lockScreenNow() {
        var policy =
            requireActivity()!!.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        policy!!.lockNow()
    }

}