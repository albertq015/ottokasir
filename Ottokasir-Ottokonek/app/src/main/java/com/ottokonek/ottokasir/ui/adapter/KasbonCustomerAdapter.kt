package com.ottokonek.ottokasir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonCustomerResponseModel
import com.ottokonek.ottokasir.ui.callback.SelectedIdCustomer
import com.ottokonek.ottokasir.utils.MoneyUtil

class KasbonCustomerAdapter(var mItems: ArrayList<KasbonCustomerResponseModel.DataBean>, val callback: SelectedIdCustomer) : RecyclerView.Adapter<KasbonCustomerAdapter.Companion.ViewHolder>() {


    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
            val tvCustomerHandphone: TextView = itemView.findViewById(R.id.tvCustomerHandphone)
            val tvOrderAmount: TextView = itemView.findViewById(R.id.tvOrderAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_customer, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCustomerName.text = mItems[position].customer_name
        holder.tvCustomerHandphone.text = mItems[position].customer_phone
        holder.tvOrderAmount.text = mItems[position].total_cashbond_active?.let {
            MoneyUtil.convertCurrencyPHP1(it)
        }

        holder.itemView.setOnClickListener {
            mItems[position].id?.let { it1 -> callback.idCustomer(it1) }
        }

    }

    //search
    fun filterList(filteredList: ArrayList<KasbonCustomerResponseModel.DataBean>) {
        mItems = filteredList
        notifyDataSetChanged()
    }
}