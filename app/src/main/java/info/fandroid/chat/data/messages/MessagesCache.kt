package info.fandroid.chat.data.messages

import info.fandroid.chat.domain.messages.MessageEntity

interface MessagesCache {
    fun saveMessage(entity: MessageEntity)

    fun getChats(): List<MessageEntity>

    fun getMessagesWithContact(contactId: Long): List<MessageEntity>
}