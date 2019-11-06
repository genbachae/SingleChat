package ru.genbach.chat.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(     //  Класс-фабрика. Для создания ViewModel.
    //  Map, который содержит классы ViewModel (ключи) и провайдеры ViewModel (значения). Тип: Map<Class<ViewModel>, Provider<ViewModel>>
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory { //  Наследуется от: Android-интерфейса ViewModelProvider.Factory.
    //  реализация функции создания ViewModel. Получает провайдер ViewModel (creators[modelClass]) и
    //  возвращает с его помощью созданный объект нужного ViewModel (returncreator.get()).
    //  Если класс не подходит – пробрасывает Exception (throwIllegalArgumentException(…)).
    //  Присутствует параметризированный тип T, наследующийся от ViewModel. Принимает класс Class<T>. Возвращает T.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}