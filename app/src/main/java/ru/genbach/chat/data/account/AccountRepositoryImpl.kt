package ru.genbach.chat.data.account

import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.account.AccountRepository
import ru.genbach.chat.domain.type.*
import java.util.*
//  Класс, содержащий функции взаимодействия с аккаунтом. При этом решает откуда брать данные: из локальной базы или из сети.
//  Содержит: объекты для работы с бд(val accountCache) и  сервером(val accountRemote), функции которые выполняют регистрацию
//  пользователя(fun register(…)) и обновление токена(fun updateAcountToken(…)).
//  Для взаимодействия с аккаунтом в бд и сети.

class AccountRepositoryImpl(
    private val accountRemote: AccountRemote,   //  объект для взаимодействия с аккаунтом на сервере(AccountRemote).
    private val accountCache: AccountCache      //  объект для взаимодействия с аккаунтом в бд(AccountCache).
) : AccountRepository { //  Интерфейса AccountRepository, который находится в Domain.
// Имплементирует его функции fun register(…) и updateAccountToken(…).

    override fun login(email: String, password: String): Either<Failure, AccountEntity> {
        return accountCache.getToken().flatMap {
            accountRemote.login(email, password, it)
        }.onNext {                                  // в блоке onNext присваивается пароль.
            it.password = password
            accountCache.saveAccount(it)
        }
    }

    override fun logout(): Either<Failure, None> {
        return accountCache.logout()
    }
    //  выполняет регистрацию. Запрашивает токен у бд(accountCache.getToken()) и при помощи оператора flatMap выполняет
    //  регистрацию(accountRemote.register(…)), тем самым подменяя Either с токеном(String) на Either с данными о регистрации(None).
    //  Принимает строки: email, name, password. Возвращает Either<Failure, None>.
    override fun register(email: String, name: String, password: String): Either<Failure, None> {
        return accountCache.getToken().flatMap {
            accountRemote.register(email, name, password, it, Calendar.getInstance().timeInMillis)
        }
    }

    override fun forgetPassword(email: String): Either<Failure, None> {
        throw UnsupportedOperationException("Password recovery is not supported")
    }


    override fun getCurrentAccount(): Either<Failure, AccountEntity> {
        return accountCache.getCurrentAccount()
    }

    //  выполняет обновление токена. Сохраняет токен в бд(accountCache.saveToken(…)). Принимает строки: token. Возвращает Either<Failure, None>.
    override fun updateAccountToken(token: String): Either<Failure, None> {
        accountCache.saveToken(token)

        return accountCache.getCurrentAccount()
            .flatMap { accountRemote.updateToken(it.id, token, it.token) }
    }

    override fun updateAccountLastSeen(): Either<Failure, None> {
        throw UnsupportedOperationException("Updating last seen is not supported")
    }


    override fun editAccount(entity: AccountEntity): Either<Failure, AccountEntity> {   //  меняет данные аккаунта в сети и бд.
        return accountCache.getCurrentAccount().flatMap {
            accountRemote.editUser(entity.id, entity.email, entity.name, entity.password,
                entity.status, it.token, entity.image)
        }.onNext {
            entity.image = it.image
            accountCache.saveAccount(entity)
        }
    }
    //  8й имплементация метода интерфейса AccountRepository. Делегирует проверку сессии AccountCache.
    //  Ничего не принимает. Возвращает Either<Failure, Boolean>.
    override fun checkAuth(): Either<Failure, Boolean> {
        return accountCache.checkAuth()
    }
}