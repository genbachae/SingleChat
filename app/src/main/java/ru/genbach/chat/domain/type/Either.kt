package ru.genbach.chat.domain.type

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Left] or [Right].
 * FP Convention dictates that [Left] is used for "failure"
 * and [Right] is used for "success".
 *
 * @see Left
 * @see Right
 */
//  Параметризированный класс-обертка. Содержит в себе: два типа данных(Left и Right); функции для их проверки(val isLeft(), val isRight()) и
//  обработки с помощью ф-ций высшего порядка(fun either(…)); операторы для трансформации(fun map(…), fun flatMap(…), fun onNext(…)).
//  Для передачи одного из двух возможных типов данных, неизвестного в момент компиляции, но известного в момент выполнения(кот Шредингера).
// Пример: Сетевой запрос может возвращать как данные, так и ошибку. Either компонует их, что помогает обработать их вместе с меньшим количеством кода.
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    //  может содержать в себе только левую часть Either.
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    //  может содержать в себе только правую часть Either.
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>    //  проверяет, является ли объект Either типом Right. Возвращает Boolean.
    val isLeft get() = this is Left<L>      //  проверяет, является ли объект Either типом Left. Возвращает Boolean.

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)
    //  выполняет одну из двух ф-ций высшего порядка, переданных в параметрах. Принимает две ф-ции высшего порядка
    //  (для параметризированных типов L(Left) и R(Right)): functionLeft(принимает L, возвращает Any) и
    //  functionRight(принимает R и возвращает Any). Возвращает Any.
    fun either(functionLeft: (L) -> Any, functionRight: (R) -> Any): Any =
        when (this) {
            is Left -> functionLeft(a)
            is Right -> functionRight(b)
        }
}

fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}
//  выполняет преобразование. если объект Either является типом L – возвращает его без изменений;
//  если объект Either является типом R – с помощью переданной ф-ции высшего порядка(fn: (R) -> Either<L, T>)
//  подменяет исходный Either другим Either, преобразовывая его содержимое (R).
//  Принимает ф-цию высшего порядка fn(принимает R, возвращает Eihter<L, T>). Возвращает Either<L, T>, где T – преобразованный R.
fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> {
    return when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }
}

//  выполнят преобразование. если объект Either является типом L – возвращает его без изменений;
//  если объект Either является типом R – возвращает преобразованный с помощью переданной ф-ции высшего порядка(fn: (R) -> (T) объект типа R.
//  Принимает ф-цию высшего порядка fn(принимает R, возвращает T). Возвращает Either<L, T>, где T – преобразованный R.
fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> {
    return this.flatMap(fn.compose(::right))
}
//  выполнят функцию. Если объект Either является типом L – возвращает его без изменений;
//  если объект Either является типом R – возвращает его без изменений. Выполняет ф-цию высшего порядка(fn: (R) -> Unit).
//  Принимает ф-цию высшего порядка fn(принимает R, ничего не возвращает). Возвращает Either<L, R>
fun <L, R> Either<L, R>.onNext(fn: (R) -> Unit): Either<L, R> {
    this.flatMap(fn.compose(::right))
    return this
}
