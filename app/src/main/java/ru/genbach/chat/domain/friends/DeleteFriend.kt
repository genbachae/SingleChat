package ru.genbach.chat.domain.friends

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class DeleteFriend @Inject constructor(     //  Для удаления друга.
    private val friendsRepository: FriendsRepository    //  объект репозитория FriendsRepository.
) : UseCase<None, FriendEntity>() { //  Наследуется от: UseCase с параметризированными типами None и FriendEntity.
    //  обращается к репозиторию для удаления друга. Принимает FriendEntity. Возвращает Either<Failure, None>.
    override suspend fun run(params: FriendEntity) = friendsRepository.deleteFriend(params)
}