package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonReportResponseModel
import com.ottokonek.ottokasir.ui.callback.SelectedReportKasbonType
import com.ottokonek.ottokasir.utils.MoneyUtil

class KasbonReportAdapter(var mItems: ArrayList<KasbonReportResponseModel.DataBean.JenisTransaksiBean>, val callback: SelectedReportKasbonType) : RecyclerView.Adapter<KasbonReportAdapter.Companion.ViewHolder>() {

    companion object {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvKasbonType: TextView = itemView.findViewById(R.id.tvKasbonType)
            val tvKasbonQuantity: TextView = itemView.findViewById(R.id.tvKasbonQuantity)
            val tvKasbonAmount: TextView = itemView.findViewById(R.id.tvKasbonAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_report, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val items = mItems[position]

        holder.tvKasbonAmount.text = MoneyUtil.convertIDRCurrencyFormat(items.amount!!.toDouble())
        holder.tvKasbonQuantity.text = "x" + items.qty
        holder.tvKasbonType.text = items.name

        holder.itemView.setOnClickListener {
            callback.kasbonType(mItems[position].name)
        }
    }
}