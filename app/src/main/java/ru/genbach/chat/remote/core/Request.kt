package ru.genbach.chat.remote.core

import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//  Класс, выполняющий сетевые запросы. Содержит: объект для проверки подключения (val networkHandler),
//  функции для выполнения запроса(fun execute(…)) и проверки ответа(extension fun Response.isSucceed()).
//  Для выполнения сетевых запросов и проверки ответа сервера.

class Request @Inject constructor(private val networkHandler: NetworkHandler) { //  val networkHandler объект для проверки сети.
    //  вспомогательная функция для проверки сети и вызова fun execute(…).
    fun <T : BaseResponse, R> make(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return when (networkHandler.isConnected) {
            true -> execute(call, transform)
            false, null -> Either.Left(Failure.NetworkConnectionError)
        }
    }

    //  выполняет сетевой запрос с помощью переданного в параметрах call (call.execute()). В блоке catch формирует маркеры ошибок
    //  для дальнейшей обработки(Either.Left(Failure.ServerError)). Функция имеет параметризированные типы: T(наследуемый от BaseResponse) и R.
    //  Принимает Call и функцию высшего порядка для трансформации transform(принимает T, возвращает R). Возвращает Either<Failure, R>.
    private fun <T : BaseResponse, R> execute(
        call: Call<T>,
        transform: (T) -> R
    ): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSucceed()) {
                true -> Either.Right(transform((response.body()!!)))
                false -> Either.Left(response.parseError())
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }
}

//  extension ф-ция, которая проверяет ответ от сервера. Ничего не принимает. Возвращает Boolean.
fun <T : BaseResponse> Response<T>.isSucceed(): Boolean {
    return isSuccessful && body() != null && (body() as BaseResponse).success == 1
}
//  extension функция, которая парсит ответ сервера и проверяет наличие в нем конкретных ошибок.
//  Если ответ содержит “email already exist” возвращает EmailAlreadyExist. В остальных случаях возвращает ServerError.
//  Ничего не принимает. Возвращает Failure.

//  распознавание ошибок: неправильный email/пароль, невалидный токен.
fun <T : BaseResponse> Response<T>.parseError(): Failure {          //  добавлен case для ошибки
    val message = (body() as BaseResponse).message
    return when (message) {
        "there is a user has this email",
        "email already exists" -> Failure.EmailAlreadyExistError
        "error in email or password" -> Failure.AuthError
        "Token is invalid" -> Failure.TokenError
        "this contact is already in your friends list" -> Failure.AlreadyFriendError        //  Урок 5
        "already found in your friend requests",
        "you requested adding this friend before" -> Failure.AlreadyRequestedFriendError    //  Урок 5
        "No Contact has this email" -> Failure.ContactNotFoundError                         //  Урок 5
        else -> Failure.ServerError
    }
}

