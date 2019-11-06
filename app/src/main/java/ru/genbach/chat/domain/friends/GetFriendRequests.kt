package ru.genbach.chat.domain.friends

import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

class GetFriendRequests @Inject constructor(    //  Для получения списка входящих приглашения дружбы.
    private val friendsRepository: FriendsRepository    //  объект репозитория FriendsRepository.
) : UseCase<List<FriendEntity>, Boolean>() {    //  Наследуется от: UseCase с параметризированными типами List<FriendEntity> и None.
    //  функция принимает параметр needFetch, которая имеет Boolean значение: выполнить загрузку с сервера(true) или из базы данных(false).
    override suspend fun run(needFetch: Boolean) = friendsRepository.getFriendRequests(needFetch)
}