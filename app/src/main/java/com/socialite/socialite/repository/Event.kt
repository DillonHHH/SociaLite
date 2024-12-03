package com.socialite.socialite.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.time.LocalDateTime
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name ="title") var title:String ,
    @ColumnInfo(name="description") var description:String,
    @ColumnInfo(name="location") var location:String,
    @ColumnInfo(name="start") var start:String,
    @ColumnInfo(name="image") var image:String,
){
    constructor(): this(null, "", ""," ", "", "")

    @OptIn(ExperimentalEncodingApi::class)
    suspend fun setImage(context: Context, image: File){
        val compressedImageFile = Compressor.compress(context, image) {
            resolution(1920, 1080)
            quality(50)
            format(Bitmap.CompressFormat.WEBP)
            size(500_000) // 500 KB
        }
        val encodedImage: String =  Base64.encode(compressedImageFile.readBytes())

        this.image = encodedImage
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getImage(context: Context): Bitmap? {
        val decodedString = Base64.decode(this.image)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
}

