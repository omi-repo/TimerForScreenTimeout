package kost.romi.timerforscreentimeout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kost.romi.timerforscreentimeout.data.TimerEntity
import kost.romi.timerforscreentimeout.data.TimerState
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class TimerHistoryAdapter :
    ListAdapter<TimerEntity, TimerHistoryAdapter.TimerHistoryViewHolder>(TimerHistoryDiffCallback) {

    /* ViewHolder for Flower, takes in the inflated view and the onClick behavior. */
    class TimerHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTimerAt_textview: TextView =
            itemView.findViewById(R.id.dateTimerAt_textview)
        private val currentTime_textview: TextView =
            itemView.findViewById(R.id.currentTime_textview)
        private val startAt_textview: TextView = itemView.findViewById(R.id.startAt_textview)
        private val state_textview: TextView = itemView.findViewById(R.id.state_textview)
        private val id_textview: TextView = itemView.findViewById(R.id.id_textview)
        private val cardView: CardView = itemView.findViewById(R.id.cardview)
        private val screenLock_switch: SwitchCompat = itemView.findViewById(R.id.screen_lock_switch)

        fun bind(timerEntity: TimerEntity) {
            dateTimerAt_textview.text =
                Date(timerEntity.dateTimerAt).let {
//                    SimpleDateFormat("dd-MM-yyyy / HH:mm").format(it)
                    SimpleDateFormat("dd-MM-yyyy / HH:mm", Locale.US).format(it)
                }
                    .toString()
            timerTextViewString(timerEntity.currentTime, currentTime_textview)
            timerTextViewString(timerEntity.startAt, startAt_textview)
            if (timerEntity.state == TimerState.FINISH) {
                state_textview.text = "Finish at: "
            }
            if (timerEntity.state == TimerState.STOPPED) {
                state_textview.text = "Stopped at: "
            }
//            id_textview.text = timerEntity.id.toString()
            id_textview.text = ""

            cardView.elevation = (16).toFloat()

            screenLock_switch.isChecked = timerEntity.screenLockSwitch
        }

        private fun timerTextViewString(long: Long, textView: TextView) {
            val f = DecimalFormat("00")
            val minutes = (long / 60000) % 60
            val seconds = (long / 1000) % 60
            val m = DecimalFormat("000")
            val millis = long % 1000
            val str =
                " ${if (long == 3600000.toLong()) 60 else f.format(minutes)} : " +
                        "${f.format(seconds)} : " +
                        "${m.format(millis)}"
            textView.text = str
            Timber.d("minutes: $minutes")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerHistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timer_history_adapter, parent, false)
        return TimerHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerHistoryViewHolder, position: Int) {
        val timerEntity = getItem(position)
        holder.bind(timerEntity)
        Timber.d("${timerEntity.id} ${timerEntity.startAt}")
    }

}

object TimerHistoryDiffCallback : DiffUtil.ItemCallback<TimerEntity>() {
    override fun areItemsTheSame(oldItem: TimerEntity, newItem: TimerEntity): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TimerEntity, newItem: TimerEntity): Boolean {
        return oldItem.id == newItem.id
    }
}