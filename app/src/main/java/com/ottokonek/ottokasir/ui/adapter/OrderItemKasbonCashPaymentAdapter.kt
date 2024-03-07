package com.ottokonek.ottokasir.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonCashPaymentResponseModel
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_kasbon_cash_payment_child.view.*

class OrderItemKasbonCashPaymentAdapter(val context: Context, var mItems: ArrayList<KasbonCashPaymentResponseModel.DataBean.OrdersBean>) : RecyclerView.Adapter<OrderItemKasbonCashPaymentAdapter.Companion.ViewHolder>() {


    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvDate: TextView = itemView.findViewById(R.id.tvDate)
            val tvOrderID: TextView = itemView.findViewById(R.id.tvOrderID)
            val tvTime: TextView = itemView.findViewById(R.id.tvTime)

            var itemKasbonCashPaymentParent: ViewGroup = itemView.findViewById(R.id.item_kasbon_cash_payment_parent)
            var itemKasbonCashPaymentChild: ViewGroup = itemView.findViewById(R.id.item_kasbon_cash_payment_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_cash_payment_parent, parent, false))
    }


    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvOrderID.text = mItems[position].code
        holder.tvDate.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                "dd MMMM yyyy,")
        holder.tvTime.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "dd MMM yyyy | HH:mm:ss",
                "HH:mm")

        holder.itemKasbonCashPaymentParent.removeAllViews()
        holder.itemKasbonCashPaymentChild.removeAllViewsInLayout()

        for (items in mItems[position].items!!) {
            val holderItems = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_kasbon_cash_payment_child, holder.itemKasbonCashPaymentParent, false)
            holder.itemKasbonCashPaymentChild.addView(holderItems)

            holderItems.tvQuantity.text = items.quantity.toString() + "x " + MoneyUtil.convertIDRCurrencyFormat(items.product_price?.toDouble())
            holderItems.tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(items.price?.toDouble())
            holderItems.tvItemName.text = items.item_name

        }
        holder.itemKasbonCashPaymentParent.addView(holder.itemKasbonCashPaymentChild)
    }

}