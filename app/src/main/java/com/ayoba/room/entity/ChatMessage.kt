package com.ayoba.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "chat_message",
    foreignKeys = [
        ForeignKey(
            entity = ChatRoom::class,
            parentColumns = arrayOf("rId"),
            childColumns = arrayOf("roomId"),
            onDelete = CASCADE
        )],
    indices = [Index(value = ["roomId"]), Index(value = ["timestamp"])]
)
@Parcelize
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val mId: Long = 0,
    val roomId: Long,
    val message: String,
    val timestamp: Long
) : Parcelable {
    constructor(roomId: Long, message: String, timestamp: Long) : this(
        0,
        roomId,
        message,
        timestamp
    )
}

