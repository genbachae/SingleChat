package info.fandroid.chat.data.account

import info.fandroid.chat.domain.type.Either
import info.fandroid.chat.domain.type.None
import info.fandroid.chat.domain.type.exception.Failure

interface AccountRemote {
    fun register(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None>
}