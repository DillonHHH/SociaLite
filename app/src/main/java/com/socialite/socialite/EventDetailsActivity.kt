package com.socialite.socialite

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Comment
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.Event
import com.socialite.socialite.repository.EventDatabase
import kotlinx.coroutines.runBlocking

class EventDetailsActivity : AppCompatActivity() {

    private var isLiked = false
    private val eventDatabase: EventDatabase = EventDatabase()
    private val commentDatabase: CommentDatabase = CommentDatabase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Retrieve data passed via Intent
        val event = runBlocking {
            eventDatabase.getEvent(intent.getIntExtra("EXTRA_ID", -1))
        }!!
        // Find Views
        val eventTitleView: TextView = findViewById(R.id.eventTitle)
        val eventDescriptionView: TextView = findViewById(R.id.eventDescription)
        val eventLocationView: TextView = findViewById(R.id.eventLocation)
        val eventDateView: TextView = findViewById(R.id.eventDate)
        val eventImageView: ImageView = findViewById(R.id.imageView)

        val likeButton = findViewById<ImageButton>(R.id.like_event)
        val commentEventButton = findViewById<ImageButton>(R.id.comment_event)
        val commentInputLayout = findViewById<LinearLayout>(R.id.commentInputLayout)
        val commenterNameInput = findViewById<EditText>(R.id.commenterNameInput)
        val commentTextInput = findViewById<EditText>(R.id.commentTextInput)
        val commentImageBeforePost = findViewById<ImageView>(R.id.commentImageBeforePost)
        val addImageImageButton = findViewById<Button>(R.id.addImageImageButton)
        val submitCommentButton = findViewById<Button>(R.id.submitCommentButton)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.eventCommentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        var comments = runBlocking {
            commentDatabase.getAllCommentsForEvent(event.id!!)
        }

        val adapter = CommentAdapter(comments)
        commentsRecyclerView.adapter = adapter

        // Populate Views
        eventTitleView.text = event.title
        eventLocationView.text = event.location
        eventDescriptionView.text = event.description
        eventDateView.text = event.start

        val image: Bitmap? = event.getImage()
        if (image != null) {
            eventImageView.setImageBitmap(image)
        } else {
            eventImageView.visibility = GONE
        }

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.nav_back).setOnClickListener {
            finish()
        }

        likeButton.setOnClickListener {
            //change image source to filled in heart
            isLiked = !isLiked
            if (isLiked) {
                likeButton.setImageResource(R.drawable.ic_filled_heart)
            } else {
                likeButton.setImageResource(R.drawable.ic_empty_heart)
            }
        }

        commentEventButton.setOnClickListener {
            // Show the comment input layout
            commentInputLayout.visibility = View.VISIBLE

//            commentImage.setOnClickListener {
//                Log.d("CommentImage", "Clicked")
//            }

            val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        val testEvent: Event = Event()
                        val context: Context = this;
                        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, uri))
                        val imageView: ImageView = commentImageBeforePost
//                    runBlocking { testEvent.setImage( bitmap ) }
//                    imageView.setImageBitmap(runBlocking { testEvent.getImage() })
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                }
            addImageImageButton.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            Registers a photo picker activity launcher in single-select mode.
//            Include only one of the following calls to launch(), depending on the types
//            of media that you want to let the user choose from.
//
//            Launch the photo picker and let the user choose only images.

//
//            val intent = Intent()
//                .setType("*/*")
//                .setAction(Intent.ACTION_GET_CONTENT)
//
//            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
            }

            // Handle comment submission
            submitCommentButton.setOnClickListener {
                val commenterName = commenterNameInput.text.toString()
                val commentText = commentTextInput.text.toString()

                // Add the comment to data source
                if (commenterName.isNotEmpty() && commentText.isNotEmpty()) {
                    val newComment = Comment(null, 1, commenterName, commentText, null)
                    //TODO Replace this with actual data source
                    runBlocking {
                        commentDatabase.insertComment(newComment)
                    }

                    comments = runBlocking {
                        commentDatabase.getAllCommentsForEvent(event.id!!)
                    }

                    // Update the RecyclerView adapter
                    adapter.notifyItemInserted(comments.size - 1)
                }

                // Hide the comment input layout
                commentInputLayout.visibility = View.GONE

                // Clear the input fields
                commenterNameInput.text.clear()
                commentTextInput.text.clear()
            }
        }
    }
}