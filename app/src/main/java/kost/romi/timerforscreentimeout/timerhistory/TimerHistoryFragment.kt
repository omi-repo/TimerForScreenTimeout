package kost.romi.timerforscreentimeout.timerhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.TimerHistoryAdapter
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.databinding.FragmentTimerHistoryBinding
import kotlinx.android.synthetic.main.fragment_timer_history.view.*

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
    ): View? {
        binding = FragmentTimerHistoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        // RecyclerView
        val adapter = TimerHistoryAdapter()
        val recyclerView: RecyclerView = binding!!.root.recycler_view
        recyclerView.adapter = adapter

        viewModel.timerEntityLiveData.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it as MutableList<TimerEntity>)
            }
        })

        return binding!!.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
//            val action = TimerHistoryFragmentDirections.actionTimerHistoryFragmentToSetTimerFragment()
//            findNavController().navigateUp(action)
        }
        return false
    }

}