package ru.genbach.chat.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import ru.genbach.chat.domain.messages.GetChats
import ru.genbach.chat.domain.messages.GetMessagesWithContact
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.domain.messages.SendMessage
import ru.genbach.chat.domain.type.None
import javax.inject.Inject

class MessagesViewModel @Inject constructor(    //  Класс ViewModel. Для взаимодействия с сообщениями.
    val getChatsUseCase: GetChats,                  //  usecase для получения списка чатов. Тип: GetChats.
    val getMessagesUseCase: GetMessagesWithContact, //  usecase для получения списка сообщений. Тип: GetMessagesWithContact.
    val sendMessageUseCase: SendMessage             //  use case для отправления сообщений. Тип: SendMessage.
): BaseViewModel() {                            //  Наследуется от: Базового класса BaseViewModel.
    val getChatsData: MutableLiveData<List<MessageEntity>> = MutableLiveData()  //  хранит список чатов. Тип: MutableLiveData<List<MessageEntity>>.
    val getMessagesData: MutableLiveData<List<MessageEntity>> = MutableLiveData()   // хранит список сообщений. Тип: MutableLiveData<List<MessageEntity>>.
    val sendMessageData: MutableLiveData<None> = MutableLiveData()              //  хранит состояние Тип: MutableLiveData<None>.

    fun getChats(needFetch: Boolean = false) {  //  выполняет получение списка чатов. Принимает Boolean: needFetch. Ничего не возвращает.
        getChatsUseCase(GetChats.Params(needFetch)) { it.either(::handleFailure) { handleGetChats(it, !needFetch)} }
    }
    //  выполняет получение списка сообщений. Принимает: Long: contactId, Boolean: needFetch.. Ничего не возвращает.
    fun getMessages(contactId: Long, needFetch: Boolean = false) {
        getMessagesUseCase(GetMessagesWithContact.Params(contactId, needFetch)) {
            it.either(::handleFailure) { handleGetMessages(it, contactId, !needFetch) }
        }
    }
    //  принимает приглашение дружбы. Принимает: Long: toId; String: message, image. Ничего не возвращает.
    fun sendMessage(toId: Long, message: String, image: String) {
        sendMessageUseCase(SendMessage.Params(toId, message, image)) { it.either(::handleFailure) { handleSendMessage(it, toId)} }
    }

    private fun handleGetChats(messages: List<MessageEntity>, fromCache: Boolean) {     //  сеттеры для
        getChatsData.value = messages
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getChats(true)
        }
    }

    private fun handleGetMessages(messages: List<MessageEntity>, contactId: Long, fromCache: Boolean) { //  сеттеры для
        getMessagesData.value = messages
        updateProgress(false)

        if (fromCache) {
            updateProgress(true)
            getMessages(contactId, true)
        }
    }

    private fun handleSendMessage(none: None, contactId: Long) {        //  сеттеры для
        sendMessageData.value = none

        getMessages(contactId, true)
    }

    override fun onCleared() {
        super.onCleared()
        getChatsUseCase.unsubscribe()
        getMessagesUseCase.unsubscribe()
        sendMessageUseCase.unsubscribe()
    }
}