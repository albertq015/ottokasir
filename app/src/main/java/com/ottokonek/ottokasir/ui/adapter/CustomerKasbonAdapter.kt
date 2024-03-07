package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.CustomerKasbonResponseModel
import com.ottokonek.ottokasir.ui.activity.receipt.DetailReceiptActivity
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_customer_kasbon_child.view.*


class CustomerKasbonAdapter(val context: Context, var data: List<CustomerKasbonResponseModel.DataBean>, val callback: ActionList) : RecyclerView.Adapter<CustomerKasbonAdapter.Companion.ViewHolder>() {

    private var statusKasbon = ""

    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            var itemCustomerKasbonParent: ViewGroup = itemView.findViewById(R.id.item_customer_kasbon_parent)
            var itemCustomerKasbonChild: ViewGroup = itemView.findViewById(R.id.item_customer_kasbon_child)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_customer_kasbon_parent, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val statusGroupKasbon = data[position].name
        if (statusGroupKasbon!!.contains("Unpaid") || statusGroupKasbon.contains("Belum Lunas")) {
            holder.tvTitle.text = "Outstanding" + context.getString(R.string.kasbon_spasi_first) + " : " + data[position].transactions!!.size
        } else {
            holder.tvTitle.text = data[position].name + context.getString(R.string.kasbon_spasi_first) + " : " + data[position].transactions!!.size
        }


        holder.itemCustomerKasbonParent.removeAllViews()
        holder.itemCustomerKasbonChild.removeAllViewsInLayout()

        for (item in data[position].transactions!!) {
            val viewTransaction = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_customer_kasbon_child, holder.itemCustomerKasbonChild, false)
            holder.itemCustomerKasbonChild.addView(viewTransaction)

            viewTransaction.tvTransactionId.text = item.code
            viewTransaction.tvPaymentMethod.text = item.payment_method
            viewTransaction.tvOrderAmount.text = item.total_amount?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }

            statusKasbon = item.status_cashbond.toString()
            if (statusKasbon.contains("Unpaid") || statusKasbon.contains("Belum Lunas")) {
                viewTransaction.tvDetail.text = context.getString(R.string.belum_lunas)
                viewTransaction.tvTransactionTime.text = DateUtil.simplifyDateFormat(item.order_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMMM yyyy | HH:mm")

            } else {
                viewTransaction.tvTransactionTime.text = DateUtil.simplifyDateFormat(item.cashbond_payment_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMMM yyyy | HH:mm")

                viewTransaction.tvDetail.text.apply {
                    viewTransaction.tvDetail.text = context.getString(R.string.lunas)
                    viewTransaction.tvDetail.apply {
                        context?.let {
                            setTextColor(
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                        it.resources.getColor(R.color.soft_green, null)
                                    else
                                        it.resources.getColor(R.color.soft_green)
                            )
                        }
                    }
                }
            }


            if (statusKasbon == "Lunas") {
                viewTransaction.tvDetail.setOnClickListener {
                    val intent = Intent(context, DetailReceiptActivity::class.java)
                    intent.putExtra(DetailReceiptActivity.KEY_PAYMENT_KASBON_LUNAS_CUSTOMER, item)
                    (context as Activity).startActivity(intent)
                }
            }


        }

        holder.itemCustomerKasbonParent.addView(holder.itemCustomerKasbonChild)
    }


}
