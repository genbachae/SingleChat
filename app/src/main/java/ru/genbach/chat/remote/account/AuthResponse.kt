package ru.genbach.chat.remote.account

import ru.genbach.chat.domain.account.AccountEntity
import ru.genbach.chat.remote.core.BaseResponse

class AuthResponse(     //  POJO-класс. Для хранения ответа сервера при авторизации.
    success: Int,
    message: String,
    val user: AccountEntity         //  содержит данные пользователя. Тип:
) : BaseResponse(success, message)