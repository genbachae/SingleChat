package info.fandroid.chat.ui.friends

import android.view.View
import info.fandroid.chat.R
import info.fandroid.chat.domain.friends.FriendEntity
import info.fandroid.chat.ui.core.BaseAdapter
import kotlinx.android.synthetic.main.item_friend.view.tvName
import kotlinx.android.synthetic.main.item_friend.view.tvStatus
import kotlinx.android.synthetic.main.item_friend_request.view.*

open class FriendRequestsAdapter : BaseAdapter<FriendRequestsAdapter.FriendRequestViewHolder>() {
    override val layoutRes = R.layout.item_friend_request

    override fun createHolder(view: View, viewType: Int): FriendRequestViewHolder {
        return FriendRequestViewHolder(view)
    }

    class FriendRequestViewHolder(view: View) : BaseViewHolder(view) {

        init {
            view.btnApprove.setOnClickListener {
                onClick?.onClick(item, it)
            }
            view.btnCancel.setOnClickListener {
                onClick?.onClick(item, it)
            }
        }

        override fun onBind(item: Any) {


            (item as? FriendEntity)?.let {
                view.tvName.text = it.name
            }

        }
    }
}