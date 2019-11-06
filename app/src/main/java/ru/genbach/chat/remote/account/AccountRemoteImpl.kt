package ru.genbach.chat.remote.account

import ru.genbach.chat.data.account.AccountRemote
import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.remote.core.Request
import ru.genbach.chat.remote.service.ApiService
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
//  Класс, содержащий функции взаимодействия с аккаунтом в сети. Содержит: объект для создания сетевых запросов(val request),
//  API сервис(val service), функции для выполнения регистрации(fun register(…)) и map’а параметров запроса(fun createRegisterMap(…)).
//  Для взаимодействия с аккаунтом в сети.

class AccountRemoteImpl @Inject constructor(
    private val request: Request,   //  объект для создания запросов.
    private val service: ApiService //  объект для формирования API запросов.
) : AccountRemote { //  Интерфейса AccountRemote, который находится в Data. Имплементирует его функцию fun register(…).
    //  выполняет регистрацию. Делегирует создание запроса(request.make(…)). Преобразовывает ответ в None ({ None() }).
    //  Принимает строки: email, name, password, token; Long: userDate. Возвращает Either<Failure, None>.
    override fun register(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None> {
        return request.make(service.register(createRegisterMap(email, name, password, token, userDate))) { None() }
    }
    //  выполняет авторизацию. Принимает String: email, password, token. Возвращает Either<Failure, AccountEntity>.
    override fun login(email: String, password: String, token: String): Either<Failure, AccountEntity> {
        return request.make(service.login(createLoginMap(email, password, token))) { it.user }
    }
    //  выполняет обновление токена. Принимает Long: userId; String: token, oldToken. Возвращает Either<Failure, None>.
    override fun updateToken(userId: Long, token: String, oldToken: String): Either<Failure, None> {
        return request.make(service.updateToken(createUpdateTokenMap(userId, token, oldToken))) { None() }
    }
    //  выполняет изменение данных пользователя на сервере.
    //  Принимает Long: userId; String: email, name, password, status, token, image. Возвращает Either<Failure, AccountEntity>.
    override fun editUser(
        userId: Long,
        email: String,
        name: String,
        password: String,
        status: String,
        token: String,
        image: String
    ): Either<Failure, AccountEntity> {
        return request.make(service.editUser(createUserEditMap(userId, email, name,
            password, status, token, image))) { it.user }
    }

    //  создает map параметров. С помощью map.put(…) добавляет параметры в map. Принимает строки: email, name, password, token; Long: userDate.
    //  Возвращает Map<String, String>.
    private fun createRegisterMap(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_NAME, name)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_USER_DATE, userDate.toString())
        return map
    }
    //  вспомогательная функция, создающая Map для авторизации. Принимает String: email, password, token. Возвращает Map<String, String>.
    private fun createLoginMap(email: String, password: String, token: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, создающая Map для обновления токена. Принимает Long: userId; String: token, oldToken. Возвращает Map<String, String>.
    private fun createUpdateTokenMap(userId: Long, token: String, oldToken: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_OLD_TOKEN, oldToken)
        return map
    }
    //  вспомогательная функция, создающая Map для измерения данных пользователя.
    //  Принимает Long: userId; String: email, name, password, status, token, image. Возвращает Map<String, String>.
    private fun createUserEditMap(
        id: Long,
        email: String,
        name: String,
        password: String,
        status: String,
        token: String,
        image: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, id.toString())
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_NAME, name)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_STATUS, status)
        map.put(ApiService.PARAM_TOKEN, token)
        if (image.startsWith("../")) {
            map.put(ApiService.PARAM_IMAGE_UPLOADED, image)
        } else {
            map.put(ApiService.PARAM_IMAGE_NEW, image)
            map.put(ApiService.PARAM_IMAGE_NEW_NAME, "user_${id}_${Date().time}_photo")
        }
        return map
    }
}