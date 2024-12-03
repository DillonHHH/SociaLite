package com.socialite.socialite

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Comment

class EventDetailsActivity : AppCompatActivity() {

    private var isLiked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        // Retrieve data passed via Intent
        val eventTitle = intent.getStringExtra("EXTRA_TITLE")
        // Find Views
        val eventTitleView: TextView = findViewById(R.id.eventTitle)
        val likeButton = findViewById<ImageButton>(R.id.like_event)
        val commentEventButton = findViewById<ImageButton>(R.id.comment_event)
        val commentInputLayout = findViewById<LinearLayout>(R.id.commentInputLayout)
        val commenterNameInput = findViewById<EditText>(R.id.commenterNameInput)
        val commentTextInput = findViewById<EditText>(R.id.commentTextInput)
        val submitCommentButton = findViewById<Button>(R.id.submitCommentButton)

        val commentsRecyclerView = findViewById<RecyclerView>(R.id.eventCommentsRecyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)

        val comments = mutableListOf<Comment>(
            // Dummy Comments
            Comment(null, 1, "User1", "This is a great event!", null),
            Comment(null, 1, "User2", "I'm looking forward to it.", null)
        )

        val adapter = CommentAdapter(comments)
        commentsRecyclerView.adapter = adapter

        // Populate Views
        eventTitleView.text = eventTitle

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.nav_back).setOnClickListener {
            finish()
        }

        likeButton.setOnClickListener{
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
                    val newComment = Comment(null, 1, commenterName, commentText, null)
                    //TODO Replace this with actual data source
                    comments.add(newComment)
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