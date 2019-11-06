package ru.genbach.chat.data.friends

import ru.genbach.chat.data.account.AccountCache
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.friends.FriendsRepository
import ru.genbach.chat.domain.type.*

class FriendsRepositoryImpl(    //  Реализация интерфейса репозитория. Для взаимодействия с друзьями.
    private val accountCache: AccountCache,     //  объект для взаимодействия с аккаунтом в бд. Тип:
    private val friendsRemote: FriendsRemote,   //  объект для взаимодействия с друзьями на сервере. Тип: FriendsRemote.
    private val friendsCache: FriendsCache      //  объект
) : FriendsRepository {     //  Наследуется от: Интерфейса FriendsRepository.
    //  вызывает accountCache.getCurrentAccount() для получения id и токена текущего пользователя.
    //  Преобразовывает Either<Failure, AccountEntity> в Either<Failure, List<FriendEntity>> с помощью оператора
    //  Вызывает friendsRemote.getFriends(полученные ранее id и token) для получения списка друзей.
    //  Возвращает Either<Failure, List<FriendEntity>>.
    //  добавлен принимаемый параметр needFetch. В блоке flatMap добавлена проверка needFetch.
    //  в зависимости от его значения: будет выполнена загрузка списка друзей из сети (true) или из бд (false).
    //  Добавлен блок onNext, в котором выполняется сохранение списка друзей.
    override fun getFriends(needFetch: Boolean): Either<Failure, List<FriendEntity>> {
        return accountCache.getCurrentAccount()
            .flatMap {
                return@flatMap if (needFetch) {
                    friendsRemote.getFriends(it.id, it.token)
                } else {
                    Either.Right(friendsCache.getFriends())
                }
            }
            .onNext { it.map { friendsCache.saveFriend(it) } }
    }
    //  вызывает getCurrentAccount() для получения id и токена. Преобразовывает Either<Failure, AccountEntity> в Either<Failure,
    //  List<FriendEntity>> с помощью оператора flatMap. Вызывает friendsRemote.getFriendRequests(…) для получения списка приглашений.
    //  Возвращает Either<Failure, List<FriendEntity>>.
    //  добавлен принимаемый параметр needFetch. В блоке flatMap добавлена проверка needFetch.
    //  в зависимости от его значения: будет выполнена загрузка списка приглашений в друзья из сети (true) или из бд (false).
    //  Добавлен блок onNext, в котором выполняется присвоение полю isRequest значения 1 (является приглашением в друзья),
    //  а также сохранение списка приглашений.
    override fun getFriendRequests(needFetch: Boolean): Either<Failure, List<FriendEntity>> {
        return accountCache.getCurrentAccount()
            .flatMap {
                return@flatMap if (needFetch) {
                    friendsRemote.getFriendRequests(it.id, it.token)
                } else {
                    Either.Right(friendsCache.getFriendRequests())
                }
            }
            .onNext { it.map {
                it.isRequest = 1
                friendsCache.saveFriend(it)
            } }
    }
    //  вызывает getCurrentAccount() для получения id и токена. Преобразовывает Either<Failure, AccountEntity> в Either<Failure, None>
    //  с помощью оператора flatMap. Вызывает friendsRemote.approveFriendRequest(…) для принятия приглашения. Возвращает Either<Failure, None>.
    //  добавлен блок onNext, в котором выполняется присвоение полю isRequest значения 0 (является другом), а также сохранение друга.
    override fun approveFriendRequest(friendEntity: FriendEntity): Either<Failure, None> {
        return accountCache.getCurrentAccount()
            .flatMap { friendsRemote.approveFriendRequest(it.id, friendEntity.id, friendEntity.friendsId, it.token) }
            .onNext {
                friendEntity.isRequest = 0
                friendsCache.saveFriend(friendEntity)
            }
    }
    //  вызывает getCurrentAccount() для получения идентификатора и токена. Преобразовывает Either<Failure, AccountEntity>
    //  в Either<Failure, None> с помощью оператора flatMap. Вызывает friendsRemote.cancelFriendRequest(…) для отклонения приглашения.
    //  Возвращает Either<Failure, None>.
    //  добавлен блок onNext, в котором выполняется удаление приглашения в друзья из бд.
    override fun cancelFriendRequest(friendEntity: FriendEntity): Either<Failure, None> {
        return accountCache.getCurrentAccount()
            .flatMap { friendsRemote.cancelFriendRequest(it.id, friendEntity.id, friendEntity.friendsId, it.token) }
            .onNext { friendsCache.removeFriendEntity(friendEntity.id) }
    }
    //  вызывает getCurrentAccount() для получения id и токена. Преобразовывает Either<Failure, AccountEntity> в Either<Failure, None>
    //  с помощью оператора flatMap. Вызывает friendsRemote.addFriend(…) для отправления приглашения.
    //  Принимает String: email. Возвращает Either<Failure, None>.
    override fun addFriend(email: String): Either<Failure, None> {
        return accountCache.getCurrentAccount()
            .flatMap { friendsRemote.addFriend(email, it.id, it.token) }
    }
    //  вызывает getCurrentAccount() для получения идентификатора и токена. Преобразовывает Either<Failure, AccountEntity> в Either<Failure, None>
    //  с помощью оператора flatMap. Вызывает friendsRemote.deleteFriend(…) для удаления друга.
    //  Возвращает Either<Failure, None>.
    //  добавлен блок onNext, в котором выполняется удаление друга из бд.
    override fun deleteFriend(friendEntity: FriendEntity): Either<Failure, None> {
        return accountCache.getCurrentAccount()
            .flatMap { friendsRemote.deleteFriend(it.id, friendEntity.id, friendEntity.friendsId, it.token) }
            .onNext { friendsCache.removeFriendEntity(friendEntity.id) }
    }
}