package info.fandroid.chat.domain.type

/**
 * Base Class for handling errors/failures/exceptions.
 */

sealed class Failure {
    object NetworkConnectionError : Failure()
    object ServerError : Failure()
    object AuthError : Failure()
    object TokenError : Failure()

    object EmailAlreadyExistError : Failure()

    object NoSavedAccountsError : Failure()
}