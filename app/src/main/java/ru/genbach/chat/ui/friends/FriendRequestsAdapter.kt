package ru.genbach.chat.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.genbach.chat.databinding.ItemFriendRequestBinding
import ru.genbach.chat.domain.friends.FriendEntity
import ru.genbach.chat.ui.core.BaseAdapter
//  Реализация метода createHolder() перенесена в onCreateViewHolder().
//  Адаптер. Для отображения списка приглашений дружбы.
open class FriendRequestsAdapter : BaseAdapter<FriendRequestsAdapter.FriendRequestViewHolder>() {
    //  добавлен инфлейт макета. Добавлено создание и инициализация поля binding, которое передается при создании FriendRequestViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {    //  8й убран вызов инфлейтера.
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendRequestBinding.inflate(layoutInflater)
        return FriendRequestViewHolder(binding)
    }
    //  Входной параметр View заменен поле ItemFriendRequestBinding. В конструктор класса-родителя передается binding.root.
    class FriendRequestViewHolder(val binding: ItemFriendRequestBinding) : BaseViewHolder(binding.root) {

        init {  //  блок инициализации. Присвоение слушателей нажатий.
            binding.btnApprove.setOnClickListener {
                onClick?.onClick(item, it)
            }
            binding.btnCancel.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }
        //  реализация внутри блока item.let заменена на присвоение полю binding.friend в макете значения it.
        //  заполнение макета данными. Принимает Any. Ничего не возвращает.
        override fun onBind(item: Any) {    //  6й добавлено отображение фото пользователя.
            (item as? FriendEntity)?.let {
                binding.friend = it
            }
        }
    }

    //  createHolder(…) – реализация функции создания вьюхолдера. Возвращает объект FriendRequestsViewHolder.
    //  FriendRequestHolder - Для отображения элемента приглашения в друзья.
}