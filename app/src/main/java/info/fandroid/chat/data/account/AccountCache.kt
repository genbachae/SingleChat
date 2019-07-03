package info.fandroid.chat.data.account

import info.fandroid.chat.domain.type.Either
import info.fandroid.chat.domain.type.None
import info.fandroid.chat.domain.type.exception.Failure

interface AccountCache {
    fun getToken(): Either<Failure, String>
    fun saveToken(token: String): Either<Failure, None>
}