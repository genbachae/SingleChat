package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

//  UseCase. Содержит: объект репозитория(val repository), класс для хранения данных(class Params), имплементированную функцию выполнения(fun run(…)).
//  Для выполнения обновления токена.
//  Пример: Firebase  прислал новый токен, который нужно сохранить в локальной базе данных и обновить на сервере.
// Токен Firebase будет использоваться в дальнейшем для аутентификации пользователя.
class UpdateToken @Inject constructor(
    private val accountRepository: AccountRepository    //  объект репозитория(AccountRepository).
) : UseCase<None, UpdateToken.Params>() {   // Наследуется от: UseCase с параметризированными типами None(для данных) и
// UpdateToken.Params(внутренний класс для передачи параметров).
    //  обращается к репозиторию для выполнения обновления токена. Принимает Params. Возвращает Either<Failure, None>.
    override suspend fun run(params: Params) = accountRepository.updateAccountToken(params.token)

    data class Params(val token: String)    //  содержит в себе поля(token) для передачи параметров.
}