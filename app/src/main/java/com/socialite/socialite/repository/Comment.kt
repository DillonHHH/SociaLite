package com.socialite.socialite.repository

import android.content.Context
import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity(tableName = "comments")
data class Comment (
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "eventId") var eventId: Int?,
    @ColumnInfo(name ="name") var name: String,
    @ColumnInfo(name ="comment") var comment: String,
    @ColumnInfo(name ="image") var image: String?, // compressed and string encoded image
){
    constructor(): this(null, null, "","", null)

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
        if (this.image.isNullOrEmpty()){
            return null
        }
        val decodedString = Base64.decode(this.image!!)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}