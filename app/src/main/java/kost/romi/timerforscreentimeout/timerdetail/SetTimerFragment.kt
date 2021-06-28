package kost.romi.timerforscreentimeout.timerdetail

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.data.TimerState
import kost.romi.timerforscreentimeout.databinding.FragmentSetTimerBinding
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*


/**
 * A fragment to pick CountDownTimer with NumberPicker.
 * CountDownTimer is started in this fragment as well.
 * Everytime after the CountDownTimer is done running, it will be saved to TimerDatabase,
 * to be used in TimerHistoryFragment.
 */
@AndroidEntryPoint
class SetTimerFragment : Fragment() {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private lateinit var binding: FragmentSetTimerBinding

    private val viewModel: SetTimerViewModel by viewModels()

    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetTimerBinding.inflate(inflater, container, false)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_history -> {
                    val action =
                        SetTimerFragmentDirections.actionSetTimerFragmentToTimerHistoryFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.stopResetFab.visibility = View.GONE
        binding.countdownTextview.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        viewModel.setupTimer()

        Timber.d("viewModel.timerState.value :  ${viewModel.timerState}")

        binding.minutesNumberPicker.maxValue = 60
        binding.minutesNumberPicker.minValue = 0
        binding.secondsNumberPicker.maxValue = 60
        binding.secondsNumberPicker.minValue = 0

        startPauseStopTimer()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Checks the orientation of the screen
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(requireContext(), "landscape", Toast.LENGTH_SHORT).show()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(requireContext(), "portrait", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startPauseStopTimer() {
        binding.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Timber.d("viewDataBinding!!.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal -> : ${picker.value}")
        }

        // For start and resume timer actions
        binding.startPauseFab.setOnClickListener {
            val limit = (60 * 60000).toLong()
            val numberPickValue =
                (binding.secondsNumberPicker.value * 1000).toLong() + (binding.minutesNumberPicker.value * 60000).toLong()
            if (numberPickValue > limit) {
                Toast.makeText(
                    requireContext(),
                    "Countdown can't be more than\n 60 minutes",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                when (viewModel.timerState.state) {
                    TimerState.READY -> startTimer(numberPickValue)
                    TimerState.STARTED -> pauseTimer()
                    TimerState.PAUSED -> pauseTimer()
                    TimerState.RESUME -> resumeTimer(viewModel.timerState.currentTime)
                    else ->
                        Toast.makeText(requireContext(), "ooops, try again.", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }

        // For stop timer actions
        binding.stopResetFab.setOnClickListener {
            stopTimer()
        }
    }


    private fun resumeTimer(value: Long) {
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.countdownTextview.visibility = View.VISIBLE
        if (viewModel.timerState.currentTime.toString() == "0") {
            binding.countdownTextview.text = getString(R.string.time_at_zero)  // test
        }
        binding.secondsNumberPicker.visibility = View.GONE

        viewModel.timerState.currentTime = value
        viewModel.timerState.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    private fun stopTimer() {
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.stopResetFab.visibility = View.GONE
        binding.countdownTextview.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.secondsNumberPicker.visibility = View.VISIBLE
        binding.minutesNumberPicker.visibility = View.VISIBLE
        binding.minutesNumberPickerTextView.visibility = View.VISIBLE
        binding.secondsNumberPickerTextView.visibility = View.VISIBLE

        viewModel.timerState.currentTime = millisOnPaused
        if (viewModel.timerState.state != TimerState.FINISH) {
            viewModel.timerState.state = TimerState.STOPPED
        }
        viewModel.saveTimerToDB()
        viewModel.setupTimer()

        timer.cancel()
    }

    private fun startTimer(value: Long) {
        Timber.d("fun startTimer(value: Long) : $value")
        Timber.d("${Date()}")

        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.stopResetFab.visibility = View.VISIBLE
        binding.countdownTextview.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        if (viewModel.timerState.currentTime.toString() == "0") {
            binding.countdownTextview.text = getString(R.string.time_at_zero)  // test
        }

        binding.secondsNumberPicker.visibility = View.GONE
        binding.minutesNumberPicker.visibility = View.GONE
        binding.minutesNumberPickerTextView.visibility = View.GONE
        binding.secondsNumberPickerTextView.visibility = View.GONE

        viewModel.timerState.dateTimerAt = 0
        viewModel.timerState.currentTime = value
        viewModel.timerState.startAt = value
        viewModel.timerState.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    private var millisOnPaused: Long = 0

    private fun newCountDownTimerInstance(value: Long) {
        timer = object : CountDownTimer(value, 10) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.i(millisUntilFinished.toString())
                val f = DecimalFormat("00")
                val minutes = (millisUntilFinished / 60000) % 60
                val seconds = (millisUntilFinished / 1000) % 60
                val m = DecimalFormat("000")
                val millis = millisUntilFinished % 1000
                val str = "${f.format(minutes)} : ${f.format(seconds)} : ${m.format(millis)}"
                binding.countdownTextview.text = str

                millisOnPaused = millisUntilFinished

                binding.progressBar.max = viewModel.timerState.startAt.toInt()
                binding.progressBar.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                Timber.i("Count Down FINISH !!!")
                binding.startPauseFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.countdownTextview.text = getString(R.string.time_at_zero)
                millisOnPaused = 0
                viewModel.timerState.state = TimerState.FINISH

                if (binding.screenLockSwitch.isChecked) {
                    lockScreenNow()
                } else {
                    Toast.makeText(requireContext(), "Countdown done.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun pauseTimer() {
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        viewModel.timerState.state = TimerState.RESUME
        viewModel.timerState.currentTime = millisOnPaused
        viewModel.timerState.pausedAt = millisOnPaused

        timer.cancel()
    }


    /**
     * A function to lock the screen.
     * For testing, sometimes it's not called.
     */
    fun lockScreenNow() {
        val policy =
            requireActivity().getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        policy!!.lockNow()
    }

}