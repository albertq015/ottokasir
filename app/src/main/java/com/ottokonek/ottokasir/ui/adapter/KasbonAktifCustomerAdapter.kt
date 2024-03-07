package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.ui.callback.SelectedItemKasbonAktifCustomer
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_kasbon_aktif_customer_child.view.*


class KasbonAktifCustomerAdapter(val context: Context, private var totalAmountOrder: String, var mItems: ArrayList<KasbonAktifSelectedResponseModel.DataBean.OrdersBean>, val callback: SelectedItemKasbonAktifCustomer) : RecyclerView.Adapter<KasbonAktifCustomerAdapter.Companion.ViewHolder>() {

    private var orderIdsItemKasbon: ArrayList<String> = java.util.ArrayList()
    private var amountOrder = 0.0

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvKasbonCode: TextView = itemView.findViewById(R.id.tvKasbonCode)
            val tvKasbonDate: TextView = itemView.findViewById(R.id.tvKasbonDate)
            val itemSelected: CheckBox = itemView.findViewById(R.id.itemSelected)
            val tvTotalAmount: TextView = itemView.findViewById(R.id.tvTotalAmount)

            var itemKasbonAktifCustomerParent: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_customer_parent)
            var itemKasbonAktifCustomerChild: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_customer_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_aktif_customer_parent, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvKasbonDate.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "dd MMM yyyy | HH:mm:ss",
                "dd MMMM yyyy | HH:mm")
        holder.tvKasbonCode.text = mItems[position].code
        holder.tvTotalAmount.text = mItems[position].total_amount?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }


        holder.itemSelected.isChecked = mItems[position].isSelected
        holder.itemSelected.tag = mItems[position]

        holder.itemSelected.setOnClickListener {
            val cb = it as CheckBox
            val item = cb.tag as KasbonAktifSelectedResponseModel.DataBean.OrdersBean

            item.isSelected = cb.isChecked


            if (item.isSelected) {
                amountOrder += item.total_amount!!.toDouble()
                totalAmountOrder = amountOrder.toString()
                Log.e("PLUS AMOUNT TOTAL", totalAmountOrder.toDouble().toString())

                orderIdsItemKasbon.add(item.code.toString())
                Log.e("ADD IDS", orderIdsItemKasbon.toString())


            } else if (!item.isSelected) {
                amountOrder -= item.total_amount!!.toDouble()
                totalAmountOrder = amountOrder.toString()
                Log.e("MINUS AMOUNT TOTAL", totalAmountOrder.toDouble().toString())

                orderIdsItemKasbon.remove(item.code.toString())
                Log.e("REMOVE IDS", orderIdsItemKasbon.toString())
            }

            callback.callbackSelectedKasbonCustomer(orderIdsItemKasbon, totalAmountOrder)
            notifyDataSetChanged()
        }


        holder.itemKasbonAktifCustomerParent.removeAllViews()
        holder.itemKasbonAktifCustomerChild.removeAllViewsInLayout()

        for (items in mItems[position].items!!) {
            val holderItems = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_kasbon_aktif_customer_child, holder.itemKasbonAktifCustomerParent, false)

            holder.itemKasbonAktifCustomerChild.addView(holderItems)

            Glide.with(context)
                    .load(items.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder_product)
                    .into(holderItems.ivProduct)
            holderItems.tvProductName.text = items.item_name
            holderItems.tvProductPrice.text = items.product_price?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }
            holderItems.tvQuantity.text = " x " + items.quantity
            holderItems.tvPrice.text = items.price?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }

        }

        holder.itemKasbonAktifCustomerParent.addView(holder.itemKasbonAktifCustomerChild)

    }
}