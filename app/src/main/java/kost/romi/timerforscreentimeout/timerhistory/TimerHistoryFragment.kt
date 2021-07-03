package kost.romi.timerforscreentimeout.timerhistory

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.Stagger
import kost.romi.timerforscreentimeout.TimerHistoryAdapter
import kost.romi.timerforscreentimeout.databinding.FragmentTimerHistoryBinding
import kotlinx.android.synthetic.main.fragment_timer_history.view.*
import timber.log.Timber

/**
 * A fragment to show all the timer the User has run.
 * Will contain date, start, and finished data of the CountDownTimer.
 */
@AndroidEntryPoint
class TimerHistoryFragment : Fragment() {

    private lateinit var binding: FragmentTimerHistoryBinding

    private val viewModel: TimerHistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerHistoryBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // This is the transition for the stagger effect.
        val stagger = Stagger()

        val list: RecyclerView = binding.recyclerView

        // RecyclerView
        val adapter = TimerHistoryAdapter()
        val recyclerView: RecyclerView = binding.root.recycler_view
        recyclerView.adapter = adapter

        list.adapter = adapter

        // We animate item additions on our side, so disable it in RecyclerView.
        list.itemAnimator = object : DefaultItemAnimator() {
            override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
                dispatchAddFinished(holder)
                dispatchAddStarting(holder)
                return false
            }
        }

        viewModel.getAllHistory.observe(owner = viewLifecycleOwner) {
            Timber.d("$it")

            it.let {
                // Delay the stagger effect until the list is updated.
                TransitionManager.beginDelayedTransition(list, stagger)
                adapter.submitList(it)
            }
        }

        binding.toolbar.title = "Countdown History"
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.clear_all_item -> {
                    Toast.makeText(
                        requireContext(),
                        "All item cleared.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    viewModel.clearCountdownHistory()
                    true
                }
                else -> false
            }
        }

    }

}