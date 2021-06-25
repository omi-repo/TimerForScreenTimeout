package kost.romi.timerforscreentimeout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kost.romi.timerforscreentimeout.data.TimerEntity
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
        private val pausedAt_textview: TextView = itemView.findViewById(R.id.pausedAt_textview)
        private val startAt_textview: TextView = itemView.findViewById(R.id.startAt_textview)
        private val state_textview: TextView = itemView.findViewById(R.id.state_textview)
        private val id_textview: TextView = itemView.findViewById(R.id.id_textview)
        private val cardView: CardView = itemView.findViewById(R.id.cardview)

        fun bind(timerEntity: TimerEntity) {
            dateTimerAt_textview.text =
                Date(timerEntity.dateTimerAt).let {
//                    SimpleDateFormat("dd-MM-yyyy / HH:mm").format(it)
                    SimpleDateFormat("dd-MM-yyyy / HH:mm", Locale.US).format(it)
                }
                    .toString()
            timerTextViewString(timerEntity.currentTime, currentTime_textview)
            timerTextViewString(timerEntity.pausedAt, pausedAt_textview)
            timerTextViewString(timerEntity.startAt, startAt_textview)
            state_textview.text = timerEntity.state.toString()
            id_textview.text = timerEntity.id.toString()

            cardView.elevation = (16).toFloat()
        }

        private fun timerTextViewString(long: Long, textView: TextView) {
            val f = DecimalFormat("00")
            val minutes = (long / 60000) % 60
            val seconds = (long / 1000) % 60
            val millis = long % 1000
            val str = "${f.format(minutes)} : ${f.format(seconds)} : ${f.format(millis)}"
            textView.text = str
//                "${f.format(minutes)} : ${f.format(seconds)} : ${f.format(millis)}"
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