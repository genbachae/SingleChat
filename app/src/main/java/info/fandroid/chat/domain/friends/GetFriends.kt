package info.fandroid.chat.domain.friends

import info.fandroid.chat.domain.interactor.UseCase
import info.fandroid.chat.domain.type.None
import javax.inject.Inject

class GetFriends @Inject constructor(
    private val friendsRepository: FriendsRepository
) : UseCase<List<FriendEntity>, None>() {

    override suspend fun run(params: None) = friendsRepository.getFriends()
}