package com.socialite.socialite.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

fun encodeBitmapToString(image: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    var quality = 50

    // Compress and reduce quality until the size meets the limit
    do {
        outputStream.reset() // Clear the output stream
        image.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        quality -= 5
    } while (outputStream.toByteArray().size > 500_000 && quality > 0)

    // Base64 encode the compressed image
    val streamBytes = outputStream.toByteArray()
    val encodedImage = Base64.encodeToString(streamBytes, Base64.DEFAULT) // Use android.util.Base64

    return encodedImage
}

fun decodeBitmapFromString(image: String): Bitmap? {
    val decodedString = Base64.decode(image, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    if (bitmap == null) {
        Log.e("Comment", "Failed to decode Bitmap from Base64 string.")
    }
    return bitmap
}