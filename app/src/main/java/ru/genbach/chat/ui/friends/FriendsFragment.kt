package ru.genbach.chat.ui.friends

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import ru.genbach.chat.R
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.domain.type.None
import ru.genbach.chat.presentation.viewmodel.FriendsViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseListFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess

class FriendsFragment : BaseListFragment() {    //  Для отображения списка друзей.
    override val viewAdapter = FriendsAdapter() //  объект

    lateinit var friendsViewModel: FriendsViewModel     //  view model для взаимодействия с друзьями. Тип:

    override val titleToolbar = R.string.screen_friends

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)           //  инъекция компонентов.
    }
    //  добавлен слушатель для progressData. Вызов метода getFriends(…) перенесен из onResume(…) в onViewCreated(…).
    //  инициализация friendsViewModel, установка слушателей для friendsData, deleteFriendData.
    //  Присвоение слушателя нажатия на кнопку элемента списка.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {   //  6й в слушатель нажатия добавлен вызов метода showUser.
        super.onViewCreated(view, savedInstanceState)

        friendsViewModel = viewModel {
            onSuccess(friendsData, ::handleFriends)
            onSuccess(deleteFriendData, ::handleDeleteFriend)
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }

        setOnItemClickListener { it, v ->
            (it as? FriendEntity)?.let {
                when (v.id) {
                    R.id.btnRemove -> showDeleteFriendDialog(it)
                    else -> {
                        navigator.showUser(requireActivity(), it)
                    }
                }
            }
        }

        friendsViewModel.getFriends()
    }

    //  отображение диалога о удалении друга. Принимает Ничего не возвращает.
    private fun showDeleteFriendDialog(entity: FriendEntity) {
        activity?.let {
            AlertDialog.Builder(it)
                .setMessage(getString(R.string.delete_friend))

                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    friendsViewModel.deleteFriend(entity)
                }

                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }

    //  обработка получения списка. Очищение адаптера, добавление в него элементов. Принимает List<FriendEntity>. Ничего не возвращает.
    private fun handleFriends(friends: List<FriendEntity>?) {
        hideProgress()
        if (friends != null) {
            viewAdapter.clear()
            viewAdapter.add(friends)
            viewAdapter.notifyDataSetChanged()
        }
    }
    //  обработка удаления друга. Повторный вызов получения списка. Принимает None. Ничего не возвращает.
    private fun handleDeleteFriend(none: None?) {
        friendsViewModel.getFriends()
    }
}