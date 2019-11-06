package ru.genbach.chat.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.genbach.chat.R
import ru.genbach.chat.cache.ChatDatabase
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.presentation.viewmodel.MessagesViewModel
import ru.genbach.chat.remote.service.ApiService
import ru.genbach.chat.ui.App
import ru.genbach.chat.ui.core.BaseFragment
import ru.genbach.chat.ui.core.ext.onFailure
import ru.genbach.chat.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesFragment : BaseFragment() {   //  Фрагмент-список. Для отображения списка чатов.
    private lateinit var recyclerView: RecyclerView     //  вью списка. Тип: RecyclerView.
    private lateinit var lm: RecyclerView.LayoutManager //  объект

    private val viewAdapter = MessagesAdapter() //  объект

    override val titleToolbar = R.string.chats  //  переопределение заголовка тулбара. Тип: Int.
    override val layoutId = R.layout.fragment_messages  //  переопределенный id макета фрагмента. Присвоен макет фрагмента сообщений. Тип: Int.

    lateinit var messagesViewModel: MessagesViewModel   //  viewmodel для взаимодействия с сообщениями. Тип: MessagesViewModel.

    private var contactId = 0L                          //  идентификатор контакта. Тип: Int.
    private var contactName = ""                        //  имя контакта. Тип: String.


    override fun onCreate(savedInstanceState: Bundle?) {    //  инъекция компонентов.
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    //  инициализация вью списка, присвоение адаптера. Инициализация messagesViewModel,
    //  установка слушателей для getMessageData, progressData, failureData.
    //  Восстановление данных контакта(id, имя) из intent. Присвоение слушателя нажатия на кнопку отправления сообщения.
    //  Установка слушателя для getLiveMessagesWithContact(…).
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lm = LinearLayoutManager(context)

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView).apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = viewAdapter
        }


        (lm as? LinearLayoutManager)?.apply {
            stackFromEnd = true
        }

        messagesViewModel = viewModel {
            onSuccess(getMessagesData, ::handleMessages)
            onSuccess(progressData, ::updateProgress)
            onFailure(failureData, ::handleFailure)
        }

        base {
            val args = intent.getBundleExtra("args")

            if (args != null) {
                contactId = args.getLong(ApiService.PARAM_CONTACT_ID)
                contactName = args.getString(ApiService.PARAM_NAME, "")
            } else {
                contactId = intent.getLongExtra(ApiService.PARAM_CONTACT_ID, 0L)
                contactName = intent.getStringExtra(ApiService.PARAM_NAME)
            }
        }

        btnSend.setOnClickListener {
            sendMessage()
        }

        ChatDatabase.getInstance(requireContext()).messagesDao.getLiveMessagesWithContact(contactId).observe(this, Observer {
            handleMessages(it)
        })
    }

    override fun onResume() {   //  присваивает заголовку тулбара имя контакта. Вызывает функцию получения списка сообщений.
        super.onResume()
        base {
            supportActionBar?.title = contactName
        }

        messagesViewModel.getMessages(contactId)
    }
    //  добавляет в адаптер список Прокручивает список в самый низ. Принимает List<MessageEntity>. Ничего не возвращает.
    private fun handleMessages(messages: List<MessageEntity>?) {
        if (messages != null) {
            viewAdapter.submitList(messages)
            Handler().postDelayed({
                recyclerView.smoothScrollToPosition(viewAdapter.itemCount - 1)
            }, 100)
        }
    }


    private fun sendMessage() {
        if (contactId == 0L) return

        val text = etText.text.toString()

        if (text.isBlank()) {
            showMessage("Введите текст")
            return
        }

        showProgress()

        messagesViewModel.sendMessage(contactId, text, "")

        etText.text.clear()
    }
}
