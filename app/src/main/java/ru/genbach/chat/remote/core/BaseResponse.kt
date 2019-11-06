package ru.genbach.chat.remote.core
//  POJO-класс. Содержит поля: код ответа(val success) и текст ошибки(val message). Для хранения ответа сервера.
//  Пример: Сервер в каждом ответе отправляет код успешности и текст ошибки, если она произошла. Чтобы ее распарсить и нужен этот класс.
open class BaseResponse(
    val success: Int,       //  содержит код ответа сервера. Успех – 1, ошибка – 0.
    val message: String     //  содержит текст ошибки.
)