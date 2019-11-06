package ru.genbach.chat.domain.friends

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class ApproveFriendRequest @Inject constructor( //  Для принятия приглашения дружбы.
    private val friendsRepository: FriendsRepository    //  объект репозитория FriendsRepository.
) : UseCase<None, FriendEntity>() { //  Наследуется от: UseCase с параметризированными типами None и FriendEntity.
    //  обращается к репозиторию для принятия приглашения дружбы. Принимает FriendEntity. Возвращает Either<Failure, None>.
    override suspend fun run(params: FriendEntity) = friendsRepository.approveFriendRequest(params)
}