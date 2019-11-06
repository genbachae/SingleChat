package ru.genbach.chat.remote.friends

import ru.genbach.chat.data.friends.FriendsRemote
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.type.Either
import ru.genbach.chat.domain.type.Failure
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.remote.core.Request
import ru.genbach.chat.remote.service.ApiService
import javax.inject.Inject

class FriendsRemoteImpl @Inject constructor(    //  Реализация интерфейса. Для взаимодействия с друзьями в сети.
    private val request: Request,       //  объект для создания запросов.
    private val service: ApiService     //  объект для формирования API запросов.
) : FriendsRemote {
    //  выполняет запрос для получения списка друзей. Принимает Long: userId, String: token. Возвращает Either<Failure, List<FriendEntity>>.
    override fun getFriends(userId: Long, token: String): Either<Failure, List<FriendEntity>> {
        return request.make(service.getFriends(createGetFriendsMap(userId, token))) { it.friends }
    }
    //  выполняет запрос для получение списка входящих приглашений дружбы. Принимает Long: userId, String: token.
    //  Возвращает Either<Failure, List<FriendEntity>>.
    override fun getFriendRequests(userId: Long, token: String): Either<Failure, List<FriendEntity>> {
        return request.make(service.getFriendRequests(createGetFriendRequestsMap(userId, token))) { it.friendsRequests }
    }
    //  выполняет запрос для принятия приглашения дружбы.
    //  Принимает Long: userId, requestUserId, friendsId, String: token. Возвращает Either<Failure, None>.
    override fun approveFriendRequest(
        userId: Long,
        requestUserId: Long,
        friendsId: Long,
        token: String
    ): Either<Failure, None> {
        return request.make(service.approveFriendRequest(createApproveFriendRequestMap(userId, requestUserId, friendsId, token))) { None() }
    }
    //  выполняет запрос для отклонения приглашения дружбы. Принимает Long: userId, requestUserId, friendsId, String: token.
    //  Возвращает Either<Failure, None>.
    override fun cancelFriendRequest(
        userId: Long,
        requestUserId: Long,
        friendsId: Long,
        token: String
    ): Either<Failure, None> {
        return request.make(service.cancelFriendRequest(createCancelFriendRequestMap(userId, requestUserId, friendsId, token))) { None() }
    }
    //  выполняет запрос для отправления приглашения дружбы. Принимает Long: userId, String: email, token. Возвращает Either<Failure, None>.
    override fun addFriend(email: String, userId: Long, token: String): Either<Failure, None> {
        return request.make(service.addFriend(createAddFriendMap(email, userId, token))) { None() }
    }
    //  выполняет запрос для удаления из друзей. Принимает Long: userId, requestUserId, friendsId, String: token.
    //  Возвращает Either<Failure, None>.
    override fun deleteFriend(userId: Long, requestUserId: Long, friendsId: Long, token: String): Either<Failure, None> {
        return request.make(service.deleteFriend(createDeleteFriendMap(userId, requestUserId, friendsId, token))) { None() }
    }


    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createGetFriendsMap(userId: Long, token: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createGetFriendRequestsMap(userId: Long, token: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createApproveFriendRequestMap(
        userId: Long,
        requestUserId: Long,
        friendsId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_REQUEST_USER_ID, requestUserId.toString())
        map.put(ApiService.PARAM_FRIENDS_ID, friendsId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createCancelFriendRequestMap(
        userId: Long,
        requestUserId: Long,
        friendsId: Long,
        token: String
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_REQUEST_USER_ID, requestUserId.toString())
        map.put(ApiService.PARAM_FRIENDS_ID, friendsId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createAddFriendMap(email: String, userId: Long, token: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_REQUEST_USER_ID, userId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
    //  вспомогательная функция, преобразовывающая параметры в Map<String, String>.
    private fun createDeleteFriendMap(userId: Long, requestUserId: Long, friendsId: Long, token: String): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_USER_ID, userId.toString())
        map.put(ApiService.PARAM_REQUEST_USER_ID, requestUserId.toString())
        map.put(ApiService.PARAM_FRIENDS_ID, friendsId.toString())
        map.put(ApiService.PARAM_TOKEN, token)
        return map
    }
}