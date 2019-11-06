package ru.genbach.chat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import ru.genbach.chat.domain.friends.*
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class FriendsViewModel @Inject constructor(     //  Класс ViewModel. Для взаимодействия с друзьями.
    val getFriendsUseCase: GetFriends,                      //  usecase для получения списка друзей. Тип: GetFriends.
    val deleteFriendUseCase: DeleteFriend,                  //  use case для удаления друга. Тип:
    val addFriendUseCase: AddFriend,                        //  use case для добавления друга. Тип: AddFriend.
    val getFriendRequestsUseCase: GetFriendRequests,        //  usecase для получения списка приглашений дружбы. Тип: GetFriendRequests.
    val approveFriendRequestUseCase: ApproveFriendRequest,  //  use case для подтверждения дружбы. Тип:
    val cancelFriendRequestUseCase: CancelFriendRequest     //  use case для отклонения дружбы. Тип:
) : BaseViewModel() {   //  Наследуется от: Базового класса BaseViewModel.
    //  хранит список друзей. Тип: MutableLiveData<List<FriendEntity>>.
    var friendsData: MutableLiveData<List<FriendEntity>> = MutableLiveData()
    //  хранит список приглашений дружбы. Тип: MutableLiveData<List<FriendEntity>>.
    var friendRequestsData: MutableLiveData<List<FriendEntity>> = MutableLiveData()
    var deleteFriendData: MutableLiveData<None> = MutableLiveData()     //  хранит состояние Тип: MutableLiveData<None>.
    var addFriendData: MutableLiveData<None> = MutableLiveData()        //  хранит состояние Тип: MutableLiveData<None>.
    var approveFriendData: MutableLiveData<None> = MutableLiveData()    //  хранит состояние Тип: MutableLiveData<None>.
    var cancelFriendData: MutableLiveData<None> = MutableLiveData()     //  хранит состояние Тип: MutableLiveData<None>.

    //  выполняет получение списка друзей. Ничего не возвращает.
    fun getFriends(needFetch: Boolean = false) {    //  добавлен принимаемый параметр needFetch, который передается в usecase и handleFriends.
        getFriendsUseCase(needFetch) { it.either(::handleFailure) { handleFriends(it, !needFetch) } }
    }
    //  выполняет получение списка приглашений дружбы. Ничего не возвращает.
    fun getFriendRequests(needFetch: Boolean = false) {     //  добавлен принимаемый параметр needFetch, который передается в usecase и handleFriendRequests.
        getFriendRequestsUseCase(needFetch) { it.either(::handleFailure) { handleFriendRequests(it, !needFetch) } }
    }
    //  удаляет из друзей. Принимает FriendEntity. Ничего не возвращает.
    fun deleteFriend(friendEntity: FriendEntity) {
        deleteFriendUseCase(friendEntity) { it.either(::handleFailure, ::handleDeleteFriend) }
    }
    //  высылает приглашение дружбы. Принимает String: email. Ничего не возвращает.
    fun addFriend(email: String) {
        addFriendUseCase(AddFriend.Params(email)) { it.either(::handleFailure, ::handleAddFriend) }
    }
    //  принимает приглашение дружбы. Принимает FriendEntity. Ничего не возвращает.
    fun approveFriend(friendEntity: FriendEntity) {
        approveFriendRequestUseCase(friendEntity) { it.either(::handleFailure, ::handleApproveFriend) }
    }
    //  отклоняет приглашение дружбы. Принимает FriendEntity. Ничего не возвращает.
    fun cancelFriend(friendEntity: FriendEntity) {
        cancelFriendRequestUseCase(friendEntity) { it.either(::handleFailure, ::handleCancelFriend) }
    }

    //  добавлен принимаемый параметр При его значении true выполняется загрузка списка друзей из сети,
    //  что обновляет первоначально загруженные данных из бд. Таким образом пользователю отображаются сохраненные данные,
    //  которые актуализируются при наличии интернета.
    private fun handleFriends(friends: List<FriendEntity>, fromCache: Boolean) {
        friendsData.value = friends
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getFriends(true)
        }
    }
    //  добавлен принимаемый параметр При его значении true выполняется загрузка списка друзей из сети,
    //  что обновляет первоначально загруженные данных из бд. Таким образом пользователю отображаются сохраненные данные,
    //  которые актуализируются при наличии интернета.
    private fun handleFriendRequests(friends: List<FriendEntity>, fromCache: Boolean) {
        friendRequestsData.value = friends
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getFriendRequests(true)
        }
    }

    private fun handleDeleteFriend(none: None?) {
        deleteFriendData.value = none
    }

    private fun handleAddFriend(none: None?) {
        addFriendData.value = none
    }

    private fun handleApproveFriend(none: None?) {
        approveFriendData.value = none
    }

    private fun handleCancelFriend(none: None?) {
        cancelFriendData.value = none
    }


    override fun onCleared() {
        super.onCleared()
        getFriendsUseCase.unsubscribe()
        getFriendRequestsUseCase.unsubscribe()
        deleteFriendUseCase.unsubscribe()
        addFriendUseCase.unsubscribe()
        approveFriendRequestUseCase.unsubscribe()
        cancelFriendRequestUseCase.unsubscribe()
    }
}