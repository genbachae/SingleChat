package ru.genbach.chat.data.messages

import ru.genbach.chat.domain.messages.MessageEntity

interface MessagesCache {   //  Интерфейс для бд. Содержит функции для взаимодействия с сообщениями в бд.
    fun saveMessage(entity: MessageEntity)  //  сохраняет сообщение. Принимает: MessageEntity. Ничего не возвращает.

    fun getChats(): List<MessageEntity>     //  получение списка чатов из бд. Ничего не принимает. Возвращает List<MessageEntity>.
    //  получение списка сообщений с контактом из бд. Принимает: Long: contactId. Возвращает List<MessageEntity>.
    fun getMessagesWithContact(contactId: Long): List<MessageEntity>
}