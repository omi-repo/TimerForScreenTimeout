package kost.romi.timerforscreentimeout.timerdetail

import android.R
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

    private var millisInFuture: Long = 0
    private var countDownInterval: Long = 0

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

        viewDataBinding!!.numberPicker.maxValue = 60
        viewDataBinding!!.numberPicker.minValue = 1

        startPauseStopTimer()
    }

    private fun startPauseStopTimer() {
        viewDataBinding!!.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Timber.d("viewDataBinding!!.numberPicker.setOnValueChangedListener { picker, oldVal, newVal -> : ${picker.value}")
        }
        viewDataBinding!!.startPauseFab.setOnClickListener {
            viewDataBinding?.stopResetFab?.visibility = View.VISIBLE
            when (viewModel.timerState.value?.state) {
                TimerState.READY -> startTimer((viewDataBinding!!.numberPicker.value * 1000).toLong())
                TimerState.STARTED -> pauseTimer()
                TimerState.PAUSED -> pauseTimer()
                TimerState.RESUME -> startTimer(viewModel.timerState.value!!.currentTime)
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

    private lateinit var timer: CountDownTimer

    fun startTimer(value: Long) {
        Timber.d("fun startTimer(value: Long) : $value")

//        timer
        viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_pause)
        viewDataBinding?.countdownTextview!!.visibility = View.VISIBLE
        viewDataBinding?.countdownTextview!!.text = "00:000"  // test
        viewDataBinding!!.numberPicker.visibility = View.GONE

        viewModel.timerState.value!!.lastTime = 0
        viewModel.timerState.value!!.currentTime = value
        viewModel.timerState.value!!.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    private fun newCountDownTimerInstance(value: Long) {
        timer = object : CountDownTimer(value, 10) {
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
    }

    fun pauseTimer() {
        viewDataBinding?.startPauseFab?.setImageResource(R.drawable.ic_media_play)
        viewModel.timerState.value!!.state = TimerState.RESUME
        timer.cancel()
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