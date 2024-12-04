package com.socialite.socialite

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Typeface
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.socialite.socialite.repository.Comment

class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commenterNameTextView: TextView = itemView.findViewById(R.id.commenterName)
        val commentTextView: TextView = itemView.findViewById(R.id.commentText)
        val imageView: ImageView = itemView.findViewById(R.id.commentImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commenterNameTextView.text = comment.name
        holder.commentTextView.text = comment.comment

        val image: Bitmap? = comment.getImage()
        if (image != null) {
            holder.imageView.setImageBitmap(image)
        }else{
            holder.imageView.visibility = GONE
        }

        // Make commenter name bold
        holder.commenterNameTextView.setTypeface(null, Typeface.BOLD)
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}