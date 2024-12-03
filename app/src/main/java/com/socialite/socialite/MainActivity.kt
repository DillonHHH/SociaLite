package com.socialite.socialite

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.EventDatabase
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.main)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.nav_home).setOnClickListener {
            replaceFragment(HomeFragment())
        }
        findViewById<ImageButton>(R.id.nav_search).setOnClickListener {
            replaceFragment(SearchFragment())
        }
        findViewById<ImageButton>(R.id.nav_calendar).setOnClickListener {
            replaceFragment(CalendarFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}

