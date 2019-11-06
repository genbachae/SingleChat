package ru.genbach.chat.domain.messages

import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None

interface MessagesRepository {  //  Интерфейс репозитория. Для взаимодействия с сообщениями(получение, отправление).
    fun sendMessage(            //  отправляет сообщение. Принимает: Long: toId; String: message, image. Возвращает Either<Failure, None>.
        toId: Long,
        message: String,
        image: String
    ): Either<Failure, None>
    //  получение списка чатов. Принимает Boolean: needFetch(определяет, необходимо загружать данные из сети – true, или же из бд — false).
    //  Возвращает Either<Failure, List<MessageEntity>>.
    fun getChats(needFetch: Boolean): Either<Failure, List<MessageEntity>>
    //  получение списка сообщений с определенным контактом. Принимает: Long: contactId; Boolean: needFetch.
    //  Возвращает Either<Failure, List<MessageEntity>>.
    fun getMessagesWithContact(contactId: Long, needFetch: Boolean): Either<Failure, List<MessageEntity>>
}