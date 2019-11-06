package ru.genbach.chat.data.account

import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Failure

//  Интерфейс, содержащий функции для взаимодействия с аккаунтом на сервере.
//  Для взаимодействия Data с Remote. При этом Data ничего не знает о Remote и его реализации,
//  так как использует свой интерфейс. Благодаря этому не нарушается Dependency Rule.
//  Пример: Репозиторий(AccountRepositoryImpl) выполняет метод кэша register используя интерфейс(AccountRemote),
// реализация которого(AccountRemoteImpl) находится в слое Remote.

interface AccountRemote {
    fun register(   //  выполняет регистрацию. Принимает cтроки: email, name, password, token, userDate. Возвращает Either<Failure, None>.
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None>

    //  выполняет авторизацию. Принимает String: email, password, token. Возвращает Either<Failure, AccountEntity>.
    fun login(email: String, password: String, token: String): Either<Failure, AccountEntity>
    //  выполняет обновление токена на сервере. Принимает: Long: userId; String: token, oldToken. Возвращает Either<Failure, None>.
    fun updateToken(userId: Long, token: String, oldToken: String): Either<Failure, None>
    //  меняет данные пользоватаеля на сервере.
    //  Принимает Long: userId; String: email, name, password, status, token, image. Возвращает: Either<Failure, AccountEntity>.
    fun editUser(
        userId: Long,
        email: String,
        name: String,
        password: String,
        status: String,
        token: String,
        image: String
    ): Either<Failure, AccountEntity>
}