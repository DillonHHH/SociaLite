package com.socialite.socialite

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Comment
import com.socialite.socialite.repository.CommentDatabase
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
        val submitCommentButton = findViewById<Button>(R.id.submitCommentButton)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.eventCommentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        var comments = runBlocking {
            commentDatabase.getAllCommentsForEvent(event.id!!)
        }

        val adapter = CommentAdapter(comments)

        commentsRecyclerView.adapter = adapter

        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsRecyclerView.itemAnimator = null

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

            // Handle comment submission
            submitCommentButton.setOnClickListener {

                val commenterName = commenterNameInput.text.toString()
                val commentText = commentTextInput.text.toString()

                // Add the comment to data source
                if (commenterName.isNotEmpty() && commentText.isNotEmpty()) {
                    val allComments: List<Comment> = runBlocking { commentDatabase.getAllComments() }
                    val newComment = Comment(allComments, event.id, commenterName, commentText, "")

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