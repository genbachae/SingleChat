package info.fandroid.chat.data.account

import info.fandroid.chat.domain.account.AccountEntity
import info.fandroid.chat.domain.type.Either
import info.fandroid.chat.domain.type.None
import info.fandroid.chat.domain.type.Failure

interface AccountRemote {
    fun register(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None>

    fun login(email: String, password: String, token: String): Either<Failure, AccountEntity>

    fun updateToken(userId: Long, token: String, oldToken: String): Either<Failure, None>
}