package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class Logout @Inject constructor(   //  Для выполнения выхода из аккаунта.
    private val accountRepository: AccountRepository
) : UseCase<None, None>() {
    //  обращается к репозиторию для выполнения выхода из аккаунта. Принимает None. Возвращает Either<Failure, None>.
    override suspend fun run(params: None): Either<Failure, None> = accountRepository.logout()
}