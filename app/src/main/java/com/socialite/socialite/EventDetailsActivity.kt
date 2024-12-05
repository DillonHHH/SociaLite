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
import com.socialite.socialite.utils.decodeBitmapFromString
import kotlinx.coroutines.runBlocking
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventDetailsActivity : AppCompatActivity() {

    private var isLiked = false
    private val eventDatabase: EventDatabase = EventDatabase()
    private val commentDatabase: CommentDatabase = CommentDatabase()
    private var newCommentImage: Bitmap? = null

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
        val commentCounter = findViewById<TextView>(R.id.commentCount)
        val commentInputLayout = findViewById<LinearLayout>(R.id.commentInputLayout)
        val commenterNameInput = findViewById<EditText>(R.id.commenterNameInput)
        val commentTextInput = findViewById<EditText>(R.id.commentTextInput)
        val commentImageBeforePost = findViewById<ImageView>(R.id.commentImageBeforePost)
        val addImageImageButton = findViewById<Button>(R.id.addImageImageButton)
        val submitCommentButton = findViewById<Button>(R.id.submitCommentButton)
        val likeCounter = findViewById<TextView>(R.id.likeCount)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.eventCommentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        var comments = runBlocking {
            commentDatabase.getAllCommentsForEvent(event.id!!)
        }

        commentCounter.text = comments.size.toString()

        val adapter = CommentAdapter(comments)

        commentsRecyclerView.adapter = adapter

        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.itemAnimator = null

        // Populate Views
        eventTitleView.text = event.title
        eventLocationView.text = event.location
        eventDescriptionView.text = event.description
        val instant = event.start.toDate().toInstant()
        val formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault())
        eventDateView.text = formatter.format(instant)

        val image: Bitmap? = decodeBitmapFromString(event.image)
        if (image != null) {
            eventImageView.setImageBitmap(image)
        } else {
            eventImageView.visibility = GONE
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    val testEvent: Event = Event()
                    val context: Context = this;
                    val bitmap = ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            this.contentResolver,
                            uri
                        )
                    )
                    val imageView: ImageView = commentImageBeforePost
                    imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false))
                    newCommentImage = Bitmap.createScaledBitmap(
                        bitmap,
                        Math.round(bitmap.width * (720f / bitmap.height.toFloat())), 720, false
                    )
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.nav_back).setOnClickListener {
            finish()
        }

        likeButton.setOnClickListener {
            //change image source to filled in heart
            isLiked = !isLiked
            if (isLiked) {
                likeCounter.text = (likeCounter.text.toString().toInt() + 1).toString()
                likeButton.setImageResource(R.drawable.ic_filled_heart)
            } else {
                likeCounter.text = (likeCounter.text.toString().toInt() - 1).toString()
                likeButton.setImageResource(R.drawable.ic_empty_heart)
            }

        }

        commentEventButton.setOnClickListener {
            // Show the comment input layout
            commentInputLayout.visibility = View.VISIBLE

            addImageImageButton.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            // Handle comment submission
            submitCommentButton.setOnClickListener {

                val commenterName = commenterNameInput.text.toString()
                val commentText = commentTextInput.text.toString()

                if (commenterName.isNotEmpty() && commentText.isNotEmpty()) {
                    val allComments: List<Comment> =
                        runBlocking { commentDatabase.getAllComments() }
                    val newComment =
                        Comment(allComments, event.id, commenterName, commentText, newCommentImage)

                    runBlocking {
                        commentDatabase.insertComment(newComment)
                    }

                    comments = runBlocking {
                        commentDatabase.getAllCommentsForEvent(event.id!!)
                    }

                    val newAdapter = CommentAdapter(comments)
                    commentsRecyclerView.adapter = newAdapter

                }

                // Hide the comment input layout
                commentInputLayout.visibility = View.GONE

                // Clear the input fields
                commenterNameInput.text.clear()
                commentTextInput.text.clear()
                commentCounter.text = (commentCounter.text.toString().toInt() + 1).toString()
            }
        }
    }
}