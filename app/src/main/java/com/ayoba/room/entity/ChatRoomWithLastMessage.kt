package com.ayoba.room.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatRoomWithLastMessage(
    @Embedded val chatRoom: ChatRoom,
    @Relation(
        parentColumn = "rId",
        entityColumn = "roomId",
        entity = ChatMessage::class
    )
    val lastMessage: ChatMessage?
) : Parcelable