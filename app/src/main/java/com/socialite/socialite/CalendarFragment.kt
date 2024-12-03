package com.socialite.socialite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.socialite.socialite.repository.Event
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CalendarFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateText: TextView
    private lateinit var eventTitleRecyclerView: RecyclerView
    private lateinit var eventTitleAdapter: EventTitleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        selectedDateText = view.findViewById(R.id.selectedDateText)
        eventTitleRecyclerView = view.findViewById(R.id.eventTitleRecyclerView)

        // Setup RecyclerView
        eventTitleRecyclerView.layoutManager = LinearLayoutManager(context)
        eventTitleAdapter = EventTitleAdapter()
        eventTitleRecyclerView.adapter = eventTitleAdapter

        // Define event dates
        val calendarDays = mutableListOf<CalendarDay>()

        // Add today's event
        val today = Calendar.getInstance()
        val todayEvent = CalendarDay(today).apply {
            // Set background drawable for this specific day
            //backgroundDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_party_icon) }

        }
        calendarDays.add(todayEvent)

        // Add an event 5 days from today
        val futureDay = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 5) }
        val futureEvent = CalendarDay(futureDay).apply {
            //backgroundDrawable = context?.let { AppCompatResources.getDrawable(it, R.drawable.ic_party_icon) }

        }
        calendarDays.add(futureEvent)

        // Set the calendar days
        calendarView.setCalendarDays(calendarDays)

        // Setup date selection listener
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                updateEventsForDate(calendarDay.calendar)
            }
        })

        // Show events for current date initially
        updateEventsForDate(Calendar.getInstance())
        return view
    }

    private fun updateEventsForDate(calendar: Calendar) {
        // Format and display selected date
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val eventDate = dateFormat.format(calendar.time)
        selectedDateText.text = "Events on " + eventDate

        // Get events for selected date and update adapter
        val eventsForDate = getEventsForDate(eventDate)
        eventTitleAdapter.submitList(eventsForDate)
    }

    private fun getEventsForDate(date: String): List<Event> {
        // Replace this with actual event data source
        return listOf(
            Event(0, "Concert", "Free concert in the park.", "Fayetteville Library, Arkansas", date, ""),
            Event(1, "Workshop", "Art workshop for all ages.", "Community Center", date, ""),
            Event(2, "Charity Run", "5k charity run event.", "Downtown, Dickson Street", date, ""),
            Event(3, "Food Festival", "Taste foods from around the world!", "Market Square", date, "")
        )
    }
}