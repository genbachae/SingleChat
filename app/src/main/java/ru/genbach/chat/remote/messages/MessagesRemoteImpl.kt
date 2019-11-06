package ru.genbach.chat.remote.messages

import ru.genbach.chat.data.messages.MessagesRemote
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.remote.core.Request
import ru.genbach.chat.remote.service.ApiService
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
    //  Реализация интерфейса. Для взаимодействия с сообщениями в сети.
class MessagesRemoteImpl @Inject constructor(
    private val request: Request,       //  объект для создания запросов.
    private val service: ApiService     //  объект для формирования API запросов.
) : MessagesRemote {        //  Наследуется от: Интерфейса MessagesRemote.
    //  выполняет запрос для получения списка чатов. Принимает Long: userId, String: token. Возвращает Either<Failure, List<MessageEntity>>.
    override fun getChats(userId: Long, token: String): Either<Failure, List<MessageEntity>> {
        return request.make(service.getLastMessages(createGetLastMessagesMap(userId, token))) { it.messages }
    }
    //  выполняет запрос для получения списка сообщений. Принимает Long: contactId, userId, String: token. Возвращает Either<Failure, List<MessageEntity>>.
    override fun getMessagesWithContact(
        contactId: Long,
        userId: Long,
        token: String
    ): Either<Failure, List<MessageEntity>> {
        return request.make(service.getMessagesWithContact(createGetMessagesWithContactMap(contactId, userId,token))) { it.messages }
    }
    //  выполняет запрос для отправления сообщения. Принимает Long: fromId, toId, String: token, message, image. Возвращает Either<Failure, None>.
    override fun sendMessage(
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Either<Failure, None> {
        return request.make(service.sendMessage(
            createSendMessageMap(fromId, toId, token, message, image))) { None() }
    }



    private fun createGetLastMessagesMap(   //  вспомогательная функция, преобразовывающаа параметры в Map<String, String>.
        userId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)

        return map
    }

    private fun createGetMessagesWithContactMap(   //  вспомогательная функция, преобразовывающаа параметры в Map<String, String>.
        contactId: Long,
        userId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_CONTACT_ID, contactId.toString())
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)

        return map
    }

    private fun createSendMessageMap(
        fromId: Long,
        toId: Long,
        token: String,
        message: String,
        image: String
    ): Map<String, String> {
        val date = Date().time
        var type = 1

        val map = HashMap<String, String>()

        if (image.isNotBlank()) {
            type = 2
            map.put(ApiService.PARAM_IMAGE_NEW, image)
            map.put(ApiService.PARAM_IMAGE_NEW_NAME, "user_${fromId}_${date}_photo")
        }

        map.put(ApiService.PARAM_SENDER_ID, fromId.toString())
        map.put(ApiService.PARAM_RECEIVER_ID, toId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_MESSAGE, message)
        map.put(ApiService.PARAM_MESSAGE_TYPE, type.toString())
        map.put(ApiService.PARAM_MESSAGE_DATE, date.toString())

        return map
    }
}