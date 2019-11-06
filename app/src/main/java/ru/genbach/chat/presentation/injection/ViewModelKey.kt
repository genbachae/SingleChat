package ru.genbach.chat.presentation.injection

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)     //  Класс-аннотация. Для обозначения ViewModel-классов при их биндинге.
        //  val value – поле в котором класс Тип: KClass<ViewModel>.
