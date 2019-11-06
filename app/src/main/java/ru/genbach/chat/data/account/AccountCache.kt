package ru.genbach.chat.data.account

import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Failure
//  Интерфейс, содержащий функции для взаимодействия с аккаунтом в локальной базе данных.
//  Для взаимодействия Data с Cache. При этом Data ничего не знает о Cache и его реализации, так как использует свой интерфейс.
//  Благодаря этому не нарушается правило Dependency Rule.
//  Пример: Репозиторий(AccountRepositoryImpl) выполняет метод кэша saveToken(…) используя интерфейс(AccountCache),
// реализация которого(AccountCacheImpl) находится в слое Cache.
interface AccountCache {
    //   возвращает токен из локальной базы данных. Ничего не принимает. Возвращает Either<Failure, String>.
    fun getToken(): Either<Failure, String>
    //  выполняет сохранение токена в локальную базу данных. Принимает cтроки: token. Возвращает Either<Failure, None>.
    fun saveToken(token: String): Either<Failure, None>

    fun logout(): Either<Failure, None>     //  выполняет выхода из аккаунта. Ничего не принимает. Возвращает Either<Failure, None>.

    fun getCurrentAccount(): Either<Failure, AccountEntity>     //  получает текущий аккаунт. Ничего не принимает. Возвращает Either<Failure, AccountEntity>.
    fun saveAccount(account: AccountEntity): Either<Failure, None>  //  сохраняет текущий аккаунт в бд. Принимает Возвращает Either<Failure, None>.

    fun checkAuth(): Either<Failure, Boolean>   //  проверяет авторизации пользователя. Ничего не принимает. Возвращает Either<Failure, Boolean>.
}