package com.socialite.socialite.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class EventDatabase {
    private val db = Firebase.firestore
    private var collectionReference: CollectionReference = db.collection("events")

    init{
        collectionReference.addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w("FirestoreDatasource", "Listen failed.", e)
                return@addSnapshotListener
            }else{
                if (snapshot != null && !snapshot.isEmpty) {
                    Log.d(
                        "FirestoreDatasource",
                        "Current number of documents: ${snapshot.documents.size}"
                    )
                }


            }
        }
    }

    suspend fun fetchAllEvents(): List<Event> {
        return try {
            val snapshot = collectionReference.get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreDatasource", "Error fetching events: $e")
            emptyList()
        }
    }

    suspend fun insert(event: Event){
        try {
            collectionReference.add(event).await()
        }
        catch (exception: Exception){
            Log.w("FireStore", "Error getting documents: ", exception)

        }

    }

    suspend fun get(eventId: Int): Event? {
        return try {
            val querySnapshot = collectionReference
                .whereEqualTo("id", eventId)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(Event::class.java)
            }[0]

        } catch (exception: Exception) {
            Log.w("FireStore", "Error getting documents: ", exception)
            null // Return null if the query fails
        }
    }



}
