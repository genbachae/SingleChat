package info.fandroid.chat.ui.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import info.fandroid.chat.databinding.ItemFriendBinding
import info.fandroid.chat.domain.friends.FriendEntity
import info.fandroid.chat.ui.core.BaseAdapter

open class FriendsAdapter : BaseAdapter<FriendsAdapter.FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFriendBinding.inflate(layoutInflater, parent, false)
        return FriendViewHolder(binding)
    }

    class FriendViewHolder(val binding: ItemFriendBinding) : BaseViewHolder(binding.root) {
        init {
            binding.btnRemove.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {
            (item as? FriendEntity)?.let {
                binding.friend = it
            }
        }
    }
}

