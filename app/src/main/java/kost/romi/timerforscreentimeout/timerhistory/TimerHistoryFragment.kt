package kost.romi.timerforscreentimeout.timerhistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kost.romi.timerforscreentimeout.R
import kost.romi.timerforscreentimeout.TimerHistoryAdapter
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.source.local.TimerDatabase
import kost.romi.timerforscreentimeout.databinding.FragmentTimerHistoryBinding
import kost.romi.timerforscreentimeout.timerdetail.SetTimerViewModel
import kost.romi.timerforscreentimeout.timerdetail.SetTimerViewModelFactory
import kotlinx.android.synthetic.main.fragment_timer_history.view.*

/**
 * A fragment to show all the timer the User has run.
 * Will contain date, start, and finished data of the CountDownTimer.
 */
class TimerHistoryFragment : Fragment() {

    private var binding: FragmentTimerHistoryBinding? = null

    lateinit var viewModel: TimerHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerHistoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        // Initialize DB.
        val application = requireNotNull(this.activity).application
        val dataSource = TimerDatabase.getInstance(application).timerDao
        val viewModelFactory = TimerHistoryModelFactory(dataSource, application)
        val timerHistoryViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(TimerHistoryViewModel::class.java)
        binding!!.setLifecycleOwner(this)
        binding!!.timerHistoryViewModel = timerHistoryViewModel

        viewModel = timerHistoryViewModel

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