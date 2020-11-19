package com.robosolutions.temixtopsmarket.adapter

import androidx.recyclerview.widget.DiffUtil
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.ItemStaffBinding
import com.robosolutions.temixtopsmarket.extensions.isSameAs
import com.robotemi.sdk.UserInfo

private val callback = object : DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo) =
        oldItem.userId == newItem.userId

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo) =
        oldItem.isSameAs(newItem)
}

class UserInfoAdapter(
    private val onClick: (UserInfo) -> Unit
) : BindingListAdapter<ItemStaffBinding, UserInfo>(callback) {
    override val itemLayoutId = R.layout.item_staff

    override fun onBinding(binding: ItemStaffBinding, item: UserInfo) {
        binding.apply {
            userInfo = item

            binding.parent.setOnClickListener { onClick(item) }
        }
    }
}