package com.socialite.socialite

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.socialite.socialite.repository.Event
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.main)

        // Set default fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()

        // Set click listeners for navigation buttons
        findViewById<ImageButton>(R.id.nav_home).setOnClickListener {
            replaceFragment(HomeFragment())
        }
        findViewById<ImageButton>(R.id.nav_add_post).setOnClickListener {
            replaceFragment(AddPostFragment())
        }
        findViewById<ImageButton>(R.id.nav_calendar).setOnClickListener {
            replaceFragment(CalendarFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val testEvent: Event = Event()
            val context: Context = this;
            val selectedFile = data?.data!! // The URI with the location of the file

            val bitmap = ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    this.contentResolver, selectedFile
                )
            )
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()
    }
}

