package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class CheckAuth @Inject constructor(    //  Для проверки авторизации пользователя.
    private val accountRepository: AccountRepository    //  объект репозитория AccountRepository.
) : UseCase<Boolean, None>() {  //  Наследуется от: UseCase с параметризированными типами Boolean и None.
    //  обращается к репозиторию для проверки сессии. Ничего не принимает. Возвращает Either<Failure, Boolean>.
    override suspend fun run(params: None): Either<Failure, Boolean> = accountRepository.checkAuth()
}