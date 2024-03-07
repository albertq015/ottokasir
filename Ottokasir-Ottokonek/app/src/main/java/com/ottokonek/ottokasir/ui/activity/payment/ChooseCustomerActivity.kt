package com.ottokonek.ottokasir.ui.activity.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.IConfig.Companion.REQUEST_PAYMENT_QR
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.model.api.request.CustomerListRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerListResponseModel
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.customer.CustomerCreateActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.adapter.CustomerChooseAdapter
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.ui.fragment.ProductFragment
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.activity_choose_customer.*
import kotlinx.android.synthetic.main.toolbar.*

class ChooseCustomerActivity : BaseLocalActivity(), CustomerPresenter.ICustomerListView {


    private val customerPresenter = CustomerPresenter(this)
    private val mItems: ArrayList<CustomerModel> = ArrayList()
    private var adapterList: CustomerChooseAdapter? = null
    private var formValidation: Boolean = false

    private var paymentCash = false
    private var paymentQris = false
    private var paymentKasbon = false

    private var keyWord = ""
    private var customerName = ""
    private var customerPhone = ""
    private var customerId = 0

    companion object {
        const val KEY_PAYMENT_CASH = "key_payment_cash"
        const val KEY_PAYMENT_QRIS = "key_payment_qris"
        const val KEY_PAYMENT_KASBON = "key_payment_kasbon"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_customer)

        getDataIntent()
        setEditTextWatcher()
        getCustomerList()

        contentChooseCustomer()
        actionChooseCustomer()
    }

    override fun onResume() {
        super.onResume()
    getCustomerList()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            when {
                it.containsKey(KEY_PAYMENT_CASH) -> {
                    paymentCash = it.getBoolean(KEY_PAYMENT_CASH)
                }
                it.containsKey(KEY_PAYMENT_QRIS) -> {
                    paymentQris = it.getBoolean(KEY_PAYMENT_QRIS)
                }
                it.containsKey(KEY_PAYMENT_KASBON) -> {
                    paymentKasbon = it.getBoolean(KEY_PAYMENT_KASBON)
                }
            }
        }
    }


    private fun contentChooseCustomer() {
        tvTitle.visibility = View.VISIBLE
        tvTitle.text = getString(R.string.pilih_pelanggan)

        btnNext.setBackgroundResource(R.drawable.rounded_rectangle_grey)
        addTextWatcher(etCustomer)

        if (paymentKasbon) {
            tvCancel.visibility = View.GONE
        } else {
            tvCancel.visibility = View.VISIBLE
            tvCancel.text = getString(R.string.lewati)
        }
    }


    private fun actionChooseCustomer() {

        backAction.setOnClickListener {
            onBackPressed()
        }

        btnAddCustomer.setOnClickListener {
            val intent = Intent(this, CustomerCreateActivity::class.java)
            intent.putExtra(CustomerCreateActivity.KEY_FROM_CHOOSE_CUSTOMER, true)
            startActivityForResult(intent, IConfig.ADD_CHOOSE_CUSTOMER)
        }

        tvCancel.setOnClickListener {
            when {
                paymentCash -> {
                    val sendData = Bundle()
                    sendData.putString(PaymentCashActivity.KEY_DATA_CUSTOMER_NAME, "")
                    sendData.putString(PaymentCashActivity.KEY_DATA_CUSTOMER_PHONE, "")
                    sendData.putString("total", ProductFragment.price)
                    ActivityUtil(this)
                            .sendData(sendData)
                            .showPage(PaymentCashActivity::class.java)
                }
                paymentQris -> {
                    val intent = Intent(this, PaymentQRActivity::class.java)
                    intent.putExtra("total", ProductFragment.price)
                    startActivityForResult(intent, REQUEST_PAYMENT_QR)
                }
                /*paymentKasbon -> {
                    val sendData = Bundle()
                    sendData.putInt(PaymentKasbonActivity.KEY_DATA_CUSTOMER_ID, customerId)
                    sendData.putString(PaymentKasbonActivity.KEY_DATA_CUSTOMER_NAME, customerName)
                    sendData.putString(PaymentKasbonActivity.KEY_DATA_CUSTOMER_PHONE, customerPhone)
                    sendData.putString("total", ProductFragment.price)
                    ActivityUtil(this)
                            .sendData(sendData)
                            .showPage(PaymentKasbonActivity::class.java)
                }*/
            }
        }

        btnNext.setOnClickListener {
            if (customerPhone != "" && customerName != "") {
                when {
                    paymentCash -> {
                        val sendData = Bundle()
                        sendData.putInt(PaymentCashActivity.KEY_DATA_CUSTOMER_ID, customerId)
                        sendData.putString(PaymentCashActivity.KEY_DATA_CUSTOMER_NAME, customerName)
                        sendData.putString(PaymentCashActivity.KEY_DATA_CUSTOMER_PHONE, customerPhone)
                        sendData.putString("total", ProductFragment.price)
                        ActivityUtil(this)
                                .sendData(sendData)
                                .showPage(PaymentCashActivity::class.java)
                    }
                    paymentQris -> {
                        val intent = Intent(this, PaymentQRActivity::class.java)
                        intent.putExtra(PaymentQRActivity.KEY_DATA_CUSTOMER_NAME, customerName)
                        intent.putExtra(PaymentQRActivity.KEY_DATA_CUSTOMER_ID, customerId)
                        intent.putExtra("total", ProductFragment.price)
                        startActivityForResult(intent, REQUEST_PAYMENT_QR)

                    }
                    paymentKasbon -> {
                        if (customerPhone.isEmpty() && customerName.isEmpty()) {
                            Toast.makeText(this, "Data customer tidak valid", Toast.LENGTH_SHORT).show()
                        } else {
                            val sendData = Bundle()
                            sendData.putInt(PaymentKasbonActivity.KEY_DATA_CUSTOMER_ID, customerId)
                            sendData.putString(PaymentKasbonActivity.KEY_DATA_CUSTOMER_NAME, customerName)
                            sendData.putString(PaymentKasbonActivity.KEY_DATA_CUSTOMER_PHONE, customerPhone)
                            sendData.putString("total", ProductFragment.price)
                            ActivityUtil(this)
                                    .sendData(sendData)
                                    .showPage(PaymentKasbonActivity::class.java)
                        }
                    }
                }
            }
        }

        iv_clear.setOnClickListener {
            etCustomer.setText("")
        }

    }

    private fun setEditTextWatcher() {

        etCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                keyWord = etCustomer.text.toString()

                if (etCustomer.text.toString() != "") {
                    getCustomerList()
                    rvCustomer.visibility = View.VISIBLE
                    iv_clear.visibility = View.VISIBLE
                } else if (etCustomer.text.toString() == "") {
                    getCustomerList()
                    rvCustomer.visibility = View.VISIBLE
                    iv_clear.visibility = View.VISIBLE
                } else {
                    rvCustomer.visibility = View.GONE
                    iv_clear.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun validationCustomer() {
        if (customerPhone != "" && customerName != "") {
            formValidation = true
            btnNext.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_blue)
        } else {
            formValidation = false
            btnNext.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_grey)
        }
    }


    private fun addTextWatcher(input: EditText) {
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                validationCustomer()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }


    private fun configureList() {
        if (adapterList == null)
            adapterList = CustomerChooseAdapter(mItems, object : ActionList {

                @SuppressLint("SetTextI18n")
                override fun clickItem(position: Int, value: Any) {
                    val items = value as CustomerModel

                    customerId = items.id
                    customerName = items.fullName
                    customerPhone = items.noHandphone

                    rvCustomer.visibility = View.VISIBLE
                    etCustomer.setText(items.fullName + " - " + items.noHandphone)

                }
            })

        rvCustomer?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = adapterList
        }
    }

    private fun refreshList() {
        adapterList.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureList()
        }
    }

    /**
     * Start Call Api Customer List
     * */
    private fun getCustomerList() {
        //showLoading()

        val data = CustomerListRequestModel()
        data.sorting = ""
        data.keyword = keyWord

        customerPresenter.onCustomerList(data, this)
    }

    override fun onSuccessCustomerList(result: MutableList<CustomerModel>, resultOri: CustomerListResponseModel) {
        hideLoading()

        mItems.clear()
        mItems.addAll(result)

        refreshList()
    }

    /**
     * End Call Api Customer List
     * */

    override fun handleError(message: String) {
        hideLoading()
        if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
            MessageUserUtil.toastMessage(this, getString(R.string.tidak_ada_koneksi))

        } else {
            MessageUserUtil.toastMessage(this, message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PAYMENT_QR && resultCode == Activity.RESULT_OK) {
            CartManager.removeAllCartItem()
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}