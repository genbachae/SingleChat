package ru.genbach.chat.remote.friends

import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.remote.core.BaseResponse

class GetFriendsResponse (  //  POJO-класс. Для хранения ответа сервера при получении списка друзей.
    success: Int,
    message: String,
    val friends: List<FriendEntity> //  содержит список друзей. Тип: List<FriendEntity>.
) : BaseResponse(success, message)  //  Наследуется от: BaseResponse.