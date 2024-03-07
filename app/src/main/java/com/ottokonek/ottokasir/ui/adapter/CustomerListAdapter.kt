package com.ottokonek.ottokasir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.ui.callback.ActionList

class CustomerListAdapter(var mItems: ArrayList<CustomerModel>, val callback: ActionList) : RecyclerView.Adapter<CustomerListAdapter.Companion.ViewHolder>() {

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvCustomerInitial: TextView = itemView.findViewById(R.id.tvCustomerInitial)
            val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
            val tvCustomerHandphone: TextView = itemView.findViewById(R.id.tvCustomerHandphone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_customer, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvCustomerName.text = mItems[position].fullName
        holder.tvCustomerHandphone.text = mItems[position].noHandphone

        val peterParker = mItems[position].fullName

        val initials = peterParker
                .split(' ')
                .mapNotNull { it.firstOrNull()?.toString() }
                .reduce { acc, s -> acc + s }

        holder.tvCustomerInitial.text = initials

        holder.itemView.setOnClickListener {
            callback.clickItem(position, mItems[position])
        }
    }

    //search
    fun filterList(filteredList: ArrayList<CustomerModel>) {
        mItems = filteredList
        notifyDataSetChanged()
    }
}