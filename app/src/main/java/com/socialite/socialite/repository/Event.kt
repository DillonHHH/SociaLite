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

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "location") var location: String,
    @ColumnInfo(name = "start") var start: String,
    @ColumnInfo(name = "likes") var likes: Int,
    @ColumnInfo(name = "image") var image: String,
) {
    constructor() : this(null, "", "", " ", "", 0, "")

    constructor(
        events: List<Event>,
        title: String,
        description: String,
        location: String,
        start: String,
        likes: Int,
        image: String
    ) : this(0, title, description, location, start, likes, image) {
        var greatestId = 0
        for (event in events) {
            if (event.id!! > greatestId) {
                greatestId = event.id!!
            }
        }
        this.id = ++greatestId
    }

}