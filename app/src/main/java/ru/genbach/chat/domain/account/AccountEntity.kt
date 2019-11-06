package ru.genbach.chat.domain.account

import com.google.gson.annotations.SerializedName
//  Модельный класс, содержащий данные аккаунта. В будущем добавятся поля id, name, email и тд. Для передачи и хранения данных аккаунта.
/*Пример: После авторизации сервер возвращает данные аккаунта, которые нужно скомпоновать в одном месте.*/
data class AccountEntity(
    @SerializedName("user_id")
    var id: Long,                           //  id текущего пользователя. Тип: Long. 6й изменены на var (Mutable).
    var name: String,                       //  имя текущего пользователя. Тип: String. 6й изменены на var (Mutable).
    var email: String,                      //  email текущего пользователя. Тип: String. 6й изменены на var (Mutable).
    @SerializedName("token")
    var token: String,                      //  токен безопасности текущего пользователя. Тип: String. 6й изменены на var (Mutable).
    var status: String,                     //  статус текущего пользователя. Тип: String. 6й изменены на var (Mutable).
    @SerializedName("user_date")
    var userDate: Long,                     //  дата создания аккаунта текущего пользователя. Тип: Long. 6й изменены на var (Mutable).
    var image: String,                      //  фото текущего пользователя. Тип: String. 6й изменены на var (Mutable).
    var password: String                    //  переменная для хранения пароля.
)