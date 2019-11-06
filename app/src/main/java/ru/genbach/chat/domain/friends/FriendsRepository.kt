package ru.genbach.chat.domain.friends

import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None

interface FriendsRepository {   //  Интерфейс репозитория. Для взаимодействия с друзьями(добавление, удаление, принятие и отклонение приглашений).
    fun getFriends(needFetch: Boolean): Either<Failure, List<FriendEntity>>         //  получение списка друзей текущего пользователя. добавлен принимаемый параметр needFetch.
    fun getFriendRequests(needFetch: Boolean): Either<Failure, List<FriendEntity>>  //  получение списка входящих приглашений на добавление в друзья. добавлен принимаемый параметр needFetch.

    fun approveFriendRequest(friendEntity: FriendEntity): Either<Failure, None> //  принимает приглашение на добавление в друзья. Принимает FriendEntity. Возвращает Either<Failure, None>.
    fun cancelFriendRequest(friendEntity: FriendEntity): Either<Failure, None>  //  отклоняет приглашение на добавление в друзья. Принимает FriendEntity. Возвращает Either<Failure, None>.

    fun addFriend(email: String): Either<Failure, None> //  высылает приглашение на добавление в друзья. Принимает String: email. Возвращает Either<Failure, None>.
    fun deleteFriend(friendEntity: FriendEntity): Either<Failure, None> //  удаляет из друзей. Принимает Возвращает Either<Failure, None>.
}