package ru.genbach.chat.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.genbach.chat.databinding.ItemFriendBinding
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.ui.core.BaseAdapter

open class FriendsAdapter : BaseAdapter<FriendsAdapter.FriendViewHolder>() {    //  Адаптер. Для отображения списка друзей.
    //  добавлен инфлейт макета. Добавлено создание и инициализация поля binding, которое передается при создании FriendViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendBinding.inflate(layoutInflater, parent, false)
        return FriendViewHolder(binding)
    }
    //  Входной параметр View заменен поле ItemFriendBinding. В конструктор класса-родителя передается binding.root.
    class FriendViewHolder(val binding: ItemFriendBinding) : BaseViewHolder(binding.root) { //  Для отображения элемента друга.
        init {      //  блок инициализации. Присвоение слушателя нажатия.
            binding.btnRemove.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }
        //  реализация внутри блока item.let заменена на присвоение полю binding.friend в макете значения it.
        //  заполнение макета данными. Принимает Any. Ничего не возвращает.
        override fun onBind(item: Any) {    // добавлено отображение фото пользователя. Добавлено скрытие view статуса, если он пустой.
            (item as? FriendEntity)?.let {
                binding.friend = it
            }
        }
    }
    //  createHolder(…) – реализация функции создания вьюхолдера. Возвращает объект FriendViewHolder.
}

