package ru.genbach.chat.data.messages

import ru.genbach.chat.data.account.AccountCache
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.domain.messages.MessagesRepository
import ru.genbach.chat.domain.type.*

class MessagesRepositoryImpl(   //  Реализация интерфейса репозитория. Для взаимодействия с сообщениями.
    private val messagesRemote: MessagesRemote,
    private val messagesCache: MessagesCache,
    private val accountCache: AccountCache
) : MessagesRepository {    //  Наследуется от: Интерфейса MessagesRepository.

//    вызывает accountCache.getCurrentAccount() для получения id и токена текущего пользователя.
//    Преобразовывает Either<Failure, AccountEntity> в Either<Failure, List<MessageEntity>> с помощью оператора
//    В блоке flatMap проверяет значение needFetch: в случае true — будет выполнена загрузка чатов из сети и сохранение их в бд;
//    в случае false – будет выполнена загрузка из бд. Принимает Boolean: needFetch.
//    С помощью оператора distinct выполняется отсеивание одинаковых элементов, так как для отображения списка чатов и
//    списка сообщений используется один и тот же тип(MessageEntity). Возвращает Either<Failure, List<MessageEntity>>.
    override fun getChats(needFetch: Boolean): Either<Failure, List<MessageEntity>> {
        return accountCache.getCurrentAccount().flatMap { account ->
            return@flatMap if (needFetch) {
                messagesRemote.getChats(account.id, account.token).onNext {
                    it.map { message ->
                        if (message.senderId == account.id) {
                            message.fromMe = true
                        }
                        messagesCache.saveMessage(message)
                    }
                }
            } else {
                Either.Right(messagesCache.getChats())
            }
        }
            .map { it.distinctBy {
                it.contact?.id }
            }
    }

    //  вызывает accountCache.getCurrentAccount() для получения id и токена текущего пользователя.
    //  Преобразовывает Either<Failure, AccountEntity> в Either<Failure, List<MessageEntity>> с помощью оператора
    //  В блоке flatMap проверяет значение needFetch: в случае true — будет выполнена загрузка сообщений из сети и сохранение их в бд;
    //  в случае false – будет выполнена загрузка из бд. Принимает: Long: contactId; Boolean: needFetch.
    //  Возвращает Either<Failure, List<MessageEntity>>.
    override fun getMessagesWithContact(contactId: Long, needFetch: Boolean): Either<Failure, List<MessageEntity>> {
        return accountCache.getCurrentAccount().flatMap { account ->
            return@flatMap if (needFetch) {
                messagesRemote.getMessagesWithContact(contactId, account.id, account.token).onNext {
                    it.map { message ->

                        if (message.senderId == account.id) {
                            message.fromMe = true
                        }

                        val contact = messagesCache.getChats().first { it.contact?.id == contactId }.contact
                        message.contact = contact

                        messagesCache.saveMessage(message)
                    }
                }
            } else {
                Either.Right(messagesCache.getMessagesWithContact(contactId))
            }
        }
    }

    //  вызывает getCurrentAccount() для получения id и токена текущего пользователя. В блоке flatMap отправляет сообщение.
    //  Принимает: Long: toId; String: message, image. Возвращает Either<Failure, None>.
    override fun sendMessage(
        toId: Long,
        message: String,
        image: String
    ): Either<Failure, None> {
        return accountCache.getCurrentAccount().flatMap {
            messagesRemote.sendMessage(it.id, toId, it.token, message, image)
        }
    }

}