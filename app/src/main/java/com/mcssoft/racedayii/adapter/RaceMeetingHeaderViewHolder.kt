package com.mcssoft.racedayii.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mcssoft.racedayii.databinding.ListItemMeetingHeaderBinding
import com.mcssoft.racedayii.entity.cache.RaceMeetingCacheEntity
import com.mcssoft.racedayii.interfaces.IViewHolder
import com.mcssoft.racedayii.utiliy.Constants.VIEW_TYPE_DETAIL

/**
 * ViewHolder for the RaceMeeting header.
 * @param binding: The layout view.
 */
class RaceMeetingHeaderViewHolder(private val binding: ListItemMeetingHeaderBinding, private val iViewHolder: IViewHolder)
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
        iViewHolder.onViewHolderSelect(VIEW_TYPE_DETAIL, adapterPosition)
    }

}