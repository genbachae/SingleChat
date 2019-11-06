package ru.genbach.chat.presentation.viewmodel

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import ru.genbach.chat.domain.media.CreateImageFile
import ru.genbach.chat.domain.media.EncodeImageBitmap
import ru.genbach.chat.domain.media.GetPickedImage
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class MediaViewModel @Inject constructor(   //  Класс ViewModel. Для взаимодействия с файлами.
    val createImageFileUseCase: CreateImageFile,        //  use case для создания файла изображения. Тип: CreateImageFile.
    val encodeImageBitmapUseCase: EncodeImageBitmap,    //  use case для кодирования изображения в строку. Тип: EncodeImageBitmap.
    val getPickedImageUseCase: GetPickedImage           //  use case для получения Bitmap-изображения. Тип: GetPickedImage.
) : BaseViewModel() {   //  Наследуется от: Базового класса BaseViewModel.

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 10001
        const val CAPTURE_IMAGE_REQUEST_CODE = 10002
    }

    class PickedImage(val bitmap: Bitmap, val string: String)

    var cameraFileCreatedData: MutableLiveData<Uri> = MutableLiveData()         //  хранит Uri-путь созданного файла. Тип: MutableLiveData<Uri>.
    private var imageBitmapData: MutableLiveData<Bitmap> = MutableLiveData()    //  хранит Bitmap-изображение. Тип: MutableLiveData<Bitmap>.
    var pickedImageData: MutableLiveData<PickedImage> = MutableLiveData()       //  хранит Тип: MutableLiveData<PickedImage>.

    fun createCameraFile() {            //  выполняет создание файла изображения. Ничего не принимает. Ничего не возвращает.
        createImageFileUseCase(None()) { it.either(::handleFailure, ::handleCameraFileCreated) }
    }

    private fun getPickedImage(uri: Uri?) { //  выполняет получение Bitmap-изображения. Принимает Uri. Ничего не возвращает.
        updateProgress(true)
        getPickedImageUseCase(uri) { it.either(::handleFailure, ::handleImageBitmap) }
    }

    private fun encodeImage(bitmap: Bitmap) {   //  выполняет кодирование изображения. Принимает Bitmap. Ничего не возвращает.
        encodeImageBitmapUseCase(bitmap) { it.either(::handleFailure, ::handleImageString) }
    }

    //  выполняет обработку данных от onActivityResult() при выборе источника изображения.
    //  В зависимости от значения requestCode, присваивает uri путь изображения из галереи(PICK_IMAGE_REQUEST_CODE) или
    //  камеры(CAPTURE_IMAGE_REQUEST_CODE). Вызывает получение Bitmap-изображения.
    //  Принимает Int: requestCode, resultCode, Intent. Ничего не возвращает.
    fun onPickImageResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val uri = when (requestCode) {
                PICK_IMAGE_REQUEST_CODE -> data?.data
                CAPTURE_IMAGE_REQUEST_CODE -> cameraFileCreatedData.value
                else -> null
            }

            getPickedImage(uri)
        }
    }

    private fun handleCameraFileCreated(uri: Uri) {     //  сеттер для
        this.cameraFileCreatedData.value = uri
    }

    private fun handleImageBitmap(bitmap: Bitmap) {     //  сеттер для
        this.imageBitmapData.value = bitmap

        encodeImage(bitmap)
    }

    private fun handleImageString(string: String) {     //  сеттер для
        this.pickedImageData.value = PickedImage(imageBitmapData.value!!, string)
        updateProgress(false)
    }

    override fun onCleared() {
        super.onCleared()
        createImageFileUseCase.unsubscribe()
        encodeImageBitmapUseCase.unsubscribe()
        getPickedImageUseCase.unsubscribe()
    }
}