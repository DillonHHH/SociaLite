package com.socialite.socialite

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.Event
import com.socialite.socialite.repository.EventDatabase
import com.socialite.socialite.utils.encodeBitmapToString
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class AddPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)

        val eventTitleEditText = view?.findViewById<EditText>(R.id.eventTitle)
        val eventLocationEditText = view?.findViewById<EditText>(R.id.eventLocation)
        val eventImage = view?.findViewById<ImageView>(R.id.imageView)


        val eventDescriptionEditText = view?.findViewById<EditText>(R.id.eventDescription)
        val eventDateButton = view?.findViewById<Button>(R.id.eventDate)
        val eventDatabase: EventDatabase = EventDatabase()
        val commentDatabase: CommentDatabase = CommentDatabase()

        val formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())

        var eventDate = Timestamp.now()

        eventDateButton?.text = formatter.format(Timestamp.now().toDate().toInstant())


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    val testEvent: Event = Event()
                    val context: Context = this.requireContext()
                    val bitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            this.requireContext().contentResolver,
                            uri
                        )
                    )
                    val imageView: ImageView? = eventImage
                    if (imageView != null) {
                        imageView.setImageBitmap(
                            Bitmap.createScaledBitmap(
                                bitmap,
                                Math.round(bitmap.width * (720f / bitmap.height.toFloat())),
                                720,
                                false
                            )
                        )
                    }

                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        view?.findViewById<FloatingActionButton>(R.id.postEventButton)?.setOnClickListener {
            val title = eventTitleEditText?.text.toString()
            val location = eventLocationEditText?.text.toString()
            val start = eventDateButton?.text.toString()
            val description = eventDescriptionEditText?.text.toString()
            val image: String = encodeBitmapToString(eventImage?.drawable!!.toBitmap())

            val allEvents = runBlocking { eventDatabase.getAllEvents() }

            //add event to database
            val event =
                Event(
                    allEvents,
                    title,
                    description,
                    location,
                    eventDate,
                    0,
                    image
                )
            runBlocking { eventDatabase.insertEvent(event) }

            //clear fields
            eventTitleEditText?.text?.clear()
            eventLocationEditText?.text?.clear()
            eventImage?.setImageResource(R.drawable.ic_launcher_background)
            eventDateButton?.text = ""
            eventDescriptionEditText?.text?.clear()

            Toast.makeText(requireContext(), "Event created!", Toast.LENGTH_SHORT).show()


            //navigate to home fragment
            activity?.supportFragmentManager?.popBackStack()
        }

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Update the calendar with the selected date
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Update the button text with the selected date
                eventDateButton?.text = formatter.format(cal.time.toInstant())

                // Update the eventDate with the selected date
                eventDate = Timestamp(cal.time)
            }

        eventDateButton?.setOnClickListener {
            DatePickerDialog(
                requireContext(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }



        eventImage?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        return view
    }
}