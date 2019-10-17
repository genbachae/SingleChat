package info.fandroid.chat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import info.fandroid.chat.databinding.ItemChatBinding
import info.fandroid.chat.domain.messages.MessageEntity
import info.fandroid.chat.ui.core.BaseAdapter
import info.fandroid.chat.ui.core.GlideHelper
import kotlinx.android.synthetic.main.fragment_account.*

open class ChatsAdapter : BaseAdapter<ChatsAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemChatBinding.inflate(layoutInflater, parent, false)
        return ChatViewHolder(binding)
    }

    class ChatViewHolder(val binding: ItemChatBinding) : BaseViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {
            (item as? MessageEntity)?.let {
                binding.message = it
            }
        }
    }
}