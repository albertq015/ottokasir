package com.ottokonek.ottokasir.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.PaymentCashResponseModel
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_ordered.view.*

class OrderedItemAdapter(val context: Context, val listItem: List<PaymentCashResponseModel.DataBean.ItemsBean>?) : RecyclerView.Adapter<OrderedItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_ordered, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItem?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = listItem!!.get(position)

        holder.tvQuantity.text = data.quantity.toString() + "x " + MoneyUtil.convertIDRCurrencyFormat(data.product_price?.toDouble())
        holder.tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.price?.toDouble())
        holder.tvItemName.text = data.item_name

        //before
        /*holder.tvQuantity.text = data.quantity.toString() + "x " + MoneyUtil.convertIDRCurrencyFormat(data.price?.toDouble())
        holder.tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.quantity?.toDouble()?.times(data.price?.toDouble()!!))
        holder.tvItemName.text = data.item_name*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvQuantity = itemView.tvQuantity!!
        var tvPrice = itemView.tvPrice!!
        var tvItemName = itemView.tvItemName!!
    }
}