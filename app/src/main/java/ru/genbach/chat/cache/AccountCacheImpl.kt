package ru.genbach.chat.cache

import ru.genbach.chat.data.account.AccountCache
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Failure
import javax.inject.Inject
//  Класс, содержащий функции взаимодействия с аккаунтом в бд. Содержит: объект для работы с SharedPrefsManager(val prefsManager),
//  функции которые выполняют сохранение(fun saveToken(…)) и восстановление(fun getToken()) токена.
//  Для взаимодействия с аккаунтом в бд.
//  Наследуется от: Интерфейса AccountCache, который находится в Data. Имплементирует его функции fun saveToken(…) и getToken().
class AccountCacheImpl @Inject constructor(private val prefsManager: SharedPrefsManager) : AccountCache {
    //  val prefsManager объект для взаимодействия с SharedPrefsManager.
    //  сохраняет токен в бд(prefsManager.saveToken(…)). Принимает строки: token. Возвращает Either<Failure, None>.
    override fun saveToken(token: String): Either<Failure, None> {
        return prefsManager.saveToken(token)
    }
    //  восстанавливает токен из бд(prefsManager.getToken()). Ничего не принимает. Возвращает Either<Failure, String>.
    override fun getToken(): Either<Failure, String> {
        return prefsManager.getToken()
    }
    //  удаляет данные аккаунта из бд. Ничего не принимает. Возвращает Either<Failure, None>.
    override fun logout(): Either<Failure, None> {
        return prefsManager.removeAccount()
    }
    //  получает текущий аккаунт из бд. Ничего не принимает. Возвращает Either<Failure, AccountEntity>.
    override fun getCurrentAccount(): Either<Failure, AccountEntity> {
        return prefsManager.getAccount()
    }
    //  сохраняет аккаунт в бд. Принимает Возвращает Either<Failure, None>.
    override fun saveAccount(account: AccountEntity): Either<Failure, None> {
        return prefsManager.saveAccount(account)
    }
    //  Имплементация метода интерфейса AccountCache. Делегирует проверку сессии PrefsManager. Ничего не принимает. Возвращает Either<Failure, Boolean>.
    override fun checkAuth(): Either<Failure, Boolean> {
        return prefsManager.containsAnyAccount()
    }
}