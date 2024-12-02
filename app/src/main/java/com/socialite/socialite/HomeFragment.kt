package com.socialite.socialite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the button and set the click listener
        val button: Button = view.findViewById(R.id.test_api)
//        val imageView: ImageView = view.findViewById(R.id.imageView)
//        button.setOnClickListener {
//            val evInt = EventInterface()
//            // Use lifecycleScope to launch the coroutine
//            viewLifecycleOwner.lifecycleScope.launch {
//                val events = evInt.getEventsByLocation("Fayetteville, Arkansas")
//                val bitMap = events[0].thumbnailLink?.let { it1 -> evInt.getThumbnailWithLink(it1) }
//                imageView.setImageBitmap(bitMap)
//            }
//        }
    }
}