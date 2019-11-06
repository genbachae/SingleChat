package ru.genbach.chat.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.genbach.chat.databinding.ItemMessageMeBinding
import ru.genbach.chat.databinding.ItemMessageOtherBinding
import ru.genbach.chat.domain.messages.MessageEntity
import ru.genbach.chat.ui.core.BaseAdapter
    //  Адаптер. Для отображения списка сообщений.
open class MessagesAdapter : ListAdapter<MessageEntity, BaseAdapter.BaseViewHolder>(MessageDiffCallback()) {
    //  реализация функции создания вьюхолдера. В зависимости от значения viewType создает вьюхолдер для входящего или исходящего сообщения:
    //  MessageMeViewHolder – 0, MessageOtherViewHolder – 1. Возвращает объект BaseViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapter.BaseViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val holder = if (viewType == 0) {
            MessageMeViewHolder(ItemMessageMeBinding.inflate(layoutInflater, parent, false))
        } else {
            MessageOtherViewHolder(ItemMessageOtherBinding.inflate(layoutInflater, parent, false))
        }

        return holder
    }
    //  реализация функции бандинга вьюхолдера. Делагирует заполнение макета данными самому вьюхолдеру.
    override fun onBindViewHolder(holder: BaseAdapter.BaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    //  реализация определения типа вью. В зависимости от значения fromMe возвращает: true – 0, false – 1.
    override fun getItemViewType(position: Int): Int {
        (getItem(position) as MessageEntity).let {
            return if (it.fromMe) 0 else 1
        }
    }
    //  Вьюхолдер. Для отображения исходящего сообщения в списке.
    class MessageMeViewHolder(val binding: ItemMessageMeBinding) : BaseAdapter.BaseViewHolder(binding.root) {
        init {                                      //  блок инициализации. Присвоение слушателей нажатий.
            binding.root.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {            //  заполнение макета данными. Принимает Any. Ничего не возвращает.
            (item as? MessageEntity)?.let {
                binding.message = it
            }
        }
    }

    class MessageOtherViewHolder(val binding: ItemMessageOtherBinding) : BaseAdapter.BaseViewHolder(binding.root) {
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

    //  Вспомогательный класс. Дает возможность адаптеру самому определять действие над элементами
    //  при добавлении списка(добавить, обновить, удалить) сопровождая это соответствующей анимацией.
    class MessageDiffCallback : DiffUtil.ItemCallback<MessageEntity>() {
        //  реализация сравнения двух элементов списка. Если id совпадают – возвращает
        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem.id == newItem.id
        }
        //  реализация сравнения контента двух элементов списка. Если контент совпадает – возвращает
        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem == newItem
        }
    }
}