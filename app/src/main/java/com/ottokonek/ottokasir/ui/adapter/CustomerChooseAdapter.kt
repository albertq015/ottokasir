package com.ottokonek.ottokasir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.ui.callback.ActionList

class CustomerChooseAdapter(var mItems: ArrayList<CustomerModel>, val callback: ActionList) : RecyclerView.Adapter<CustomerChooseAdapter.Companion.ViewHolder>() {


    private var limitItem = 4

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
            val tvCustomerHandphone: TextView = itemView.findViewById(R.id.tvCustomerHandphone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_choose_customer, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
        /*return if (mItems.size > limitItem) {
            limitItem
        } else {
            mItems.size
        }*/
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCustomerName.text = mItems[position].fullName
        holder.tvCustomerHandphone.text = mItems[position].noHandphone

        holder.itemView.setOnClickListener {
            callback.clickItem(position, mItems[position])
        }
    }
}