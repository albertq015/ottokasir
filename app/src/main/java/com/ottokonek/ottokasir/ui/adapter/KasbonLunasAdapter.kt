package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonLunasResponseModel
import com.ottokonek.ottokasir.ui.activity.receipt.DetailReceiptActivity
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_kasbon_lunas_child.view.*
import kotlinx.android.synthetic.main.item_list_kasbon_lunas_child.view.tvDetail
import kotlinx.android.synthetic.main.item_list_kasbon_lunas_child.view.tvOrderAmount
import kotlinx.android.synthetic.main.item_list_kasbon_lunas_child.view.tvTransactionId
import kotlinx.android.synthetic.main.item_list_kasbon_lunas_child.view.tvTransactionTime

class KasbonLunasAdapter(val context: Context, var mItems: List<KasbonLunasResponseModel.DataBean>, val callback: ActionList) : RecyclerView.Adapter<KasbonLunasAdapter.Companion.ViewHolder>() {


    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvDateKasbon: TextView = itemView.findViewById(R.id.tvDateKasbon)
            val tvTotalKasbon: TextView = itemView.findViewById(R.id.tvTotalKasbon)

            var itemKasbonLunasParent: ViewGroup = itemView.findViewById(R.id.item_kasbon_lunas_parent)
            var itemKasbonLunasChild: ViewGroup = itemView.findViewById(R.id.item_kasbon_lunas_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_lunas_parent, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvDateKasbon.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "yyyy-MM-dd",
                "dd MMMM yyyy")
        holder.tvTotalKasbon.text = context.getString(R.string.total_kasbon_spasi) + mItems[position].total_cashbond

        holder.itemKasbonLunasParent.removeAllViews()
        holder.itemKasbonLunasChild.removeAllViewsInLayout()

        for (item in mItems[position].order!!) {


            val viewTransaction = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_kasbon_lunas_child, holder.itemKasbonLunasChild, false)
            holder.itemKasbonLunasChild.addView(viewTransaction)

            viewTransaction.tvCustomerKasbon.text = item.customer_name + " - " + item.customer_phone
            viewTransaction.tvTransactionId.text = item.code
            viewTransaction.tvTransactionTime.text =  DateUtil.simplifyDateFormat(item.cashbond_payment_date,
                    "dd MMM yyyy | HH:mm:ss",
                    "dd MMMM yyyy | HH:mm")
            viewTransaction.tvOrderAmount.text = item.total_amount?.toDouble()?.let {
                MoneyUtil.convertIDRCurrencyFormat(it)
            }

            viewTransaction.tvDetail.setOnClickListener {
                val intent = Intent(context, DetailReceiptActivity::class.java)
                intent.putExtra(DetailReceiptActivity.KEY_PAYMENT_KASBON_LUNAS, item)
                (context as Activity).startActivity(intent)
            }

        }
        holder.itemKasbonLunasParent.addView(holder.itemKasbonLunasChild)

    }

    //search
    /*fun filterList(filteredList: ArrayList<KasbonLunasResponseModel.DataBean>) {
        mItems = filteredList
        notifyDataSetChanged()
    }*/
}