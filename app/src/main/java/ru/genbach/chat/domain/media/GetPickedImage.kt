package ru.genbach.chat.domain.media

import android.graphics.Bitmap
import android.net.Uri
import ru.genbach.chat.domain.interactor.UseCase
import javax.inject.Inject

class GetPickedImage @Inject constructor(       //  Для получения изображения Bitmap.
    private val mediaRepository: MediaRepository    //  объект репозитория MediaRepository.
) : UseCase<Bitmap, Uri?>() {   //  Наследуется от: UseCase с параметризированными типами Bitmap и Uri.
    //  делегирует репозиторию получение изображения Bitmap. Принимает Uri. Возвращает Either<Failure, Bitmap>.
    override suspend fun run(params: Uri?) = mediaRepository.getPickedImage(params)
}