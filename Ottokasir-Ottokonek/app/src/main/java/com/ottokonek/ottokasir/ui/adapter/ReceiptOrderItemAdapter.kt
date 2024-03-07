package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.pojo.ItemModel
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.row_receipt_order_item.view.*

class ReceiptOrderItemAdapter(val itemList: List<ItemModel>?, val activity: Activity) : RecyclerView.Adapter<ReceiptOrderItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.row_receipt_order_item, null, false)

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {

        var item: ItemModel = itemList!!.get(pos)
        holder.itemName.text = item.item_name

        val priceAfterDiscount: Double = item.price.toDouble() - item.discount.toDouble()
        holder.itemPrice.text = MoneyUtil.convertIDRCurrencyFormat(priceAfterDiscount)

        val discount: Double = item.discount.toDouble()
        if (discount > 0) holder.itemBeforeDiscount.text = MoneyUtil.convertIDRCurrencyFormat(item.price.toDouble())
        else holder.itemBeforeDiscount.text = ""
        holder.itemBeforeDiscount.paintFlags = holder.itemBeforeDiscount.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG;

        holder.itemQty.text = "${item.quantity} Item"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemName = itemView.itemName!!
        var itemPrice = itemView.itemPrice!!
        var itemBeforeDiscount = itemView.itemPriceBeforeDiscount!!
        var itemQty = itemView.itemQty!!

    }
}