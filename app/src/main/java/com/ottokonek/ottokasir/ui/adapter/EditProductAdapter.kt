package com.ottokonek.ottokasir.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import app.beelabs.com.codebase.support.util.CacheUtil
import com.bumptech.glide.Glide
import com.ottokonek.ottokasir.IConfig.Companion.IS_ADD_PRODUCT
import com.ottokonek.ottokasir.IConfig.Companion.PRODUCT_DATA
import com.ottokonek.ottokasir.IConfig.Companion.PRODUCT_TYPE
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.edit_product.EditProductActivity
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import com.ottokonek.ottokasir.ui.fragment.manage_product.ManageProductFragment
import com.ottokonek.ottokasir.utils.MoneyUtil
import com.ottokonek.ottokasir.utils.NetworkConnection
import kotlinx.android.synthetic.main.item_list_product_edit.view.*


class EditProductAdapter(val fragment: ManageProductFragment) : RecyclerView.Adapter<EditProductAdapter.mViewHolder>() {

    var products: MutableList<ProductItemModel> = mutableListOf()
    lateinit var context: Context

    private var stockAlert = 0
    private var stockProduct = 0
    private var stockActive = false

    class mViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEdit = itemView.tv_edit
        val tvPrice = itemView.tv_price
        val tvProductName = itemView.tv_productname
        val tvStock = itemView.tv_stock
        val ivProduct = itemView.iv_product
        val tvStocksAlert = itemView.tv_stock_alert
        val ivBarcodeProduct = itemView.iv_barcode_product
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_list_product_edit, parent, false)
        return mViewHolder(view)

    }

    override fun getItemCount(): Int {
        return products.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        val data = products[position]

        holder.tvEdit.setOnClickListener {
            //if(isNetworkConnected()) {
              //  CustomProgressDialog.showDialog(context, "Loading")
                stockProduct = data.stocks
                stockAlert = data.stock_alert
                stockActive = data.isStock_active
                SessionManager.putStocks(stockProduct, context)
                SessionManager.putStocksAlert(stockAlert, context)
                SessionManager.putIsStockActive(stockActive, context)

            val handler = Handler()
            handler.postDelayed({
                val intent = Intent(context, EditProductActivity::class.java)
                intent.putExtra(PRODUCT_DATA, data)
                intent.putExtra(IS_ADD_PRODUCT, false)
                CacheUtil.putPreferenceString(PRODUCT_TYPE, data.product_type, context)
                fragment.startActivity(intent)
            }, 1000)


            //fragment.startActivityForResult(intent, IConfig.EDIT_PRODUCT_CODE)
           // }
        }

        if (data.price == "-1.00") {
            holder.tvPrice.setText(R.string.belum_ada_harga)
        } else {
            holder.tvPrice.text = MoneyUtil.convertCurrencyPHP1((data.price).toDouble())
        }

        if (data.barcode != "") {
            holder.ivBarcodeProduct.visibility = View.VISIBLE
        } else {
            holder.ivBarcodeProduct.visibility = View.GONE
        }

        if (data.stocks > data.stock_alert && data.isStock_active) {
            holder.tvStocksAlert.visibility = View.GONE
            holder.tvStock.visibility = View.VISIBLE
            holder.tvStock.text = context.getString(R.string.stok_spasi) + data.stocks
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tvStock.setTextColor(context.getColor(R.color.green_two))
            } else {
                holder.tvStock.setTextColor(context.resources.getColor(R.color.green_two))
            }

        } else if (data.stocks > 0 && data.stocks <= data.stock_alert && data.isStock_active) {
            holder.tvStocksAlert.visibility = View.VISIBLE
            holder.tvStocksAlert.text = context.getString(R.string.stok_menipis)
            holder.tvStock.text = context.getString(R.string.stok_spasi) + data.stocks
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tvStock.setTextColor(context.getColor(R.color.red))
            } else {
                holder.tvStock.setTextColor(context.resources.getColor(R.color.red))
            }

        } else if (data.stocks == 0 && data.isStock_active) {
            holder.tvStocksAlert.visibility = View.VISIBLE
            holder.tvStocksAlert.text = context.getString(R.string.stok_habis)
            holder.tvStock.text = context.getString(R.string.stok_spasi) + data.stocks
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tvStock.setTextColor(context.getColor(R.color.red))
            } else {
                holder.tvStock.setTextColor(context.resources.getColor(R.color.red))
            }
        } else if (!data.isStock_active) {
            holder.tvStocksAlert.visibility = View.GONE
            holder.tvStock.visibility = View.GONE
            /*holder.tvStock.text = context.getString(R.string.stok_)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.tvStock.setTextColor(context.getColor(R.color.green_two))
            } else {
                holder.tvStock.setTextColor(context.resources.getColor(R.color.green_two))
            }*/
        }



        holder.tvProductName.text = data.name

        //holder.tvStock.text = "Stock : " + data.stocks

        Glide.with(context)
                .load(data.image)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder_product)
                .into(holder.ivProduct)
    }

    fun onUpdateProduct(data: MutableList<ProductItemModel>) {
        products = data
        notifyDataSetChanged()
    }


    private fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }


    private fun checkConnection():Boolean {



        val networkConnection = NetworkConnection(context)
        var returnVal:Boolean = false
        networkConnection.observe(fragment, Observer { isConnected ->

            if (!isConnected) {
                CustomProgressDialog.closeDialog()
                returnVal = false
            }
            else{
                returnVal = true
            }

        })
        return returnVal
    }
}