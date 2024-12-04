package com.socialite.socialite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.Event
import com.socialite.socialite.repository.EventDatabase
import kotlinx.coroutines.runBlocking

class AddPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_post, container, false)

        val eventTitleEditText = view?.findViewById<EditText>(R.id.eventTitle)
        val eventLocationEditText = view?.findViewById<EditText>(R.id.eventLocation)
        val eventImage = view?.findViewById<ImageView>(R.id.imageView)
        val eventDateEditText = view?.findViewById<EditText>(R.id.eventDate)
        val eventDescriptionEditText = view?.findViewById<EditText>(R.id.eventDescription)
        val eventDatabase: EventDatabase = EventDatabase()
        val commentDatabase: CommentDatabase = CommentDatabase()


        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                val testEvent: Event = Event()
                val context: Context = this.requireContext()
                val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.requireContext().contentResolver, uri))
                val imageView: ImageView? = eventImage
                if (imageView != null) {
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false))
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        view?.findViewById<FloatingActionButton>(R.id.postEventButton)?.setOnClickListener {
            val title = eventTitleEditText?.text.toString()
            val location = eventLocationEditText?.text.toString()
            val start = eventDateEditText?.text.toString()
            val description = eventDescriptionEditText?.text.toString()
            val image = eventImage?.drawable.toString()

            val allEvents = runBlocking { eventDatabase.getAllEvents() }

            //add event to database
            val event = Event(allEvents, title, description, location, start, 0, image)
            runBlocking { eventDatabase.insertEvent(event) }

            //clear fields
            eventTitleEditText?.text?.clear()
            eventLocationEditText?.text?.clear()
            eventImage?.setImageResource(R.drawable.ic_launcher_background)
            eventDateEditText?.text?.clear()
            eventDescriptionEditText?.text?.clear()

            Toast.makeText(requireContext(), "Event created!", Toast.LENGTH_SHORT).show()

            //navigate to home fragment
            //findNavController().navigate(R.id.action_addPostFragment_to_homeFragment)
        }


        eventImage?.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        return view
    }
}