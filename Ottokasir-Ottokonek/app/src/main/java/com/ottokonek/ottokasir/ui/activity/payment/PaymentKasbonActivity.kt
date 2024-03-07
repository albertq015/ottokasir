package com.ottokonek.ottokasir.ui.activity.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.model.api.request.PaymentCashBondRequestModel
import com.ottokonek.ottokasir.model.api.response.PaymentCashBondResponseModel
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.receipt.ReceiptActivity
import kotlinx.android.synthetic.main.activity_payment_cash.*
import kotlinx.android.synthetic.main.partial_customer_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class PaymentKasbonActivity : BaseLocalActivity(), PaymentCashBondIView {

    private lateinit var presenter: OrderPresenter
    private var customerId = 0

    companion object {
        const val KEY_DATA_CUSTOMER_ID = "key_data_customer_id"
        const val KEY_DATA_CUSTOMER_NAME = "key_data_customer_name"
        const val KEY_DATA_CUSTOMER_PHONE = "key_data_customer_phone"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_kasbon)

        presenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter

        getDataIntent()
        contentPaymentKasbon()
        actionPaymentKasbon()
    }

    @SuppressLint("SetTextI18n")
    private fun getDataIntent() {
        tvTotalPurchase.text = intent.getStringExtra("total")

        customerId = intent.getIntExtra(KEY_DATA_CUSTOMER_ID, 0)
        val customerName = intent.getStringExtra(KEY_DATA_CUSTOMER_NAME)
        val customerPhone = intent.getStringExtra(KEY_DATA_CUSTOMER_PHONE)
        etCustomer.setText("$customerName - $customerPhone")
    }

    private fun contentPaymentKasbon() {
        tvTitle.text = getString(R.string.nominal_kasbon)
        tvCancel.text = getString(R.string.batal)
        tvTitleTotal.text = getString(R.string.total_kasbon)
    }


    private fun actionPaymentKasbon() {
        backAction.setOnClickListener {
            onBackPressed()
        }

        tvCancel.setOnClickListener {
            CartManager.removeAllCartItem()
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnPay.setOnClickListener {
            showLoading()

            var model = PaymentCashBondRequestModel()
            model = presenter.createOrderCashBond(presenter.getTotalPrice().toString(), "CashBond", customerId)
            presenter.onPaymentCashBond(this, model)

        }

    }

    override fun handleProcessing() {

    }

    override fun handleSuccess(data: PaymentCashBondResponseModel) {
        hideLoading()
        if (data.meta != null) {
            if (data.meta!!.isStatus) {
                var intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra(IConfig.KEY_PAYMENT_KASBON, data.data)
                intent.putExtra(IConfig.PAYMENT_TYPE, IConfig.KEY_PAYMENT_KASBON)
                startActivity(intent)
            } else {

                Toast.makeText(this, data.meta!!.message, Toast.LENGTH_LONG).show()
                hideLoading()
            }
        }
    }

}