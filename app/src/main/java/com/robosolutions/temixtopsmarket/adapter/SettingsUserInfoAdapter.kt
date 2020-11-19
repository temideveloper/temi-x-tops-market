package com.robosolutions.temixtopsmarket.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.databinding.ItemStaffSelectBinding
import com.robosolutions.temixtopsmarket.extensions.isSameAs
import com.robotemi.sdk.UserInfo

data class UserInfoListItem(val selected: Boolean, val userInfo: UserInfo)

private val callback = object : DiffUtil.ItemCallback<UserInfoListItem>() {
    override fun areItemsTheSame(oldListItem: UserInfoListItem, newListItem: UserInfoListItem) =
        oldListItem.userInfo.userId == newListItem.userInfo.userId

    override fun areContentsTheSame(oldListItem: UserInfoListItem, newListItem: UserInfoListItem) =
        oldListItem == newListItem

    override fun getChangePayload(oldItem: UserInfoListItem, newItem: UserInfoListItem): Any? {
        return if (oldItem.userInfo.isSameAs(newItem.userInfo) && oldItem.selected != newItem.selected) {
            CheckBoxPayload
        } else {
            super.getChangePayload(oldItem, newItem)
        }
    }
}

class SettingsUserInfoAdapter(private val onCheckedChange: (View, UserInfoListItem, Boolean) -> Unit) :
    BindingListAdapter<ItemStaffSelectBinding, UserInfoListItem>(callback) {
    override val itemLayoutId = R.layout.item_staff_select

    override fun onBindViewHolder(
        holder: BindingViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads.any { it == CheckBoxPayload }) {
                holder.binding.checkBoxStaff.isChecked = getItem(position).selected
            }
        }
    }

    override fun onBinding(binding: ItemStaffSelectBinding, item: UserInfoListItem) {
        binding.apply {
            selected = item.selected
            userInfo = item.userInfo

            binding.checkBoxStaff.setOnClickListener {
                onCheckedChange(it, item, binding.checkBoxStaff.isChecked)
            }
        }
    }
}

object CheckBoxPayload