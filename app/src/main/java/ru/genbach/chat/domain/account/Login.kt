package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

class Login @Inject constructor(    //  Для авторизации.
    private val accountRepository: AccountRepository    //  объект AccountRepository.
) : UseCase<AccountEntity, Login.Params>() {
    //  обращается к репозиторию для выполнения авторизации. Принимает Params. Возвращает Either<Failure, AccountEntity>.
    override suspend fun run(params: Params) = accountRepository.login(params.email, params.password)
    //  содержит в себе поля(email, password) для передачи параметров.
    data class Params(val email: String, val password: String)
}