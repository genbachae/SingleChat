package ru.genbach.chat.data.media

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import ru.genbach.chat.domain.media.MediaRepository
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import java.io.File
import javax.inject.Singleton

@Singleton
//  Реализация интерфейса репозитория. Для работы с файлами(создание, обработка).
class MediaRepositoryImpl(val context: Context) : MediaRepository { //  Наследуется от: Интерфейса MediaRepository.
    //  делегирует создание файла изображения MediaHelper. Проверяет результат на null. Ничего не принимает. Возвращает Either<Failure, Uri>.
    override fun createImageFile(): Either<Failure, Uri> {
        val uri = MediaHelper.createImageFile(context)
        return if (uri == null) {
            Either.Left(Failure.FilePickError)
        } else {
            Either.Right(uri)
        }
    }
    //  проверяет Bitmap-изображение на null. Делегирует кодирование изображения в строку MediaHelper.
    //  Проверяет результат на null. Принимает Bitmap. Возвращает Either<Failure, String>.
    override fun encodeImageBitmap(bitmap: Bitmap?): Either<Failure, String> {
        if (bitmap == null) return Either.Left(Failure.FilePickError)

        val imageString = MediaHelper.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)

        return if (imageString.isEmpty()) {
            Either.Left(Failure.FilePickError)
        } else {
            Either.Right(imageString)
        }
    }
    //  проверяет Uri-путь на Делегирует получение и сохранение изображения MediaHelper.
    //  Проверяет результат на null. Принимает Uri. Возвращает Either<Failure, Bitmap>.
    override fun getPickedImage(uri: Uri?): Either<Failure, Bitmap> {
        if (uri == null) return Either.Left(Failure.FilePickError)

        val filePath = MediaHelper.getPath(context, uri)
        val image = MediaHelper.saveBitmapToFile(File(filePath))

        return if (image == null) {
            Either.Left(Failure.FilePickError)
        } else {
            Either.Right(image)
        }
    }
}