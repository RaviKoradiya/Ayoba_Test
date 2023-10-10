package com.ayoba.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ayoba.room.dao.ChatDao
import com.ayoba.room.entity.ChatMessage
import com.ayoba.room.entity.ChatRoom


@Database(
    entities = [ChatRoom::class, ChatMessage::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}