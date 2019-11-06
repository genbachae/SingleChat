package ru.genbach.chat.ui.core.ext
//  Для содержания extension функций, связанных с жизненным циклом.
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.HandleOnce
//  установка слушателя для данных(LiveData) успешного выполнения задач. Присутствует параметризированные типы: T,
//  наследующийся от Any и L, наследующийся от LiveData с типом T. Принимает L и функцию body(Принимает T, ничего не возвращает).
//  Ничего не возвращает.
fun <T : Any, L : LiveData<T>> LifecycleOwner.onSuccess(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))
//  установка слушателя для данных(LiveData) неудачного выполнения задач(ошибки). Присутствует параметризированный тип L,
//  наследующийся от LiveData с типом HandleOnce<Failure>. Выполняется null-безопасный вызов let(…),
//  предотвращающий повторную обработку одних и тех же данных(отрисовку ошибки).
//  Принимает L и функцию body(Принимает Failure, ничего не возвращает). Ничего не возвращает.
fun <L : LiveData<HandleOnce<Failure>>> LifecycleOwner.onFailure(liveData: L, body: (Failure?) -> Unit) =
    liveData.observe(this, Observer {
        it.getContentIfNotHandled()?.let(body)
    })
