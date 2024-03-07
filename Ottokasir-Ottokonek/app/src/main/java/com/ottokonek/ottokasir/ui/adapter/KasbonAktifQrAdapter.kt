package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.ui.callback.SelectedItemQr
import com.ottokonek.ottokasir.utils.MoneyUtil

class KasbonAktifQrAdapter(val context: Context, var mItems: ArrayList<KasbonAktifSelectedResponseModel.DataBean.OrdersBean>, val callback: SelectedItemQr) : RecyclerView.Adapter<KasbonAktifQrAdapter.Companion.ViewHolder>() {

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvCustomerKasbon: TextView = itemView.findViewById(R.id.tvCustomerKasbon)
            val tvTransactionId: TextView = itemView.findViewById(R.id.tvTransactionId)
            val tvTransactionTime: TextView = itemView.findViewById(R.id.tvTransactionTime)
            val tvOrderAmount: TextView = itemView.findViewById(R.id.tvOrderAmount)
            val tvShowQr: TextView = itemView.findViewById(R.id.tvShowQr)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_aktif_qr, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCustomerKasbon.text = mItems[position].customer_name + " - " + mItems[position].customer_phone
        holder.tvTransactionId.text = mItems[position].code
        holder.tvTransactionTime.text = mItems[position].order_date
        holder.tvOrderAmount.text = mItems[position].total_amount?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }

        holder.tvShowQr.setOnClickListener {

            val orderCode = mItems[position].code
            val totalAmount = mItems[position].total_amount

            if (totalAmount != null && orderCode != null) {
                callback.orderIdsSelected(orderCode, totalAmount)

            }
        }

    }
}