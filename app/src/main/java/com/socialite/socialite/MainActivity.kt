package com.socialite.socialite

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.EventDatabase
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.temp_layout)

//        val button: Button = findViewById(R.id.test_api)

        val events = EventDatabase()
        val comments = CommentDatabase()
//
//        button.setOnClickListener {
//            runBlocking {
//                Log.d("test", runBlocking {comments.fetchCommentsForEvent(5)}.toString())
//            }
//        }


    }
}

