package com.mcssoft.racedayii.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcssoft.racedayii.databinding.ListItemMeetingDetailBinding
import com.mcssoft.racedayii.entity.cache.RaceMeetingCacheEntity
import com.mcssoft.racedayii.interfaces.IViewHolder
import com.mcssoft.racedayii.utiliy.Constants

/**
 * ViewHolder for the RaceMeeting detail.
 * @param binding: The layout view.
 */
class RaceMeetingDetailViewHolder(private val binding: ListItemMeetingDetailBinding, private val iViewHolder: IViewHolder)
    : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    init {
        binding.clickListener = this
    }

    internal fun bind(mtg: RaceMeetingCacheEntity) {
        binding.apply {
            meeting = mtg

            executePendingBindings()
        }
    }

    override fun onClick(view: View) {
        iViewHolder.onViewHolderSelect(Constants.VIEW_TYPE_HEADER, adapterPosition)
    }

}