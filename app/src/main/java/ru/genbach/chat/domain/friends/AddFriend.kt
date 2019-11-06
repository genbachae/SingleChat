package ru.genbach.chat.domain.friends

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class AddFriend @Inject constructor(    //  Для отправления приглашения дружбы.
    private val friendsRepository: FriendsRepository    //  объект репозитория FriendsRepository.
) : UseCase<None, AddFriend.Params>() { //  Наследуется от: UseCase с параметризированными типами None и AddFriend.Params.
    //  обращается к репозиторию для отправления приглашения дружбы. Принимает AddFriend.Params. Возвращает Either<Failure, None>.
    override suspend fun run(params: Params) = friendsRepository.addFriend(params.email)

    data class Params(val email: String)    //  содержит в себе поля(email) для передачи параметров.
}