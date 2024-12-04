package com.socialite.socialite.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class CommentDatabase {
    private val db = Firebase.firestore
    private var collectionReference: CollectionReference = db.collection("comments")


    init {
        collectionReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("FirestoreDatasource", "Listen failed.", e)
                return@addSnapshotListener
            } else {
                if (snapshot != null && !snapshot.isEmpty) {
                    Log.d(
                        "FirestoreDatasource",
                        "Current number of documents: ${snapshot.documents.size}"
                    )
                }


            }
        }
    }

    suspend fun getAllComments(): List<Comment> {
        return try {
            val snapshot = collectionReference.get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Comment::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreDatasource", "Error fetching comments: $e")
            emptyList()
        }
    }

    suspend fun getAllCommentsForEvent(eventId: Int): List<Comment> {
        return try {
            val snapshot = collectionReference.whereEqualTo("eventId", eventId).get().await()
            val comments = snapshot.documents.mapNotNull { document ->
                document.toObject(Comment::class.java)
            }
            return comments
        } catch (e: Exception) {
            Log.e("FirestoreDatasource", "Error fetching comments: $e")
            emptyList()
        }
    }

    suspend fun getComment(commentId: Int): Comment? {
        return try {
            val querySnapshot = collectionReference
                .whereEqualTo("id", commentId)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(Comment::class.java)
            }[0]

        } catch (exception: Exception) {
            Log.w("FireStore", "Error getting documents: ", exception)
            null // Return null if the query fails
        }
    }

    suspend fun insertComment(comment: Comment) {
        try {
            collectionReference.add(comment).await()
        } catch (exception: Exception) {
            Log.w("FireStore", "Error adding documents: ", exception)
        }

    }


    suspend fun updateComment(modifiedComment: Comment) {
        // Will update the document with the same id as modifiedEvent
        try {

            // Delete old comment, create a new one with updated data
            val querySnapshot = collectionReference
                .whereEqualTo("id", modifiedComment.id)
                .get()
                .await()
            querySnapshot.documents.forEach {
                it.reference.delete()
            }

            insertComment(modifiedComment)

        } catch (exception: Exception) {
            Log.w("FireStore", "Error getting documents: ", exception)

        }
    }


}
