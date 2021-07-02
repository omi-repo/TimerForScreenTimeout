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

    private lateinit var binding: FragmentSetTimerBinding

    private val viewModel: SetTimerViewModel by viewModels()

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

        Timber.d("viewModel.timerState.value :  ${viewModel.timerState}")

        binding.minutesNumberPicker.maxValue = 60
        binding.minutesNumberPicker.minValue = 0
        binding.secondsNumberPicker.maxValue = 59
        binding.secondsNumberPicker.minValue = 0

        subscribeUi()

        binding.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            Timber.d("viewDataBinding!!.secondsNumberPicker.setOnValueChangedListener { picker, oldVal, newVal -> : ${picker.value}")
        }

        binding.startFab!!.setOnClickListener {


            val limit = (60 * 60000).toLong()
            val numberPickValue =
                (binding.secondsNumberPicker.value * 1000).toLong() + (binding.minutesNumberPicker.value * 60000).toLong()
            if (numberPickValue > limit || numberPickValue == 0L) {
                Toast.makeText(
                    requireContext(),
                    "Countdown can't be more than 60 minutes\nor 0",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.startNewCountDownTimer(numberPickValue)
                viewModel.setStartTimerBoolean(true)
                binding.progressBar.max = numberPickValue.toInt()
                viewModel.setStartTimerAtLong(numberPickValue)
            }

        }

        binding.stopResetFab.setOnClickListener {

            viewModel.setStartTimerBoolean(false)
            viewModel.stopCountDownTimer()

        }

    }

    private fun subscribeUi() {
        viewModel.startTimerBoolean.observe(viewLifecycleOwner, {
            when (it) {
                true -> {

                    binding.testSetTimerTextview?.visibility = View.VISIBLE

                    binding.startFab!!.visibility = View.GONE
                    binding.stopResetFab.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.countdownTextview.visibility = View.VISIBLE

                    binding.countdownTextview.text = viewModel.onTickValueString.value
                    binding.progressBar.progress = viewModel.millisOnCountDownTimer.value!!.toInt()

                    binding.secondsNumberPicker.visibility = View.GONE
                    binding.minutesNumberPicker.visibility = View.GONE
                    binding.minutesNumberPickerTextView.visibility = View.GONE
                    binding.secondsNumberPickerTextView.visibility = View.GONE

                }
                false -> {

                    binding.testSetTimerTextview?.visibility = View.GONE

                    binding.startFab!!.visibility = View.VISIBLE
                    binding.stopResetFab.visibility = View.GONE
                    binding.countdownTextview.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE

                    binding.secondsNumberPicker.visibility = View.VISIBLE
                    binding.minutesNumberPicker.visibility = View.VISIBLE
                    binding.minutesNumberPickerTextView.visibility = View.VISIBLE
                    binding.secondsNumberPickerTextView.visibility = View.VISIBLE

                }
                else -> null
            }
        })

        viewModel.onTickBoolean.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                true -> {
//                    binding.stopResetFab.visibility = View.VISIBLE
//                    binding.startPauseFab.visibility = View.GONE
                }
                false -> {
                    binding.countdownTextview.text = "00 : 00 : 000"
                    binding.progressBar.progress = 0
                    viewModel.onTickBoolean(true)
                    if (binding.screenLockSwitch.isChecked) {
                        lockScreenNow()
                    }
                }
                else -> null
            }
        })

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