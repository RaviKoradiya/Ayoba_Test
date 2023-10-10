package com.ayoba.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "chat_room",
    indices = [Index(value = ["name"], unique = true)]
)
@Parcelize
data class ChatRoom(
    @PrimaryKey(autoGenerate = true) val rId: Long = 0,
    val createdAt: Long,
    val name: String,
    val isMuted: Boolean
) : Parcelable {
    constructor(createdAt: Long, name: String, isMuted: Boolean) : this(
        0,
        createdAt,
        name,
        isMuted
    )
}