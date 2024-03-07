package com.ottokonek.ottokasir.ui.activity.store_type

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductResponseModel

class StoreTypeProductAdapter(
        private val context: Context,
        private var allProductsIds: ArrayList<Int>,
        private val item_list: ArrayList<StoreTypeProductResponseModel.DataBean>,
        private val storeTypeIView: StoreTypeIView) : RecyclerView.Adapter<StoreTypeProductAdapter.ViewHolder>() {


    private var productsIds: ArrayList<Int> = ArrayList()
    private var productIDS: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_recommendation_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.tvStoreType.text = item_list[position].name
        viewHolder.cbSelectProduct.isChecked = item_list[position].isSelected
        viewHolder.cbSelectProduct.tag = item_list[position]

        viewHolder.cbSelectProduct.setOnClickListener { v: View ->
            val cb = v as CheckBox
            val model = cb.tag as StoreTypeProductResponseModel.DataBean

            productsIds.addAll(allProductsIds)
            model.isSelected = cb.isChecked
            item_list[position].isSelected = cb.isChecked

            if (model.isSelected) {
                productsIds.add(item_list[position].id)
                Log.e("IDS TRUE 1", productsIds.toString())
                /*notifyDataSetChanged()
                storeTypeIView.selectedItem(productsIds)*/

            } else if (!model.isSelected) {
                productsIds.remove(item_list[position].id)
                Log.e("IDS FALSE 1", productsIds.toString())
                /*notifyDataSetChanged()
                storeTypeIView.selectedItem(productsIds)*/
            }

            storeTypeIView.selectedItem(productsIds)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return item_list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cbSelectProduct: CheckBox = itemView.findViewById(R.id.cbSelectProduct)
        val tvStoreType: TextView = itemView.findViewById(R.id.tvStoreType)

    }

}