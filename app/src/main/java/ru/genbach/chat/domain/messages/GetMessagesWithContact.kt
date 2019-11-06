package ru.genbach.chat.domain.messages

import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject
//  UseCase. Для получения списка сообщений с контактом.
//  Наследуется от: UseCase с параметризированными типами List<MessageEntity> и GetMessagesWithContact.Params.
class GetMessagesWithContact @Inject constructor(
    private val messagesRepository: MessagesRepository      //  объект репозитория MessagesRepository.
) : UseCase<List<MessageEntity>, GetMessagesWithContact.Params>() {
    //  обращается к репозиторию для получения сообщений. Принимает Params. Возвращает Either<Failure, List<MessageEntity>>.
    override suspend fun run(params: Params) = messagesRepository.getMessagesWithContact(params.contactId, params.needFetch)

    data class Params(val contactId: Long, val needFetch: Boolean)  //  содержит в себе поля(contactId, needFetch) для передачи параметров.
}