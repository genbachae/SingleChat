package ru.genbach.chat.remote.friends

import com.google.gson.annotations.SerializedName
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.remote.core.BaseResponse

class GetFriendRequestsResponse (   //  POJO-класс. Для хранения ответа сервера при получении списка приглашений дружбы.
    success: Int,
    message: String,
    @SerializedName("friend_requests")
    val friendsRequests: List<FriendEntity>     //  содержит список приглашений дружбы. Тип: List<FriendEntity>.
) : BaseResponse(success, message)