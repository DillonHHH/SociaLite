package com.socialite.socialite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import com.socialite.socialite.repository.Event
import com.socialite.socialite.EventAdapter
import com.socialite.socialite.EventDetailsActivity
import com.socialite.socialite.R
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    fun launchEventDetailsActivity(eventTitle: String){
        val secondActivityIntent = Intent(requireContext(), EventDetailsActivity::class.java)
        secondActivityIntent.putExtra("EXTRA_TITLE",eventTitle)
        this.startActivity(secondActivityIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get event data (you may fetch this from a database or API)
        val events = getEventData()

        // Sort events by date (soonest event first)
        //val sortedEvents = events.sortedBy { it.date?.let { it1 -> parseDate(it1.startDate) } }
        val sortedEvents = events.sortedBy { it.start }

        // Set up the adapter with sorted events
        eventAdapter = EventAdapter(sortedEvents) { eventTitle ->
            launchEventDetailsActivity(eventTitle)
        }
        recyclerView.adapter = eventAdapter
        return view
    }

    private fun getEventData(): List<Event> {
        // Replace with actual data source
        // Dummy Data
        return listOf(
            Event(null, "Concert", "Free concert in the park.", "Fayetteville Library, Arkansas", "2024-11-22 Evening", ""),
            Event(null, "Workshop", "Art workshop for all ages.", "Community Center", "2024-11-20 Morning", ""),
            Event(null, "Charity Run", "5k charity run event.", "Downtown, Dickson Street", "2024-11-25 Morning", ""),
            Event(null, "Food Festival", "Taste foods from around the world!", "Market Square", "", "")
        )
    }
}