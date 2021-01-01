package com.mcssoft.racedayii.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mcssoft.racedayii.adapter.RaceMeetingHeaderViewHolder
import com.mcssoft.racedayii.interfaces.IViewHolder
import com.mcssoft.racedayii.databinding.ListItemMeetingDetailBinding
import com.mcssoft.racedayii.databinding.ListItemMeetingHeaderBinding
import com.mcssoft.racedayii.entity.cache.RaceMeetingCacheEntity
import com.mcssoft.racedayii.utiliy.Constants.VIEW_TYPE_DETAIL
import com.mcssoft.racedayii.utiliy.Constants.VIEW_TYPE_HEADER
import javax.inject.Inject

/**
 * Class implements the RaceMeeting list adapter.
 */
class RaceMeetingAdapter @Inject constructor()
    : ListAdapter<RaceMeetingCacheEntity, RecyclerView.ViewHolder>(RaceMeetingDiffCallback()) , IViewHolder {

    //https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        Log.d("TAG","RaceMeetingAdapter.onCreateViewHolder")
        return when(viewType) {
            VIEW_TYPE_HEADER -> {
                RaceMeetingHeaderViewHolder(
                    ListItemMeetingHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false), this)
            }
            VIEW_TYPE_DETAIL -> {
                RaceMeetingDetailViewHolder(
                    ListItemMeetingDetailBinding.inflate(
                            LayoutInflater.from(parent.context), parent, false), this)
            }
            else -> throw ClassCastException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("TAG","RaceMeetingAdapter.onBindViewHolder")
        when(getItemViewType(position)) {
            VIEW_TYPE_HEADER -> {
                holder as RaceMeetingHeaderViewHolder
                holder.bind(getItem(position))
            }
            VIEW_TYPE_DETAIL -> {
                holder as RaceMeetingDetailViewHolder
                holder.bind(getItem(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).meta) {
            true -> VIEW_TYPE_DETAIL
            false -> VIEW_TYPE_HEADER
        }
        //return super.getItemViewType(position)
    }

    override fun onViewHolderSelect(vhType: Int, position: Int) {
        when(vhType) {
            VIEW_TYPE_HEADER -> {
                getItem(position).meta = false
            }
            VIEW_TYPE_DETAIL -> {
                getItem(position).meta = true
            }
        }
        notifyItemChanged(position)
    }

}

private class RaceMeetingDiffCallback : DiffUtil.ItemCallback<RaceMeetingCacheEntity>() {

    override fun areItemsTheSame(oldItem: RaceMeetingCacheEntity, newItem: RaceMeetingCacheEntity): Boolean {
        return oldItem.mtgId == newItem.mtgId
    }

    override fun areContentsTheSame(oldItem: RaceMeetingCacheEntity, newItem: RaceMeetingCacheEntity): Boolean {
        return oldItem == newItem
    }
}
/*
  private void animateExpand() {
    RotateAnimation rotate =
        new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.setAnimation(rotate);
  }

  private void animateCollapse() {
    RotateAnimation rotate =
        new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
    rotate.setDuration(300);
    rotate.setFillAfter(true);
    arrow.setAnimation(rotate);
  }
 */