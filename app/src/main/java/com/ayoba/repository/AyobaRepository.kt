package com.ayoba.repository

import com.ayoba.room.dao.ChatDao
import com.ayoba.room.entity.ChatMessage
import com.ayoba.room.entity.ChatRoom

class AyobaRepository(val localDataSource: ChatDao, remoteDataSource: Any) {

    fun getAllRooms() = localDataSource.getAllRooms()

    suspend fun insert(chatRoom: ChatRoom) = localDataSource.insert(chatRoom)

    suspend fun delete(chatRoom: ChatRoom) = localDataSource.delete(chatRoom)

    suspend fun deleteChats(idList: List<Long>) = localDataSource.deleteChats(idList)

    suspend fun updateChats(isMuted: Boolean, idList: List<Long>) = localDataSource.updateChats(isMuted, idList)

    fun getAllMessages(roomId: Long) = localDataSource.getAllMessages(roomId)

    suspend fun insert(chatMessage: ChatMessage) = localDataSource.insert(chatMessage)

    suspend fun delete(chatMessage: ChatMessage) = localDataSource.delete(chatMessage)

    suspend fun deleteMessages(idList: List<Long>) = localDataSource.deleteMessages(idList)
}