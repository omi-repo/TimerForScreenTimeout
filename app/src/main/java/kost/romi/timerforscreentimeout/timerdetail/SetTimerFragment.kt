package kost.romi.timerforscreentimeout.timerdetail

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.REQUEST_WRITE_SETTINGS
import kost.romi.timerforscreentimeout.data.TimerState
import kost.romi.timerforscreentimeout.databinding.FragmentSetTimerBinding
import kost.romi.timerforscreentimeout.hasPermission
import timber.log.Timber
import java.text.DecimalFormat
import java.util.*


/**
 * A fragment to pick CountDownTimer with NumberPicker.
 * CountDownTimer is started in this fragment as well.
 * Everytime after the CountDownTimer is done running, it will be saved to TimerDatabase,
 * to be used in TimerHistoryFragment.
 * TODO; add minutes to setting up timer.
 * TODO; update UI view
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
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                Toast.makeText(requireContext(), "history fragment button hit", Toast.LENGTH_SHORT)
                    .show()
                var action =
                    SetTimerFragmentDirections.actionSetTimerFragmentToTimerHistoryFragment()
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.stopResetFab.visibility = View.GONE

        viewModel.setupTimer()

        Timber.d("viewModel.timerState.value :  ${viewModel.timerState}")

        binding.minutesNumberPicker.maxValue = 60
        binding.minutesNumberPicker.minValue = 0
        binding.secondsNumberPicker.maxValue = 60
        binding.secondsNumberPicker.minValue = 0

        startPauseStopTimer()
    }

    private fun startPauseStopTimer() {
        binding.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Timber.d("viewDataBinding!!.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal -> : ${picker.value}")
        }

        binding.startPauseFab.setOnClickListener {
            binding.stopResetFab?.visibility = View.VISIBLE
            when (viewModel.timerState!!.state) {
                TimerState.READY -> startTimer((binding.secondsNumberPicker.value * 1000).toLong() + (binding.minutesNumberPicker.value * 60000).toLong())
                TimerState.STARTED -> pauseTimer()
                TimerState.PAUSED -> pauseTimer()
                TimerState.RESUME -> resumeTimer(viewModel.timerState!!.currentTime)
            }
        }

        binding.stopResetFab.setOnClickListener {
            stopTimer()
        }
    }


    private fun resumeTimer(value: Long) {
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.countdownTextview.visibility = View.VISIBLE
        if (viewModel.timerState.currentTime.toString().equals("0")) {
            binding.countdownTextview.text = "00:000"  // test
        }
        binding.secondsNumberPicker.visibility = View.GONE

        viewModel.timerState.currentTime = value
        viewModel.timerState.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    fun stopTimer() {
        binding.stopResetFab.visibility = View.GONE
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.countdownTextview.visibility = View.GONE
        binding.secondsNumberPicker.visibility = View.VISIBLE
        binding.minutesNumberPicker.visibility = View.VISIBLE

        viewModel.timerState.currentTime = millisOnPaused
        if (viewModel.timerState.state != TimerState.FINISH) {
            viewModel.timerState.state = TimerState.STOPPED
        }
        viewModel.saveTimerToDB()
        viewModel.setupTimer()

        timer.cancel()
    }

    fun startTimer(value: Long) {
        Timber.d("fun startTimer(value: Long) : $value")
        Timber.d("${Date()}")

        binding.startPauseFab?.setImageResource(R.drawable.ic_baseline_pause_24)
        binding.countdownTextview.visibility = View.VISIBLE
        if (viewModel.timerState.currentTime.toString().equals("0")) {
            binding?.countdownTextview.text = "00:000"  // test
        }
        binding.secondsNumberPicker.visibility = View.GONE
        binding.minutesNumberPicker.visibility = View.GONE

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
                var f = DecimalFormat("00")
                var minutes = (millisUntilFinished / 60000) % 60
                var seconds = (millisUntilFinished / 1000) % 60
                var m = DecimalFormat("000")
                var milis = millisUntilFinished % 1000
                binding.countdownTextview.text =
                    "${f.format(minutes)} : ${f.format(seconds)} : ${m.format(milis)}"

                millisOnPaused = millisUntilFinished
            }

            override fun onFinish() {
                Timber.i("Count Down FINISH !!!")
//            lockScreenNow()
                policy = null
                binding.startPauseFab?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding.countdownTextview?.text = "00 : 00 : 000"
                viewModel.timerState.state = TimerState.FINISH
            }
        }
    }

    fun pauseTimer() {
        binding.startPauseFab.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        viewModel.timerState.state = TimerState.RESUME
        viewModel.timerState.currentTime = millisOnPaused
        viewModel.timerState.pausedAt = millisOnPaused

        timer.cancel()
    }

    var policy = null

    /**
     * A function to lock the screen.
     * For testing, sometimes it's not called.
     */
    fun lockScreenNow() {
        var policy =
            requireActivity()!!.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?
        policy!!.lockNow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Consider not storing the binding instance in a field, if not needed.
    }

}