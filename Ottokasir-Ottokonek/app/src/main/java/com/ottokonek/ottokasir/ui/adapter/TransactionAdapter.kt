package com.ottokonek.ottokasir.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_HISTORY_ALL
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.db.TransactionModel
import com.ottokonek.ottokasir.ui.activity.receipt.DetailReceiptActivity
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_transaction_list.view.*

class TransactionAdapter(val context: Context) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    //    var listData: ArrayList<TransactionModel> = listHistory;
    private var listData = mutableListOf<TransactionModel>()
    private var totalItem = 0

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TransactionAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_transaction_list, p0, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return totalItem
    }

    fun setData(model: MutableList<TransactionModel>, count: Int) {
        listData = model
        totalItem = count
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: TransactionModel = listData[position]
        holder.tvTransactionId.text = data.orderID
        holder.tvTransactionTime.text = DateUtil.simplifyDateFormat(data.transactionDate,
                "yyyy-MM-dd HH:mm:ss",
                "dd MMMM yyyy | HH:mm")
        holder.tvPaymentType.text = data.paymentType
        holder.tvOrderAmount.text = MoneyUtil.convertCurrencyPHP1(data.amount)


        if (data.products != "") {
            holder.tvDetail.setOnClickListener {
                val intent = Intent(context, DetailReceiptActivity::class.java)
                intent.putExtra(KEY_PAYMENT_HISTORY_ALL, data.changeToParcelable())
                (context as Activity).startActivity(intent)
            }
        }
        /*else {
            holder.tvDetail.text = "Sudah di Refund"
        }*/
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTransactionId = itemView.tvCustomerKasbon
        var tvTransactionTime = itemView.tvTransactionTime
        var tvPaymentType = itemView.tvTransactionId
        var tvOrderAmount = itemView.tvOrderAmount
        var tvDetail = itemView.tvDetail
    }


}