package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.item_list_kasbon_aktif_selected_child.view.*

class KasbonAktifSelectedAdapter(val context: Context, var mItems: ArrayList<KasbonAktifSelectedResponseModel.DataBean.OrdersBean>, val callback: ActionList) : RecyclerView.Adapter<KasbonAktifSelectedAdapter.Companion.ViewHolder>() {


    companion object {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val tvKasbonCode: TextView = itemView.findViewById(R.id.tvKasbonCode)
            val tvKasbonDate: TextView = itemView.findViewById(R.id.tvKasbonDate)

            var itemKasbonAktifSelectedParent: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_selected_parent)
            var itemKasbonAktifSelectedChild: ViewGroup = itemView.findViewById(R.id.item_kasbon_aktif_selected_child)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_kasbon_aktif_selected_parent, parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvKasbonCode.text = mItems[position].code
        holder.tvKasbonDate.text = DateUtil.simplifyDateFormat(mItems[position].order_date,
                "dd MMM yyyy | HH:mm:ss",
                "dd MMMM yyyy | HH:mm")

        holder.itemKasbonAktifSelectedParent.removeAllViews()
        holder.itemKasbonAktifSelectedChild.removeAllViewsInLayout()


        for (items in mItems[position].items!!) {
            val holderItems = LayoutInflater.from(holder.itemView.context).inflate(R.layout.item_list_kasbon_aktif_selected_child, holder.itemKasbonAktifSelectedParent, false)
            holder.itemKasbonAktifSelectedChild.addView(holderItems)

            Glide.with(context)
                    .load(items.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder_product)
                    .into(holderItems.ivProduct)
            holderItems.tvProductName.text = items.item_name
            holderItems.tvProductPrice.text = items.product_price?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }
            holderItems.tvQuantity.text = " x " + items.quantity
            holderItems.tvPrice.text = items.price?.toDouble()?.let { MoneyUtil.convertIDRCurrencyFormat(it) }

        }
        holder.itemKasbonAktifSelectedParent.addView(holder.itemKasbonAktifSelectedChild)
    }

}