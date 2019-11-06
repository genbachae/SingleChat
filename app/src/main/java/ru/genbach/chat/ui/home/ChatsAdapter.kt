package ru.genbach.chat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.genbach.chat.databinding.ItemChatBinding
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.ui.core.BaseAdapter

open class ChatsAdapter : BaseAdapter<ChatsAdapter.ChatViewHolder>() {  //  Адаптер. Для отображения списка чатов.
    //   реализация функции создания вьюхолдера. Возвращает объект ChatViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
        return ChatViewHolder(binding)
    }

    class ChatViewHolder(val binding: ItemChatBinding) : BaseViewHolder(binding.root) { //  Вьюхолдер. Для отображения элемента списка чатов.
        init {                                  //  блок инициализации. Присвоение слушателей нажатий.
            binding.root.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {                //  заполнение макета данными. Принимает Any. Ничего не возвращает.
            (item as? MessageEntity)?.let {
                binding.message = it
            }
        }
    }
}