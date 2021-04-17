package com.ahmedrafat.ordersapp.ui.mainfragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmedrafat.ordersapp.R
import com.ahmedrafat.ordersapp.databinding.OrdersItemsBinding
import com.ahmedrafat.ordersapp.model.apimodels.OrdersModel

class OrdersAdapter(private val orders:List<OrdersModel>) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    lateinit var context:Context

    class ViewHolder(binding:OrdersItemsBinding) : RecyclerView.ViewHolder(binding.root){
        val ordersItemsBinding = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(OrdersItemsBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderModel = orders[position]
        holder.ordersItemsBinding.tvNameval.text = orderModel.name
        holder.ordersItemsBinding.tvPhoneval.text = orderModel.phoneNumber
        holder.ordersItemsBinding.tvShopperval.text = orderModel.shopper
        holder.ordersItemsBinding.tvTimeval.text = orderModel.timeToDeliver
        val items = ArrayList<String>()
        orderModel.items?.forEach {
            items.add("${it?.name} - ${it?.count}")
        }
        val adapter = ArrayAdapter(context, R.layout.dropdown_layout,items)
        holder.ordersItemsBinding.lvItems.adapter = adapter
    }

    override fun getItemCount(): Int {
        return orders.size
    }
}