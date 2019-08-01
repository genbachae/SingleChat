package info.fandroid.chat.domain.friends

import info.fandroid.chat.domain.type.Either
import info.fandroid.chat.domain.type.Failure
import info.fandroid.chat.domain.type.None

interface FriendsRepository {
    fun getFriends(): Either<Failure, List<FriendEntity>>
    fun getFriendRequests(): Either<Failure, List<FriendEntity>>

    fun approveFriendRequest(friendEntity: FriendEntity): Either<Failure, None>
    fun cancelFriendRequest(friendEntity: FriendEntity): Either<Failure, None>

    fun addFriend(email: String): Either<Failure, None>
    fun deleteFriend(friendEntity: FriendEntity): Either<Failure, None>
}