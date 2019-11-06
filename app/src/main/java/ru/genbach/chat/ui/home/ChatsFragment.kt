package ru.genbach.chat.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import ru.genbach.chat.R
import ru.genbach.chat.cache.ChatDatabase
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.presentation.viewmodel.MessagesViewModel
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseListFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess


class ChatsFragment : BaseListFragment() {  //  Фрагмент-список. Для отображения списка чатов.
    override val viewAdapter = ChatsAdapter()       //  объект

    override val titleToolbar = R.string.chats      //  переопределение заголовка тулбара. Тип: Int.

    private lateinit var messagesViewModel: MessagesViewModel   //  viewmodel для взаимодействия с сообщениями. Тип: MessagesViewModel.

    override fun onCreate(savedInstanceState: Bundle?) {    //  инъекция компонентов.
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }
    //  инициализация messagesViewModel, установка слушателей для progressData, failureData.
    //  Присвоение слушателя нажатия на кнопку элемента списка. Установка слушателя для getLiveChats(…).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesViewModel = viewModel {
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }

        setOnItemClickListener { it, v ->
            (it as? MessageEntity)?.let {
                val contact = it.contact
                if (contact != null) {
                    navigator.showChatWithContact(contact.id, contact.name, requireActivity())
                }
            }
        }

        ChatDatabase.getInstance(requireContext()).messagesDao.getLiveChats().observe(this, Observer {
            val list = it.distinctBy { it.contact?.id }.toList()
            handleChats(list)
        })
    }

    override fun onResume() {   //  вызов функции получения списка чатов.
        super.onResume()

        messagesViewModel.getChats()
    }
    //  обработка получения списка. Очищение адаптера, добавление в него элементов. Принимает List<MessageEntity>. Ничего не возвращает.
    fun handleChats(messages: List<MessageEntity>?) {
        if (messages != null) {
            viewAdapter.clear()
            viewAdapter.add(messages)
            viewAdapter.notifyDataSetChanged()
        }
    }
}
