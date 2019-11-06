package ru.genbach.chat.data.messages

import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None

interface MessagesRemote {  //  Интерфейс для серверной части. Содержит функции для взаимодействия с сообщениями на сервере.
    //  получение списка чатов. Принимает: Long: userId, String: token Возвращает Either<Failure, List<MessageEntity>>.
    fun getChats(userId: Long, token: String): Either<Failure, List<MessageEntity>>
    //  получение списка сообщений с контактом. Принимает: Long: contactId, userId; String: token. Возвращает Either<Failure, List<MessageEntity>>.
    fun getMessagesWithContact(contactId: Long, userId: Long, token: String): Either<Failure, List<MessageEntity>>

    fun sendMessage(    //  отправляет сообщение. Принимает: Long: fromId, toId; String: token, message, image. Возвращает Either<Failure, None>.
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Either<Failure, None>
}