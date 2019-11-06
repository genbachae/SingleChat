package ru.genbach.chat.domain.type

/**
 * Base Class for handling errors/failures/exceptions.
 */
//  Класс-маркер. Содержит в себе возможные типы данных: Для передачи маркера об ошибке с дальнейшей ее обработкой.
sealed class Failure {

    object NetworkConnectionError : Failure()
    object ServerError : Failure()
    object AuthError : Failure()                        //  объект для обозначения ошибки авторизации(неправильный email/пароль).
    object TokenError : Failure()                       //  объект для обозначения ошибки о невалидном токене.

    object EmailAlreadyExistError : Failure()           //  3й: объект для обозначения ошибки при регистрации, связанной с тем, что email уже существует в БД.

    object AlreadyFriendError : Failure()               //  объект для обозначения ошибки при попытке добавить в друзья пользователя, уже являющегося другом.
    object AlreadyRequestedFriendError : Failure()      //  объект для обозначения ошибки повторного реквеста на добавление в друзья.
    object ContactNotFoundError : Failure()             //  объект для обозначения ошибки при добавлении в друзья несуществующего пользователя.AuthError

    object NoSavedAccountsError : Failure()             //  объект для обозначения ошибки получения текущего аккаунта, при его отсутствии.

    object FilePickError : Failure()                    //  объект для обозначения ошибки выбора файла.
}

/*
Пример:
API запросы подразумевают риск получения ошибок: сервер не доступен, нет подключения к сети. При их получении мы искусственно создаем объекты ошибок,
которые можно удобно обработать (Выводить разные тосты в зависимости от типа объекта).

Sealed(изолированные) классы.
Изолированные классы используются для отражения ограниченных иерархий классов, когда значение может иметь тип только из ограниченного набора,
 и никакой другой. Они являются, по сути, расширением enum-классов: набор значений enum типа также ограничен, но каждая enum-константа
 существует только в единственном экземпляре, в то время как наследник изолированного класса может иметь множество экземпляров,
 которые могут нести в себе какое-то состояние.
*/