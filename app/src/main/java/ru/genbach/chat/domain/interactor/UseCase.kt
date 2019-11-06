package ru.genbach.chat.domain.interactor

import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
/* Абстрактный(обобщающий) параметризированный класс. Оперирует двумя параметризированными типами: Type(тип возвращаемого объекта с данными) и
Params(тип класса-оболочки, хранящей параметры для выполнения функции). Содержит: объекты контекста выполнения работы
(val backgoundContext, val foregroundContext), объект работы(val parentJob), абстрактную функцию выполнения(fun run(…)),
оператор вызова(operator fun invoke(…)), функцию отписки работы(fun unsubscribe(…)).
Для выполнения любых задач.
Пример:
Регистрация – задача, одна часть которой выполняется в фоновом потоке(API запрос), а другая – в UI потоке(вывод тоста о успехе или ошибке).
*/

/**
 * Абстрактный класс для варианта использования (Interactor с точки зрения чистой архитектуры).
  * Эта абстракция представляет собой исполнительный блок для разных вариантов использования (это означает, что любое использование
  * дело в заявке должно реализовывать данный договор).
  *
  * По соглашению каждая реализация [UseCase] будет выполнять свою работу в фоновом потоке
  * (сопрограмма kotlin) и опубликует результат в ветке пользовательского интерфейса.
 */

abstract class UseCase<out Type, in Params> {
    //  создает объект фонового контекста для выполнения работ.
    var backgroundContext: CoroutineContext = Dispatchers.IO
    //  создает объект UI контекста для выполнения работ.
    var foregroundContext: CoroutineContext = Dispatchers.Main
    //  создает объект, используемый для инициирования и отмены работы.
    private var parentJob: Job = Job()
    //  абстрактная функция, имплементируемая в дочерних классах. Выполняется в фоновом контексте.
    //  Принимает объект параметризированного типа Params. Возвращает Either<Failure, Type>, где Failure – ошибка, а Type – тип данных.
    abstract suspend fun run(params: Params): Either<Failure, Type>
    //  отменяет предыдущую(вызывает unsubscribe(…)) и инициирует новую работу. С помощью функций launch и withContext разбивает
    //  работу между потоками(корутин контекстами). Вызывает функцию run(…) в фоне. Вызывает функцию высшего порядка,
    //  переданную в параметрах(onResult: ((Either<Failure, Type>) -> Unit), в UI потоке. Принимает объект params параметризированного
    //  типа Params и ф-цию высшего порядка onResult(принимает Either<Failure, Type>, ничего не возвращает). Ничего не возвращает.
    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {
        unsubscribe()
        parentJob = Job()

        CoroutineScope(foregroundContext + parentJob).launch {
            val result = withContext(backgroundContext) {
                run(params)
            }

            onResult(result)
        }
    }

    fun unsubscribe() {     //  отменяет работу.
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
}
