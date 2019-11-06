package ru.genbach.chat.domain.messages

import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

class GetChats @Inject constructor(
    //  val messagesRepository объект репозитория MessagesRepository.
    private val messagesRepository: MessagesRepository  //  Наследуется от: UseCase с параметризированными типами List<MessageEntity> и GetChats.Params.
) : UseCase<List<MessageEntity>, GetChats.Params>() {   //  UseCase. Для получения чатов.

    //  обращается к репозиторию для получения чатов. Принимает Params. Возвращает Either<Failure, List<MessageEntity>>.
    override suspend fun run(params: Params) = messagesRepository.getChats(params.needFetch)
    //  Внутренние классы: GetChats.Params содержит в себе поля(needFetch) для передачи параметров.
    data class Params(val needFetch: Boolean)
}