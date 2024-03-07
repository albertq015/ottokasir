package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonAktifResponseModel
import com.ottokonek.ottokasir.ui.callback.SelectedItemKasbonAktif
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_kasbon_aktif_child.view.*

class KasbonAktifAdapter(var mItems: ArrayList<KasbonAktifResponseModel.DataBean>, val callback: SelectedItemKasbonAktif) : RecyclerView.Adapter<KasbonAktifAdapter.Companion.ViewHolder>() {

    private var orderIdsItemKasbon: ArrayList<String> = java.util.ArrayList()

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvDateKasbon: TextView = itemView.findViewById(R.id.tvDateKasbon)
            val tvTotalKasbon: TextView = itemView.findViewById(R.id.tvTotalKasbon)
            //val allItemSelected: CheckBox = itemView.findViewById(R.id.allItemSelected)

            var itemKasbonAktifParent: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_parent)
            var itemKasbonAktifChild: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_aktif_parent, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvDateKasbon.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "yyyy-MM-dd",
                "dd MMMM yyyy")
        holder.tvTotalKasbon.text = "Total Pay Later : " + mItems[position].total_cashbond


        holder.itemKasbonAktifParent.removeAllViews()
        holder.itemKasbonAktifChild.removeAllViewsInLayout()
        for (item in mItems[position].order!!) {
            val viewTransaction = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_kasbon_aktif_child, holder.itemKasbonAktifParent, false)
            holder.itemKasbonAktifChild.addView(viewTransaction)

            viewTransaction.tvCustomerKasbon.text = item.customer_name + " - " + item.customer_phone
            viewTransaction.tvTransactionId.text = item.code
            viewTransaction.tvTransactionTime.text = DateUtil.simplifyDateFormat(item.order_date,
                    "dd MMM yyyy | HH:mm:ss",
                    "dd MMMM yyyy | HH:mm")
            viewTransaction.tvOrderAmount.text = item.total_amount?.toDouble()?.let {
                MoneyUtil.convertCurrencyPHP1(it)
            }


            viewTransaction.itemSelected.isChecked = item.isSelected
            viewTransaction.itemSelected.tag = item

            viewTransaction.itemSelected.setOnClickListener {
                val cb = it as CheckBox
                val item = cb.tag as KasbonAktifResponseModel.DataBean.OrderBean

                item.isSelected = cb.isChecked

                if (item.isSelected) {
                    orderIdsItemKasbon.add(item.code.toString())
                    Log.e("ADD IDS", orderIdsItemKasbon.toString())


                } else if (!item.isSelected) {
                    orderIdsItemKasbon.remove(item.code.toString())
                    Log.e("REMOVE IDS", orderIdsItemKasbon.toString())
                }

                callback.orderIdsItemKasbonAktif(orderIdsItemKasbon)
                notifyDataSetChanged()

            }


            viewTransaction.tvDetail.setOnClickListener {
                //callback.clickItem(position, ArrayItemSelected)
            }

        }
        holder.itemKasbonAktifParent.addView(holder.itemKasbonAktifChild)

    }
}