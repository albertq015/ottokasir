package com.ottokonek.ottokasir.ui.activity.kasbon

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.KasbonAktifSelectedRequestModel
import com.ottokonek.ottokasir.model.api.request.KasbonCustomerDetailRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.payment.PaymentCashActivity
import com.ottokonek.ottokasir.ui.adapter.KasbonAktifCustomerAdapter
import com.ottokonek.ottokasir.ui.adapter.KasbonAktifSelectedAdapter
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.ui.callback.SelectedItemKasbonAktifCustomer
import com.ottokonek.ottokasir.ui.dialog.kasbon.PaymentMethodCallback
import com.ottokonek.ottokasir.ui.dialog.kasbon.PaymentMethodDialog
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_kasbon_detail_transaction.*
import kotlinx.android.synthetic.main.partial_cartsum.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class KasbonDetailTransactionActivity : BaseLocalActivity(), KasbonPresenter.IKasbonCustomerDetailView, KasbonPresenter.IKasbonAktifSelectedView, PaymentMethodCallback {

    private var itemsKasbonAktifSelected = ArrayList<KasbonAktifSelectedResponseModel.DataBean.OrdersBean>()
    private val kasbonPresenter = KasbonPresenter(this)

    private var kasbonAktifCustomerAdapter: KasbonAktifCustomerAdapter? = null
    private var kasbonAktifAdapter: KasbonAktifSelectedAdapter? = null

    private var totalAmount = ""
    private var customerName = ""
    private var customerPhone = ""

    private var customerIdKasbon = 0
    private var orderIdsItemKasbon: ArrayList<String> = ArrayList()

    private var activityFromKasbonCustomer = false
    private var activityFromKasbonAktif = false

    private var isFormValidationSuccess = false


    companion object {
        const val KEY_ACTIVITY_FROM_KASBON_CUSTOMER = "from_kasbon_customer"
        const val KEY_ACTIVITY_FROM_KASBON_AKTIF = "from_kasbon_aktif"
        const val KEY_ID_KASBON_CUSTOMER = "id_customer_kasbon"
        const val KEY_ORDER_IDS_KASBON_SELECTED = "order_ids_kasbon_seleceted"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kasbon_detail_transaction)

        getDataIntent()

        if (activityFromKasbonCustomer) {
            callApiKasbonCustomerDetail()
            isFormValidationSuccess = false
            clFooter.background = ContextCompat.getDrawable(this@KasbonDetailTransactionActivity,
                    R.drawable.rounded_rectangle_grey)

        } else if (activityFromKasbonAktif) {
            getKasbonAktifSelected()
        }

        contentKasbonDetail()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_ACTIVITY_FROM_KASBON_CUSTOMER)) {
                activityFromKasbonCustomer = it.getBoolean(KEY_ACTIVITY_FROM_KASBON_CUSTOMER, false)
            }

            if (it.containsKey(KEY_ACTIVITY_FROM_KASBON_AKTIF)) {
                activityFromKasbonAktif = it.getBoolean(KEY_ACTIVITY_FROM_KASBON_AKTIF, false)
            }


            if (it.containsKey(KEY_ID_KASBON_CUSTOMER)) {
                customerIdKasbon = it.getInt(KEY_ID_KASBON_CUSTOMER)
            }


            if (it.containsKey(KEY_ORDER_IDS_KASBON_SELECTED)) {
                orderIdsItemKasbon = it.getStringArrayList(KEY_ORDER_IDS_KASBON_SELECTED) as ArrayList<String>
            }

        }
    }

    override fun onResume() {
        actionCallApiKasbonType()
        super.onResume()
    }


    fun actionCallApiKasbonType() {
        getDataIntent()

        if (activityFromKasbonAktif) {
            getKasbonAktifSelected()

        }
        if (activityFromKasbonCustomer) {
            isFormValidationSuccess = false
            clFooter.background = ContextCompat.getDrawable(this@KasbonDetailTransactionActivity, R.drawable.rounded_rectangle_grey)

            totalAmount = "0"
            tv_total.text = totalAmount.toDouble().toString().let { MoneyUtil.convertCurrencyPHP1(it) }

            itemsKasbonAktifSelected.clear()
            callApiKasbonCustomerDetail()
            configureListKasbonCustomer()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun contentKasbonDetail() {
        tvTitle.text = getString(R.string.detail_transaksi)
        tv_tagihan.text = getString(R.string.total_kasbon)

        backAction.setOnClickListener {
            onBackPressed()
        }

        fl_footer.setOnClickListener {
            if (activityFromKasbonCustomer && isFormValidationSuccess) {

                val dialog = totalAmount?.let { it1 ->
                    PaymentMethodDialog(
                            this, R.style.style_bottom_sheet, it1, this)
                }
                dialog.show()

            }

            if (activityFromKasbonAktif) {
                val dialog = totalAmount?.let { it1 ->
                    PaymentMethodDialog(
                            this, R.style.style_bottom_sheet, it1, this)
                }
                dialog.show()
            }
        }
    }


    /**
     * Kasbon Customer
     * */
    private fun callApiKasbonCustomerDetail() {
        showLoading()

        val data = KasbonCustomerDetailRequestModel()
        data.customer_id = customerIdKasbon

        kasbonPresenter.onKasbonCustomerDetail(data, this)
    }


    override fun onSuccessKasbonCustomerDetail(result: KasbonAktifSelectedResponseModel) {
        hideLoading()

        for (item in result.data?.orders!!) {
            customerName = item.customer_name.toString()
            customerPhone = item.customer_phone.toString()
        }

        itemsKasbonAktifSelected.clear()
        result.data?.orders.let {
            it?.let { it1 -> itemsKasbonAktifSelected.addAll(it1) }
        }

        refreshListKasbonCustomer()
    }

    private fun configureListKasbonCustomer() {

        kasbonAktifCustomerAdapter = KasbonAktifCustomerAdapter(this, totalAmount, itemsKasbonAktifSelected, object : SelectedItemKasbonAktifCustomer {

            override fun callbackSelectedKasbonCustomer(orderIdsKasbon: ArrayList<String>, amountOrder: String) {
                orderIdsItemKasbon = orderIdsKasbon
                validationCartKasbon()

                totalAmount = amountOrder
                tv_total.text = amountOrder.toDouble().toString().let { MoneyUtil.convertCurrencyPHP1(it) }
            }
        })

        rvKasbonAktifDetail?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonAktifCustomerAdapter
        }
    }


    private fun validationCartKasbon() {
        if (orderIdsItemKasbon.isNotEmpty()) {
            isFormValidationSuccess = true
            clFooter.background = ContextCompat.getDrawable(this@KasbonDetailTransactionActivity, R.drawable.rounded_fill_blue)
        } else {
            isFormValidationSuccess = false
            clFooter.background = ContextCompat.getDrawable(this@KasbonDetailTransactionActivity, R.drawable.rounded_rectangle_grey)

        }
    }


    private fun refreshListKasbonCustomer() {
        kasbonAktifCustomerAdapter.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureListKasbonCustomer()
        }
    }


    /**
     * Kasbon Aktif
     * */
    private fun getKasbonAktifSelected() {

        val data = KasbonAktifSelectedRequestModel()
        data.order_ids = orderIdsItemKasbon

        kasbonPresenter.onKasbonAktifSelected(data, this)
    }

    override fun onSuccessKasbonAktifSelected(result: KasbonAktifSelectedResponseModel) {
        for (item in result.data?.orders!!) {
            customerName = item.customer_name.toString()
            customerPhone = item.customer_phone.toString()
        }

        itemsKasbonAktifSelected.clear()
        result.data?.orders.let {
            it?.let { it1 -> itemsKasbonAktifSelected.addAll(it1) }
        }

        totalAmount = result.data!!.total_amount.toString()
        tv_total.text = totalAmount.toDouble().toString().let {
            MoneyUtil.convertCurrencyPHP1(it)
        }
        refreshListKasbonAktif()

    }


    private fun configureListKasbonAktif() {
        if (kasbonAktifAdapter == null)
            kasbonAktifAdapter = KasbonAktifSelectedAdapter(this, itemsKasbonAktifSelected, object : ActionList {

                override fun clickItem(position: Int, value: Any) {

                }
            })

        rvKasbonAktifDetail?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonAktifAdapter
        }
    }


    private fun refreshListKasbonAktif() {
        kasbonAktifAdapter.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureListKasbonAktif()
        }
    }


    override fun onSelectMethod(method: String) {

        if (method == "Cash") {
            val intent = Intent(this, PaymentCashActivity::class.java)
            intent.putExtra(PaymentCashActivity.KEY_PAYMENT_KASBON_AKTIF, true)
            intent.putStringArrayListExtra(PaymentCashActivity.KEY_ORDER_IDS_KASBON_SELECTED, orderIdsItemKasbon)
            intent.putExtra(PaymentCashActivity.KEY_DATA_CUSTOMER_NAME, customerName)
            intent.putExtra(PaymentCashActivity.KEY_DATA_CUSTOMER_PHONE, customerPhone)
            intent.putExtra("total", totalAmount)
            startActivity(intent)

        } else if (method == "QR") {
            val bundle = Bundle()
            bundle.putSerializable(KasbonListQrActivity.KEY_ORDER_IDS_KASBON_SELECETED, orderIdsItemKasbon)
            ActivityUtil(this)
                    .sendData(bundle)
                    .showPage(KasbonListQrActivity::class.java)
        }

    }

    override fun handleError(message: String) {
        hideLoading()
        when (message) {
            "Invalid request token!" -> {
                MessageUserUtil.shortMessage(this, message)
                SessionManager.logoutDevice(this)
            }
            else -> {
                if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
                    MessageUserUtil.shortMessage(this, getString(R.string.tidak_ada_koneksi))
                } else {
                    MessageUserUtil.shortMessage(this, message)
                }
            }
        }
    }
}