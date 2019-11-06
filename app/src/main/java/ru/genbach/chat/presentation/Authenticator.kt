package ru.genbach.chat.presentation

import ru.genbach.chat.domain.account.CheckAuth
import ru.genbach.chat.domain.type.None
import javax.inject.Inject
import javax.inject.Singleton
/*Пример:
При запуске приложения нужно проверить, авторизован ли пользователь. Authenticator проверяет наличие незавершенных сессий.*/
@Singleton
class Authenticator     //  Вспомогательный класс. Для проверки авторизации пользователя.
@Inject constructor(
    val checkAuth: CheckAuth        //  объект
) {
    //  проверяет наличие сохраненного аккаунта. Ничего не принимает. Возвращает Boolean
    //  добавлен входящий параметр – функция body(принимает Boolean, ничего не возвращает).
    //  Добавлено выполнение usecaseCheckAuth, в котором вызывается функция body (в случае ошибки в body передается false).
    fun userLoggedIn(body: (Boolean) -> Unit) {
        checkAuth(None()) {
            it.either({ body(false) }, { body(it) })
        }
    }
}