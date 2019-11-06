package ru.genbach.chat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.HandleOnce

abstract class BaseViewModel : ViewModel() {    //  Базовый класс. Для выделения поведения всех ViewModel.
    //  объект LiveData, содержащий ошибку(Failure). Обернут в класс HandleOnce для предотвращения повторных отрисовок одних и тех же ошибок.
    //  Тип: MutableLiveData<HandleOnce<Failure>>.
    var failureData: MutableLiveData<HandleOnce<Failure>> = MutableLiveData()
    //  хранит состояние загрузки данных. Нужен для управления отображением прогресс бара. Тип: LiveData<Boolean>.
    var progressData: MutableLiveData<Boolean> = MutableLiveData()
    //  сеттер, присваивающий ошибку. Обертывает в HandleOnce. Принимает Failure. Ничего не возвращает.
    protected fun handleFailure(failure: Failure) {
        this.failureData.value = HandleOnce(failure)
        updateProgress(false)
    }

    protected fun updateProgress(progress: Boolean) {   //  сеттер для
        this.progressData.value = progress
    }
}