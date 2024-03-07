package com.ottokonek.ottokasir.ui.activity.kasbon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.KasbonAktifSelectedRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.payment.PaymentQRActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.adapter.KasbonAktifQrAdapter
import com.ottokonek.ottokasir.ui.callback.SelectedItemQr
import kotlinx.android.synthetic.main.activity_kasbon_list_qr.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class KasbonListQrActivity : BaseLocalActivity(), KasbonPresenter.IKasbonAktifSelectedView {

    private val kasbonPresenter = KasbonPresenter(this)
    private var itemsKasbonAktifSelected = ArrayList<KasbonAktifSelectedResponseModel.DataBean.OrdersBean>()
    private var kasbonAdapter: KasbonAktifQrAdapter? = null

    private var orderIdsItemKasbon: ArrayList<String> = ArrayList()
    private var merchantName: String? = null


    companion object {
        const val KEY_ORDER_IDS_KASBON_SELECETED = "order_ids_kasbon_seleceted"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kasbon_list_qr)

        getDataIntent()
        getKasbonAktifSelected()
        configureList()
        contentKasbonListQr()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_ORDER_IDS_KASBON_SELECETED)) {
                orderIdsItemKasbon = it.getStringArrayList(KEY_ORDER_IDS_KASBON_SELECETED) as ArrayList<String>
            }
        }
    }

    private fun contentKasbonListQr() {
        tvTitle.text = getString(R.string.daftar_transaksi)

        backAction.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        getKasbonAktifSelected()
        super.onResume()
    }

    private fun getKasbonAktifSelected() {
        showLoading()

        val data = KasbonAktifSelectedRequestModel()
        data.order_ids = orderIdsItemKasbon

        kasbonPresenter.onKasbonAktifSelected(data, this)
    }


    override fun onSuccessKasbonAktifSelected(result: KasbonAktifSelectedResponseModel) {
        hideLoading()

        for (item in result.data?.orders!!) {
            merchantName = item.store_name
        }

        itemsKasbonAktifSelected.clear()
        result.data?.orders.let {
            it?.let { it1 -> itemsKasbonAktifSelected.addAll(it1) }
        }
        refreshList()

        if (itemsKasbonAktifSelected.isEmpty()) {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }


    private fun configureList() {
        if (kasbonAdapter == null)
            kasbonAdapter = KasbonAktifQrAdapter(this, itemsKasbonAktifSelected, object : SelectedItemQr {


                override fun orderIdsSelected(orderIds: String, totalAmount: String) {

                    val intent = Intent(this@KasbonListQrActivity, PaymentQRActivity::class.java)
                    intent.putExtra(PaymentQRActivity.KEY_TOTAL_AMOUNT, totalAmount)
                    intent.putExtra(PaymentQRActivity.KEY_MERCHANT_NAME, merchantName)
                    intent.putExtra(PaymentQRActivity.KEY_ORDER_IDS_KASBON_SELECETED, orderIds)
                    intent.putExtra(PaymentQRActivity.KEY_PAYMENT_KASBON_AKTIF, true)
                    startActivityForResult(intent, IConfig.REQUEST_KASBON_QR)
                }

            })

        rvKasbonAktifQr?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonAdapter
        }
    }

    private fun refreshList() {
        kasbonAdapter.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureList()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IConfig.REQUEST_KASBON_QR) {

        }
    }


}