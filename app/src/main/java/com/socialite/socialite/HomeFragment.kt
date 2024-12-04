package com.socialite.socialite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.EventDatabase
import kotlinx.coroutines.runBlocking

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val eventDatabase: EventDatabase = EventDatabase()
    private val commentDatabase: CommentDatabase = CommentDatabase()

    fun launchEventDetailsActivity(eventId: Int) {
        val secondActivityIntent = Intent(requireContext(), EventDetailsActivity::class.java)
        secondActivityIntent.putExtra("EXTRA_ID", eventId)
        this.startActivity(secondActivityIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get event data (you may fetch this from a database or API)
        val events = runBlocking {
            eventDatabase.getAllEvents()
        }

        // Sort events by date (soonest event first)
        //val sortedEvents = events.sortedBy { it.date?.let { it1 -> parseDate(it1.startDate) } }
        val sortedEvents = events.sortedBy { it.start }

        // Set up the adapter with sorted events
        eventAdapter = EventAdapter(sortedEvents) { eventId ->
            launchEventDetailsActivity(eventId)
        }
        recyclerView.adapter = eventAdapter
        return view
    }
}