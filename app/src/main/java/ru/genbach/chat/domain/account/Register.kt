package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

//  UseCase. Содержит: объект репозитория(val repository), класс для хранения данных(class Params),
//  имплементированную функцию выполнения(fun run(…)).
//  Для выполнения регистрации.
class Register @Inject constructor(
    private val repository: AccountRepository   //  объект репозитория(AccountRepository).
) : UseCase<None, Register.Params>() {  //  Наследуется от: UseCase с параметризированными типами None(для данных) и
// Register.Params(внутренний класс для передачи параметров).
    //  обращается к репозиторию для выполнения регистрации. Принимает Params. Возвращает Either<Failure, None>.
    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.register(params.email, params.name, params.password)
    }
    //  содержит в себе поля(email, name, password) для передачи параметров.
    data class Params(val email: String, val name: String, val password: String)
}

