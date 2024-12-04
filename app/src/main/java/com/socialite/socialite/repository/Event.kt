package com.socialite.socialite.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "location") var location: String,
    @ColumnInfo(name = "start") var start: String,
    @ColumnInfo(name = "image") var image: String,
) {
    constructor() : this(null, "", "", " ", "", "")

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun setImage(context: Context, image: Bitmap) {

        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        var quality = 50

        while (outputStream.toByteArray().size > 500000) {
            Log.d("TAG", outputStream.toByteArray().size.toString())
            val streamBytes = outputStream.toByteArray()
            val tempImage = BitmapFactory.decodeByteArray(streamBytes, 0, streamBytes.size)
            outputStream.reset()
            tempImage.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            quality -= 5
            if (quality <= 0){
                break
            }
        }

        val streamBytes = outputStream.toByteArray()
        val encodedImage: String = Base64.encode(streamBytes)

        this.image = encodedImage
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getImage(context: Context): Bitmap? {
        val decodedString = Base64.decode(this.image)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    // https://stackoverflow.com/questions/46943860/idiomatic-way-to-generate-a-random-alphanumeric-string-in-kotlin
    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}