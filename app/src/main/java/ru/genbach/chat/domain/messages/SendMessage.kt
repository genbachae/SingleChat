package ru.genbach.chat.domain.messages

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject
//  Для отправки сообщения.
class SendMessage @Inject constructor(
    private val messagesRepository: MessagesRepository  //  объект репозитория MessagesRepository.
) : UseCase<None, SendMessage.Params>() {   //  Наследуется от:    UseCase с параметризированными типами None и SendMessage.Params.
    //  обращается к репозиторию для получения сообщений. Принимает Params. Возвращает Either<Failure, None>>.
    override suspend fun run(params: Params) = messagesRepository.sendMessage(params.toId, params.message, params.image)

    data class Params(val toId: Long, val message: String, val image: String)   //  содержит в себе поля(toId, message, image) для передачи параметров.
}