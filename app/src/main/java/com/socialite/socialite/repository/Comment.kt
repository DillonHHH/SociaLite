package com.socialite.socialite.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "eventId") var eventId: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "comment") var comment: String,
    @ColumnInfo(name = "image") private var image: String?, // compressed and string encoded image
) {
    constructor(): this(null, null, "", "","")

    constructor(
        comments: List<Comment>,
        eventId: Int?,
        name: String,
        comment: String,
        image: String
    ) : this(0, eventId, name, comment, image) {
        var greatestId = 0
        for (com in comments) {
            if (com.id!! > greatestId) {
                greatestId = com.id!!
            }
        }
        this.id = ++greatestId
    }

    // required for deserialization from database
    fun setImage(image: String) {
        this.image = image
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun setImageWithBitmap(image: Bitmap) {

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
            if (quality <= 0) {
                break
            }
        }

        val streamBytes = outputStream.toByteArray()
        val encodedImage: String = Base64.encode(streamBytes)

        this.image = encodedImage

    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getImage(): Bitmap? {
        if (this.image.isNullOrEmpty()) {
            return null
        }
        val decodedString = Base64.decode(this.image!!)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}