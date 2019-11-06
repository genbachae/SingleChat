package ru.genbach.chat.domain.type
/* Слушатели LiveData срабатывают при смене конфигурации. В таком случае все данные передаются в UI повторно.
Для предотвращения повторного отображения тоста об одной и той же ошибке при повороте устройства мы и используем этот класс.*/
open class HandleOnce<out T>(private val content: T) {  //  Класс-обертка. Присутствует параметризация T. Предотвращает повторное получение данных.
    //  content – контент класса-обертки.

    private var hasBeenHandled = false

    /**
     * Возвращает содержимое и снова запрещает его использование.
     */
    //  возвращает контент только один раз. В случае если контент уже был возвращен, возвращает null. Ничего не принимает. Возвращает
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}