package com.ayoba.viewmodels

import androidx.lifecycle.ViewModel
import com.ayoba.repository.AyobaRepository
import com.ayoba.room.entity.ChatMessage
import com.ayoba.room.entity.ChatRoom

class AyobaViewModel(val repository: AyobaRepository) : ViewModel() {

    fun getAllRooms() = repository.getAllRooms()

    suspend fun insert(chatRoom: ChatRoom) = repository.insert(chatRoom)

    suspend fun delete(chatRoom: ChatRoom) = repository.delete(chatRoom)

    suspend fun deleteChats(idList: List<Long>) = repository.deleteChats(idList)

    suspend fun updateChats(isMuted: Boolean, idList: List<Long>) =
        repository.updateChats(isMuted, idList)

    fun getAllMessages(roomId: Long) = repository.getAllMessages(roomId)

    suspend fun insert(chatMessage: ChatMessage) = repository.insert(chatMessage)

    suspend fun delete(chatMessage: ChatMessage) = repository.delete(chatMessage)

    suspend fun deleteMessages(idList: List<Long>) = repository.deleteMessages(idList)
}