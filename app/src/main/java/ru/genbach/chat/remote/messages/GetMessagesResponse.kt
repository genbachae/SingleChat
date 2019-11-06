package ru.genbach.chat.remote.messages

import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.remote.core.BaseResponse

class GetMessagesResponse ( //  POJO-класс. Для хранения ответа сервера при получении списка сообщений и чатов.
    success: Int,
    message: String,        //  содержит список сообщений. Тип: List<MessageEntity>.
    val messages: List<MessageEntity>
) : BaseResponse(success, message)