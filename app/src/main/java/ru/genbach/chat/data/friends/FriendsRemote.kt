package ru.genbach.chat.data.friends

import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None

interface FriendsRemote {   //  Содержит функции для взаимодействия с друзьями на сервере.
    //  получение списка друзей текущего пользователя. Принимает Long: userId, String: token. Возвращает Either<Failure, List<FriendEntity>>.
    fun getFriends(userId: Long, token: String): Either<Failure, List<FriendEntity>>
    //  получение списка входящих приглашений на добавление в друзья. Принимает Long: userId, String: token.
    //  Возвращает Either<Failure, List<FriendEntity>>.
    fun getFriendRequests(userId: Long, token: String): Either<Failure, List<FriendEntity>>
    //  принимает приглашение на добавление в друзья. Принимает Long: userId, requestUserId, friendsId, String: token.
    //  Возвращает Either<Failure, None>.
    fun approveFriendRequest(userId: Long, requestUserId: Long, friendsId: Long, token: String): Either<Failure, None>
    //  отклоняет приглашение на добавление в друзья. Принимает Long: userId, requestUserId, friendsId, String: token.
    //  Возвращает Either<Failure, None>.
    fun cancelFriendRequest(userId: Long, requestUserId: Long, friendsId: Long, token: String): Either<Failure, None>
    //  высылает приглашение на добавление в друзья. Принимает Long: userId, String: email, token. Возвращает Either<Failure, None>.
    fun addFriend(email: String, userId: Long, token: String): Either<Failure, None>
    //  удаляет из друзей. Принимает Long: userId, requestUserId, friendsId, String: token. Возвращает Either<Failure, None>.
    fun deleteFriend(userId: Long, requestUserId: Long, friendsId: Long, token: String): Either<Failure, None>
}