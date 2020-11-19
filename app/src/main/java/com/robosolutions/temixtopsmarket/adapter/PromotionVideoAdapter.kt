package com.robosolutions.temixtopsmarket.adapter

import android.content.Context
import android.view.View
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
    private val isEditMode: Boolean,
    private val context: Context,
    private val onClickCard: (View, PromotionVideo) -> Unit,
    private val onClickRemove: (PromotionVideo) -> Unit,
) : BindingListAdapter<ItemVideoBinding, PromotionVideo>(callback) {
    override val itemLayoutId = R.layout.item_video

    override fun onBinding(binding: ItemVideoBinding, item: PromotionVideo) {
        binding.apply {
            binding.editable = isEditMode
            binding.promotionVideo = item
            binding.videoFile = item.getVideoFile(context)

            binding.parent.setOnClickListener {
                onClickCard(it, item)
            }

            binding.buttonRemove.setOnClickListener {
                onClickRemove(item)
            }
        }
    }
}