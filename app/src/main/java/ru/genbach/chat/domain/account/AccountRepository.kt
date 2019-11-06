package ru.genbach.chat.domain.account

import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
/*
Интерфейс, содержащий функции для взаимодействия с аккаунтом.
Для взаимодействия Domain с Data. При этом Domain ничего не знает о Data и ее реализации, так как использует свой интерфейс.
Благодаря этому не нарушается Dependency Rule. Это правило говорит нам, что внутренние слои не должны зависеть от внешних.
То есть наша бизнес-логика и логика приложения не должны зависеть от презентеров, UI, баз данных и т.п.

Пример:
UseCase регистрации выполняет метод репозитория register используя интерфейс(AccountRepository),
реализация которого(AccountRepositoryImpl) находится в слое Data.
*/

interface AccountRepository {   //  Для перемещения логики проверки активности сессии (пользователь авторизован) в Репозиторий.
    //  получает токен (accountCache.getToken(…)). Использует полученный токен для авторизации (accountRemote.login(…)).
    //  Сохраняет полученный аккаунт в бд (accountCache.saveAccount(…)).
    fun login(email: String, password: String): Either<Failure, AccountEntity>
    fun logout(): Either<Failure, None>     //  выполняет выход из аккаунта(accountCache.logout()).
    //  выполняет регистрацию. Принимает cтроки: email, name, password. Возвращает Either<Failure, None>.
    fun register(email: String, name: String, password: String): Either<Failure, None>
    fun forgetPassword(email: String): Either<Failure, None>

    fun getCurrentAccount(): Either<Failure, AccountEntity>         //  получает текущий аккаунт (accountCache.getCurrentAccount()).
    //  получает текущий аккаунт (accountCache.getCurrentAccount()). Использует полученный аккаунт для обновления токена
    //  на сервере (accountRemote.updateToken(…)). Сохраняет токен в бд (accountCache.saveToken(…)).
    fun updateAccountToken(token: String): Either<Failure, None>
    fun updateAccountLastSeen(): Either<Failure, None>

    fun editAccount(entity: AccountEntity): Either<Failure, AccountEntity>

    fun checkAuth(): Either<Failure, Boolean>   //  проверяет авторизации пользователя. Ничего не принимает. Возвращает Either<Failure, Boolean>.
}
//  УДАЛЁН: fun updateToken(…) выполняет обновление токена. Принимает строку: token. Возвращает Either<Failure, None>.