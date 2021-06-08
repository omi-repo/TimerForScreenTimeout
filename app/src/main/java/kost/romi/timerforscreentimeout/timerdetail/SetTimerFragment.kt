package kost.romi.timerforscreentimeout.timerdetail

import android.R
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kost.romi.timerforscreentimeout.data.TimerState
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
        viewDataBinding?.stopResetFab?.visibility = View.GONE

        viewModel.setupTimer()

        Timber.d("viewModel.timerState.value :  ${viewModel.timerState.value}")

//        viewDataBinding.textView.text = viewModel.timerState
        startPauseStopTimer()
    }

    private fun startPauseStopTimer() {
        viewDataBinding!!.startPauseFab.setOnClickListener {
            viewDataBinding?.stopResetFab?.visibility = View.VISIBLE
            when (viewModel.timerState.value?.state) {
                TimerState.READY -> startTimer()
                TimerState.STARTED -> pauseTimer()
                TimerState.PAUSED -> pauseTimer()
                TimerState.RESUME -> startTimer()
            }
        }
        viewDataBinding!!.stopResetFab.setOnClickListener {
            viewModel.setupTimer()
            viewDataBinding!!.stopResetFab.visibility = View.GONE
            viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_play)
            viewDataBinding?.countdownTextview!!.visibility = View.GONE
            viewDataBinding!!.numberPicker.visibility = View.VISIBLE
        }
    }

    fun startTimer() {
//        timer
        viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_pause)
        viewDataBinding?.countdownTextview!!.visibility = View.VISIBLE
        viewDataBinding?.countdownTextview!!.text = "00:00"  // test
        viewDataBinding!!.numberPicker.visibility = View.GONE
        viewModel.timerState.value!!.state = TimerState.STARTED
        timer.start()
    }

    fun pauseTimer() {
        viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_play)
        viewModel.timerState.value!!.state = TimerState.RESUME
        timer.cancel()
    }

    val timer = object : CountDownTimer(20000, 10) {
        override fun onTick(millisUntilFinished: Long) {
            Timber.i(millisUntilFinished.toString())
            var timeStr = millisUntilFinished.toString()
            when (timeStr.length) {
                5 -> viewDataBinding?.countdownTextview?.text =
                    "${timeStr.substring(0, 2)} : ${timeStr.substring(2)}"
                4 -> viewDataBinding?.countdownTextview?.text =
                    "0${timeStr.get(0)} : ${timeStr.substring(1)}"
                3 -> viewDataBinding?.countdownTextview?.text = "00 : $timeStr"
                2 -> viewDataBinding?.countdownTextview?.text = "00 : 0${timeStr}"
                1 -> viewDataBinding?.countdownTextview?.text = "00 : 00${timeStr}"
            }
        }

        override fun onFinish() {
            Timber.i("Count Down FINISH !!!")
//            lockScreenNow()
            viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_play)
            viewDataBinding?.countdownTextview?.text = "00 : 000"
        }
    }

    fun lockScreenNow() {
        var policy =
            requireActivity()!!.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        policy!!.lockNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Consider not storing the binding instance in a field, if not needed.
        viewDataBinding = null
    }

}