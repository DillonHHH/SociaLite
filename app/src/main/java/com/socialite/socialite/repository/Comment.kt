package com.socialite.socialite.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream
import android.util.Base64 // Replace kotlin.io.encoding.Base64
import com.socialite.socialite.utils.encodeBitmapToString

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "eventId") var eventId: Int?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "comment") var comment: String,
    @ColumnInfo(name = "image") var image: String, // compressed and string encoded image
) {
    constructor() : this(null, null, "", "", "")

    constructor(
        comments: List<Comment>,
        eventId: Int?,
        name: String,
        comment: String,
        image: Bitmap?
    ) : this(0, eventId, name, comment, "") {
        var greatestId = 0
        for (com in comments) {
            if (com.id!! > greatestId) {
                greatestId = com.id!!
            }
        }
        this.id = ++greatestId
        if (image != null) {
            this.image = encodeBitmapToString(image)
        }
    }

}

