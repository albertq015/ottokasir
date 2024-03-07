package com.ottokonek.ottokasir.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.pojo.kasbon.ItemsBean
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_ordered.view.*

class OrderItemKasbonLunasAdapter(val context: Context, private val listItem: ArrayList<ItemsBean>?) : RecyclerView.Adapter<OrderItemKasbonLunasAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_ordered, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listItem!!.get(position)

        holder.tvQuantity.text = data.quantity.toString() + "x " + MoneyUtil.convertIDRCurrencyFormat(data.product_price?.toDouble())
        holder.tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.price?.toDouble())
        holder.tvItemName.text = data.item_name
        
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQuantity = itemView.tvQuantity!!
        var tvPrice = itemView.tvPrice!!
        var tvItemName = itemView.tvItemName!!
    }
}