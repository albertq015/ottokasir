package com.ottokonek.ottokasir.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.TransactionReportResponse
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_report.view.*

class TransactionTypeAdapter(val context: Context) : RecyclerView.Adapter<TransactionTypeAdapter.ViewHolder>() {

    private var listData = mutableListOf<TransactionReportResponse.DataBean.JenisTransaksiBean>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_report, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val data = listData.get(p1)
        holder.tvProduct.text = data.name
        holder.tvQuantity.text = "x"+data.qty.toString()
        holder.tvPrice.text = MoneyUtil.convertCurrencyPHP1(data.amount.toDouble())
    }

    fun setData(data: MutableList<TransactionReportResponse.DataBean.JenisTransaksiBean>) {
        listData = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProduct = itemView.tvProduct
        val tvQuantity = itemView.tvQuantity
        val tvPrice = itemView.tvPrice
        val llContainer = itemView.llContainer
    }
}