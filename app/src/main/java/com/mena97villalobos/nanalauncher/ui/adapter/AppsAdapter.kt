package com.mena97villalobos.nanalauncher.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mena97villalobos.nanalauncher.databinding.AppItemBinding
import com.mena97villalobos.nanalauncher.model.AppModel

class AppsAdapter(private val context: Context) :
    ListAdapter<AppModel, AppsAdapter.ViewHolder>(ReceiptDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position)!!)


    class ViewHolder private constructor(private val binding: AppItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder =
                ViewHolder(
                    AppItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }

        fun bind(item: AppModel) {
            binding.appName.text = item.appName
            binding.appImage.setImageDrawable(item.appIcon)
            binding.clickListener = item.onClickListener
            binding.executePendingBindings()
        }
    }

}


class ReceiptDiffCallback : DiffUtil.ItemCallback<AppModel>() {

    override fun areItemsTheSame(oldItem: AppModel, newItem: AppModel): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: AppModel, newItem: AppModel): Boolean =
        oldItem == newItem
}