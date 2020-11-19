package com.robosolutions.temixtopsmarket.adapter

import androidx.recyclerview.widget.DiffUtil
import com.robosolutions.temixtopsmarket.R
import com.robosolutions.temixtopsmarket.database.PromotionVideo
import com.robosolutions.temixtopsmarket.databinding.ItemVideoBinding

private val callback = object : DiffUtil.ItemCallback<PromotionVideo>() {
    override fun areItemsTheSame(oldItem: PromotionVideo, newItem: PromotionVideo) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PromotionVideo, newItem: PromotionVideo) =
        oldItem == newItem
}

class PromotionVideoAdapter(
    private val isEditMode: Boolean
) : BindingListAdapter<ItemVideoBinding, PromotionVideo>(callback) {
    override val itemLayoutId = R.layout.item_video

    override fun onBinding(binding: ItemVideoBinding, item: PromotionVideo) {
        binding.apply {
            binding.editable = isEditMode
            binding.promotionVideo = item

            binding.parent.setOnClickListener {

            }
        }
    }
}