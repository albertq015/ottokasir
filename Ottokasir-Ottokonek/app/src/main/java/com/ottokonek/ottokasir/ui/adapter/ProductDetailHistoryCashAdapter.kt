package com.ottokonek.ottokasir.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.miscModel.ItemModel
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_ordered.view.*

class ProductDetailHistoryCashAdapter(val context: Context) : RecyclerView.Adapter<OrderedItemAdapter.ViewHolder>() {

    var listItem = emptyArray<ItemModel>()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderedItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_ordered, p0, false)

        return OrderedItemAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listItem.size != 0) {
            return listItem.size!!;
        }
        return 0
    }

    override fun onBindViewHolder(holder: OrderedItemAdapter.ViewHolder, position: Int) {
        var data = ItemModel(0, 0.0, "", 0)
        
        if (listItem.size != 0) {
            data = listItem.get(position)
        }

        val amountQty: Double = (data.amount?.toDouble()!! / data.qty!!.toDouble())
        holder.tvQuantity.text = data.qty.toString() + "x " + MoneyUtil.convertIDRCurrencyFormat(amountQty)
        holder.tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.amount?.toDouble())
        holder.tvItemName.text = data.name
    }

    fun setData(data: Array<ItemModel>) {
        listItem = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQuantity = itemView.tvQuantity!!
        var tvPrice = itemView.tvPrice!!
        var tvItemName = itemView.tvItemName!!
    }
}