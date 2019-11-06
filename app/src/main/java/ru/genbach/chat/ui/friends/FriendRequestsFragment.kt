package ru.genbach.chat.ui.friends

import android.os.Bundle
import android.view.View
import ru.genbach.chat.R
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.presentation.viewmodel.FriendsViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseListFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess

class FriendRequestsFragment : BaseListFragment() {     //  Фрагмент-список. Для отображения списка приглашений в друзья.
    override val viewAdapter = FriendRequestsAdapter()      //  объект

    override val layoutId = R.layout.inner_fragment_list    //  переопределение id макета фрагмента. Тип:

    lateinit var friendsViewModel: FriendsViewModel         //  view model для взаимодействия с друзьями. Тип:



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)               //  инъекция компонентов.
    }
    //  инициализация friendsViewModel, установка слушателей для friendsRequestsData, approveFriendData, cancelFriendData, failureData.
    //  Присвоение слушателей нажатий на кнопки элемента списка.
    //  onResume(…) – вызов функции получения списка приглашений.
    //  вызов метода getFriendRequests(…) перенесен из onResume(…) в onViewCreated(…).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   // 6й в слушатель нажатия добавлен вызов метода showUser.
        super.onViewCreated(view, savedInstanceState)

        base {
            friendsViewModel = viewModel {
                onSuccess(friendRequestsData, ::handleFriendRequests)
                onSuccess(approveFriendData, ::handleFriendRequestAction)
                onSuccess(cancelFriendData, ::handleFriendRequestAction)
                onFailure(failureData, ::handleFailure)
            }
        }


        setOnItemClickListener { item, v ->
            (item as? FriendEntity)?.let {
                when (v.id) {
                    R.id.btnApprove -> {
                        showProgress()
                        friendsViewModel.approveFriend(it)
                    }
                    R.id.btnCancel -> {
                        showProgress()
                        friendsViewModel.cancelFriend(it)
                    }
                    else -> {
                        activity?.let { act ->
                            navigator.showUser(act, it)
                        }
                    }
                }
            }
        }

        friendsViewModel.getFriendRequests()
    }

    //  обработка получения списка. Очищение адаптера, добавление в него элементов.
    //  Принимает List<FriendEntity>. Ничего не возвращает.
    private fun handleFriendRequests(requests: List<FriendEntity>?) {
        hideProgress()
        if (requests != null) {
            viewAdapter.clear()
            viewAdapter.add(requests)
            viewAdapter.notifyDataSetChanged()
        }
    }
    //  обработка принятия и отклонения приглашения. Повторный вызов получения списка.
    //  Принимает Ничего не возвращает.
    private fun handleFriendRequestAction(none: None?) {
        hideProgress()
        friendsViewModel.getFriendRequests()
    }
}