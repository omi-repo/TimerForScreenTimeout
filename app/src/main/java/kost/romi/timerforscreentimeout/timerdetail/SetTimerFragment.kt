package kost.romi.timerforscreentimeout.timerdetail

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.data.TimerState
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import kost.romi.timerforscreentimeout.databinding.FragmentSetTimerBinding
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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
    private lateinit var  binding: FragmentSetTimerBinding

    private val viewModel: SetTimerViewModel by viewModels()

    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetTimerBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        // Initialize DB.
//        val application = requireNotNull(this.activity).application
//        val dataSource = TimerDatabase.getInstance(application).timerDao
//        val viewModelFactory = SetTimerViewModelFactory(dataSource, application)
//        val setTimerViewModel = ViewModelProvider(
//            this, viewModelFactory
//        ).get(SetTimerViewModel::class.java)
//        binding!!.setLifecycleOwner(this)
//        binding!!.setTimerViewModel = setTimerViewModel
//
//        viewModel = setTimerViewModel

        return binding!!.root
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

    var tempFlag =
        false  // flag so lockNow wont run everytime the app started back up through the lifecycle.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.lifecycleOwner = this.viewLifecycleOwner
        binding?.stopResetFab?.visibility = View.GONE

        viewModel.setupTimer()

        Timber.d("viewModel.timerState.value :  ${viewModel.timerState}")

        binding!!.numberPicker.maxValue = 60
        binding!!.numberPicker.minValue = 1

        startPauseStopTimer()
    }

    private fun startPauseStopTimer() {
        binding!!.numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Timber.d("viewDataBinding!!.numberPicker.setOnValueChangedListener { picker, oldVal, newVal -> : ${picker.value}")
        }
        binding!!.startPauseFab.setOnClickListener {
            binding?.stopResetFab?.visibility = View.VISIBLE
            when (viewModel.timerState!!.state) {
                TimerState.READY -> startTimer((binding!!.numberPicker.value * 1000).toLong())
                TimerState.STARTED -> pauseTimer()
                TimerState.PAUSED -> pauseTimer()
                TimerState.RESUME -> resumeTimer(viewModel.timerState!!.currentTime)
            }
        }
        binding!!.stopResetFab.setOnClickListener {
            stopTimer()
        }
    }

    private fun resumeTimer(value: Long) {
        binding?.startPauseFab?.setImageResource(R.drawable.ic_baseline_pause_24)
        binding?.countdownTextview!!.visibility = View.VISIBLE
        if (viewModel.timerState!!.currentTime.toString().equals("0")) {
            binding?.countdownTextview!!.text = "00:000"  // test
        }
        binding!!.numberPicker.visibility = View.GONE

        viewModel.timerState!!.currentTime = value
        viewModel.timerState!!.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    fun stopTimer() {
        binding!!.stopResetFab.visibility = View.GONE
        binding?.startPauseFab?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding?.countdownTextview!!.visibility = View.GONE
        binding!!.numberPicker.visibility = View.VISIBLE

        viewModel.timerState!!.currentTime = millisOnPaused
        if (viewModel.timerState!!.state != TimerState.FINISH) {
            viewModel.timerState!!.state = TimerState.STOPPED
        }
        viewModel.saveTimerToDB()
        viewModel.setupTimer()

        timer.cancel()
    }

    fun startTimer(value: Long) {
        Timber.d("fun startTimer(value: Long) : $value")
        Timber.d("${Date()}")

        binding?.startPauseFab?.setImageResource(R.drawable.ic_baseline_pause_24)
        binding?.countdownTextview!!.visibility = View.VISIBLE
        if (viewModel.timerState!!.currentTime.toString().equals("0")) {
            binding?.countdownTextview!!.text = "00:000"  // test
        }
        binding!!.numberPicker.visibility = View.GONE

        viewModel.timerState!!.dateTimerAt = 0
        viewModel.timerState!!.currentTime = value
        viewModel.timerState.startAt = value
        viewModel.timerState!!.state = TimerState.STARTED

        newCountDownTimerInstance(value)

        timer.start()
    }

    private var millisOnPaused: Long = 0

    private fun newCountDownTimerInstance(value: Long) {
        timer = object : CountDownTimer(value, 10) {
            override fun onTick(millisUntilFinished: Long) {
                Timber.i(millisUntilFinished.toString())
                var timeStr = millisUntilFinished.toString()
                when (timeStr.length) {
                    5 -> binding?.countdownTextview?.text =
                        "${timeStr.substring(0, 2)} : ${timeStr.substring(2)}"
                    4 -> binding?.countdownTextview?.text =
                        "0${timeStr.get(0)} : ${timeStr.substring(1)}"
                    3 -> binding?.countdownTextview?.text = "00 : $timeStr"
                    2 -> binding?.countdownTextview?.text = "00 : 0${timeStr}"
                    1 -> binding?.countdownTextview?.text = "00 : 00${timeStr}"
                }
                millisOnPaused = millisUntilFinished
            }

            override fun onFinish() {
                Timber.i("Count Down FINISH !!!")
//            lockScreenNow()
                policy = null
                binding?.startPauseFab?.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                binding?.countdownTextview?.text = "00 : 000"
                viewModel.timerState!!.state = TimerState.FINISH
            }
        }
    }

    fun pauseTimer() {
        binding?.startPauseFab?.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        viewModel.timerState!!.state = TimerState.RESUME
        viewModel.timerState!!.currentTime = millisOnPaused
        viewModel.timerState!!.pausedAt = millisOnPaused

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