package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class GetAccount @Inject constructor(   //  Для получения аккаунта текущего пользователя.
    private val accountRepository: AccountRepository    //  объект AccountRepository.
) : UseCase<AccountEntity, None>() {
    //  обращается к репозиторию для получения аккаунта. Принимает None. Возвращает Either<Failure, AccountEntity>.
    override suspend fun run(params: None) = accountRepository.getCurrentAccount()
}