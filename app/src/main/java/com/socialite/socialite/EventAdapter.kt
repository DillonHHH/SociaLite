package com.socialite.socialite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Event

class EventAdapter(
    private val events: List<Event>,
    private val onItemClick: (eventTitle: String) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.eventTitle)
        val location: TextView = itemView.findViewById(R.id.eventLocation)
        val date: TextView = itemView.findViewById(R.id.eventDate)
        val description: TextView = itemView.findViewById(R.id.eventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        //val displayDate = event.date?.startDate?.let { parseDate(it) }

        holder.itemView.setOnClickListener{
            onItemClick(event.title)
        }

        holder.title.text = event.title
        holder.location.text = event.location
        holder.date.text = event.start
        holder.description.text = event.description
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
