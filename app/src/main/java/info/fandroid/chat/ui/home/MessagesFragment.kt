package info.fandroid.chat.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import info.fandroid.chat.R
import info.fandroid.chat.cache.ChatDatabase
import info.fandroid.chat.domain.messages.MessageEntity
import info.fandroid.chat.presentation.viewmodel.MessagesViewModel
import info.fandroid.chat.remote.service.ApiService
import info.fandroid.chat.ui.App
import info.fandroid.chat.ui.core.BaseFragment
import info.fandroid.chat.ui.core.ext.onFailure
import info.fandroid.chat.ui.core.ext.onSuccess
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesFragment : BaseFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var lm: RecyclerView.LayoutManager

    private val viewAdapter = MessagesAdapter()

    override val titleToolbar = R.string.chats
    override val layoutId = R.layout.fragment_messages

    lateinit var messagesViewModel: MessagesViewModel

    private var contactId = 0L
    private var contactName = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }


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

    override fun onResume() {
        super.onResume()
        base {
            supportActionBar?.title = contactName
        }

        messagesViewModel.getMessages(contactId)
    }

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
