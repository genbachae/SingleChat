package ru.genbach.chat.domain.media

import android.graphics.Bitmap
import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

class EncodeImageBitmap @Inject constructor(    //  Для кодирования изображения в строку.
    private val mediaRepository: MediaRepository    //  объект репозитория MediaRepository.
) : UseCase<String, Bitmap?>() {    //  Наследуется от: UseCase с параметризированными типами String и Bitmap.
    //  делегирует репозиторию кодирование изображения. Принимает Bitmap. Возвращает Either<Failure, String>.
    override suspend fun run(params: Bitmap?) = mediaRepository.encodeImageBitmap(params)
}