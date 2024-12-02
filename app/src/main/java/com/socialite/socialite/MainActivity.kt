package com.socialite.socialite

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.decodeBitmap
import com.socialite.socialite.repository.CommentDatabase
import com.socialite.socialite.repository.Event
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


        val list = assets.list("")
        Log.d("TAGListAst", "listAssetFiles:s: ${list?.size}")
        if (list!!.isNotEmpty()) {
            for (file: String in list) {
                Log.d("TEST", file)
            }
        }

        val btn: Button = findViewById(R.id.button)
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

                    runBlocking {
                        testEvent.setImage(
                            context,
                            bitmap
                        )
                    }
                    val imageView: ImageView = findViewById(R.id.imageView)

                    imageView.setImageBitmap(runBlocking { testEvent.getImage(context) })
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        btn.setOnClickListener {


            // Registers a photo picker activity launcher in single-select mode.


// Include only one of the following calls to launch(), depending on the types
// of media that you want to let the user choose from.

// Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

//            val intent = Intent()
//                .setType("*/*")
//                .setAction(Intent.ACTION_GET_CONTENT)
//
//            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
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
                    this.contentResolver,
                    selectedFile
                )
            )

            runBlocking {
                testEvent.setImage(
                    context,
                    bitmap
                )
            }
            val imageView: ImageView = findViewById(R.id.imageView)

            imageView.setImageBitmap(runBlocking { testEvent.getImage(context) })
        }
    }


}

