package com.ayoba.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ayoba.room.entity.ChatMessage
import com.ayoba.room.entity.ChatRoom
import com.ayoba.room.entity.ChatRoomWithLastMessage

@Dao
interface ChatDao {

    @Transaction
    @Query("SELECT chat_room.*, chat_message.*, MAX(chat_message.timestamp) FROM chat_room LEFT JOIN chat_message ON chat_room.rId = chat_message.roomId GROUP BY chat_room.rId")
    fun getAllRooms(): LiveData<List<ChatRoomWithLastMessage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chatRoom: ChatRoom): Long

    @Delete
    suspend fun delete(chatRoom: ChatRoom)

    @Query("DELETE from chat_room where rId in (:idList)")
    suspend fun deleteChats(idList: List<Long>)

    @Query("UPDATE chat_room SET isMuted = :isMuted WHERE rId IN (:idList)")
    suspend fun updateChats(isMuted: Boolean, idList: List<Long>)

    @Query("SELECT * FROM chat_message WHERE roomId = :roomId ORDER BY timestamp DESC")
    fun getAllMessages(roomId: Long): LiveData<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chatMessage: ChatMessage): Long

    @Delete
    suspend fun delete(chatMessage: ChatMessage)

    @Query("DELETE FROM chat_message WHERE mId IN (:idList)")
    suspend fun deleteMessages(idList: List<Long>)
}