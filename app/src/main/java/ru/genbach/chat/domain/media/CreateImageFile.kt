package ru.genbach.chat.domain.media

import android.net.Uri
import ru.genbach.chat.domain.interactor.UseCase
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class CreateImageFile @Inject constructor(          //  Для создания файла изображения.
    private val mediaRepository: MediaRepository    //  объект репозитория MediaRepository.
) : UseCase<Uri, None>() {  //  Наследуется от: UseCase с параметризированными типами Uri и None.
    //  делегирует репозиторию создания файла. Принимает None. Возвращает Either<Failure, Uri>.
    override suspend fun run(params: None) = mediaRepository.createImageFile()
}