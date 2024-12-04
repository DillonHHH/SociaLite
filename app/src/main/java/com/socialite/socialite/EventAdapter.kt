package com.socialite.socialite

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Event

class EventAdapter(
    private val events: List<Event>,
    private val onItemClick: (eventId: Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.eventTitle)
        val location: TextView = itemView.findViewById(R.id.eventLocation)
        val date: TextView = itemView.findViewById(R.id.eventDate)
        val description: TextView = itemView.findViewById(R.id.eventDescription)
        val image: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        //val displayDate = event.date?.startDate?.let { parseDate(it) }

        holder.itemView.setOnClickListener{
            onItemClick(event.id!!)
        }

        holder.title.text = event.title
        holder.location.text = event.location
        holder.date.text = event.start
        holder.description.text = event.description

        val image: Bitmap? = event.getImage()
        if (image != null) {
            holder.image.setImageBitmap(image)
        }else{
            holder.image.visibility = GONE
        }

        holder.image.setImageBitmap(event.getImage())
    }

    override fun getItemCount(): Int {
        return events.size
    }
}
