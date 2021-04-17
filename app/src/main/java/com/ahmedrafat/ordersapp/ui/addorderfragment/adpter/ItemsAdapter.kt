package com.ahmedrafat.ordersapp.ui.addorderfragment.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmedrafat.ordersapp.databinding.ShopSingleItemBinding
import com.ahmedrafat.ordersapp.model.datamodels.ItemModel

class ItemsAdapter(private val items: ArrayList<ItemModel>,private val listener: onDeleteListener) :
    RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    interface onDeleteListener{
        fun onItemDelete(deletedItem:ItemModel,adapter: ItemsAdapter)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ShopSingleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemModel = items[position]
        holder.shopItemBinding.tvItemName.text = itemModel.name
        holder.shopItemBinding.tvItemQty.text = itemModel.qty.toString()
        setData(holder.shopItemBinding, itemModel,position)
    }

    private fun setData(shopItemBinding: ShopSingleItemBinding, itemModel: ItemModel, position: Int) {
        shopItemBinding.ivIncrease.setOnClickListener {
            if (itemModel.qty < 99)
                itemModel.qty = itemModel.qty+1

            notifyItemChanged(position)
        }
        shopItemBinding.ivRemove.setOnClickListener {
            if (itemModel.qty > 1)
                itemModel.qty = itemModel.qty-1

            notifyItemChanged(position)
        }
        shopItemBinding.ivDelete.setOnClickListener {
            listener.onItemDelete(itemModel,this)
        }
    }

    class ViewHolder(itemView: ShopSingleItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val shopItemBinding = itemView
    }
}