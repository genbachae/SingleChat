package ru.genbach.chat.domain.media

import android.graphics.Bitmap
import android.net.Uri
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure

interface MediaRepository { //  Интерфейс репозитория. Для работы с файлами(создание, обработка).
    //  создает файл изображения. Используется для передачи в Intent камеры при выборе изображения. Ничего не принимает. Возвращает Either<Failure, Uri>.
    fun createImageFile(): Either<Failure, Uri>
    //  кодирует изображение в строку. Используется для отправки изображения на сервер. Принимает Bitmap. Возвращает Either<Failure, String>.
    fun encodeImageBitmap(bitmap: Bitmap?): Either<Failure, String>
    //  возвращает изображение в формате Bitmap используя путь Uri . Принимает Uri. Возвращает Either<Failure, Bitmap>.
    fun getPickedImage(uri: Uri?): Either<Failure, Bitmap>

/*    Пример:
    Пользователь меняет изображение профиля используя камеру.
    Для этого репозиторий создает файл, получает его в формате Bitmap и кодирует в строку для отправки на сервер.*/
}