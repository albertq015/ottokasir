package com.ottokonek.ottokasir.ui.activity.payment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.widget.EditText
import android.widget.Toast
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_CASH
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_KASBON_CASH
import com.ottokonek.ottokasir.IConfig.Companion.PAYMENT_TYPE
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.model.api.request.KasbonCashPaymentRequestModel
import com.ottokonek.ottokasir.model.api.request.PaymentCashRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonCashPaymentResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentCashResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.receipt.ReceiptActivity
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.CommonUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_payment_cash.*
import kotlinx.android.synthetic.main.partial_customer_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*


class PaymentCashActivity : BaseLocalActivity(), PaymentCashIView, KasbonPresenter.IKasbonCashPaymentView {

    private var orderIdsItemKasbon: ArrayList<String>? = ArrayList()
    private var paymentKasbonAktif = false
    private var totalAmount: String? = null
    private var customerId = 0
    private var customerName = ""
    private var customerPhone = ""

    private lateinit var presenter: OrderPresenter
    private val kasbonPresenter = KasbonPresenter(this)

    var nominalValue = "0.0"
    private var mTextWatcher: TextWatcher? = null

    companion object {
        const val KEY_DATA_CUSTOMER_ID = "key_data_customer_id"
        const val KEY_DATA_CUSTOMER_NAME = "key_data_customer_name"
        const val KEY_DATA_CUSTOMER_PHONE = "key_data_customer_phone"
        const val KEY_PAYMENT_KASBON_AKTIF = "key_payment_kasbon_aktif"
        const val KEY_ORDER_IDS_KASBON_SELECTED = "order_ids_kasbon_selected"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_cash)

        /*etNominal.monetaryDivider = ','
        etNominal.groupDivider = ','
        etNominal.locale*/
        addTextWatcherPrice(etNominal)

        presenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter
        initView()

        getDataIntent()
    }

    @SuppressLint("SetTextI18n")
    private fun getDataIntent() {
        paymentKasbonAktif = intent.getBooleanExtra(KEY_PAYMENT_KASBON_AKTIF, false)
        orderIdsItemKasbon = intent.getStringArrayListExtra(KEY_ORDER_IDS_KASBON_SELECTED)

        if (paymentKasbonAktif) {
            tvTitle.text = getString(R.string.nominal_kasbon)
            tvTitleTotal.text = getString(R.string.total_kasbon)
            totalAmount = intent.getStringExtra("total")
            tvTotalPurchase.text = MoneyUtil.convertIDRCurrencyFormat(totalAmount.toString().toDouble())
        } else {
            tvTitle.text = getString(R.string.nominal_pembayaran)
            tvTotalPurchase.text = intent.getStringExtra("total")
        }
        tvCancel.text = getString(R.string.batal)
        customerId = intent.getIntExtra(KEY_DATA_CUSTOMER_ID, 0)
        customerName = intent?.getStringExtra(KEY_DATA_CUSTOMER_NAME)!!
        customerPhone = intent?.getStringExtra(KEY_DATA_CUSTOMER_PHONE)!!

        if (customerName != "" && customerPhone != "") {
            etCustomer.setText("$customerName - $customerPhone")
        } else {
            ly_customer.visibility = View.GONE
        }

    }


    override fun onPause() {
        super.onPause()
        presenter.onDestroy()
    }


    private fun initView() {
        tvUangPas.setBackgroundResource(R.drawable.rounded_fill_green)
        tvUangPas.setTextColor(resources.getColor(R.color.white))

        btnPay.setOnClickListener {
            showLoading()

            if (paymentKasbonAktif) {
                if (etNominal.text!!.equals("")) {
                    etNominal.setText("0")
                }
                var model = PaymentCashRequestModel()
                if (fl_nominal.visibility == View.VISIBLE) {
                    if (nominalValue.toDouble() < totalAmount?.toDouble()!!) {
                        hideLoading()
                        Toast.makeText(this, getString(R.string.uang_pembayaran_harus_lebih_besar_dari_total_pembayaran), Toast.LENGTH_LONG).show()
                    } else {
                        getKasbonCashPaymentUangLebih()
                    }
                } else {
                    getKasbonCashPaymentUangPas()
                }

            } else {
                if (etNominal.text!!.equals("")) {
                    etNominal.setText("0")
                }
                var model = PaymentCashRequestModel()
                if (fl_nominal.visibility == View.VISIBLE) {
                    if (nominalValue.toDouble() < presenter.getTotalPrice()) {
                        hideLoading()
                        Toast.makeText(this, getString(R.string.uang_pembayaran_harus_lebih_besar_dari_total_pembayaran), Toast.LENGTH_LONG).show()
                    } else {
                        model = presenter.createOrderCash(nominalValue.toString(), presenter.getTotalPrice().toString(), "Cash", customerId)
                        presenter.onPaymentCash(this, model)
                    }
                } else {
                    model = presenter.createOrderCash(presenter.getTotalPrice().toString(), presenter.getTotalPrice().toString(), "Cash", customerId)
                    presenter.onPaymentCash(this, model)
                }
            }
        }

        backAction.setOnClickListener {
            onBackPressed()
        }
        tvCancel.setOnClickListener {
            CartManager.removeAllCartItem()
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvUangPas.setOnClickListener {
            tvLainnya.setBackgroundResource(R.drawable.rounded_border_green_4dp)
            tvLainnya.setTextColor(resources.getColor(R.color.colorPrimary))

            CommonUtil.hideKeyboard(this)
            separator.visibility = View.GONE
            fl_nominal.visibility = View.GONE

            tvUangPas.setBackgroundResource(R.drawable.rounded_fill_green)
            tvUangPas.setTextColor(resources.getColor(R.color.white))
        }

        tvLainnya.setOnClickListener {
            tvUangPas.setBackgroundResource(R.drawable.rounded_border_green_4dp)
            tvUangPas.setTextColor(resources.getColor(R.color.colorPrimary))

            separator.visibility = View.VISIBLE
            fl_nominal.visibility = View.VISIBLE

            tvLainnya.setBackgroundResource(R.drawable.rounded_fill_green)
            tvLainnya.setTextColor(resources.getColor(R.color.white))

            etNominal.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etNominal, SHOW_IMPLICIT)
        }
    }


    override fun handleSuccess(data: PaymentCashResponseModel) {
        hideLoading()
        if (data.meta != null) {
            if (data.meta!!.isStatus) {
                val intent = Intent(this, ReceiptActivity::class.java)
                intent.putExtra(KEY_PAYMENT_CASH, data.data)
                intent.putExtra(PAYMENT_TYPE, KEY_PAYMENT_CASH)
                startActivity(intent)
            } else {
                Toast.makeText(this, data.meta!!.message, Toast.LENGTH_LONG).show()
                hideLoading()
            }
        }
    }


    /**
     * Call Api Kasbon Cash Payment
     * */
    private fun getKasbonCashPaymentUangLebih() {
        showLoading()

        val totalPaid = nominalValue.toDouble()
        val change = totalPaid - totalAmount!!.toDouble()

        val data = KasbonCashPaymentRequestModel()
        data.order_ids = orderIdsItemKasbon
        data.total_paid = totalPaid
        data.change = change


        kasbonPresenter.onKasbonCashPayment(data, this)
    }

    private fun getKasbonCashPaymentUangPas() {
        showLoading()

        val data = KasbonCashPaymentRequestModel()
        data.order_ids = orderIdsItemKasbon
        data.total_paid = totalAmount!!.toDouble()
        data.change = 0.0


        kasbonPresenter.onKasbonCashPayment(data, this)
    }

    override fun onSuccessKasbonCashPayment(result: KasbonCashPaymentResponseModel) {
        hideLoading()

        val bundle = Bundle()
        bundle.putString(PAYMENT_TYPE, KEY_PAYMENT_KASBON_CASH)
        bundle.putSerializable(ReceiptActivity.KEY_ITEMS_KASBON_PAYMENT_CASH, result.data)
        ActivityUtil(this)
                .sendData(bundle)
                .showPage(ReceiptActivity::class.java)
    }


    /*override fun handleError(message: String) {
        hideLoading()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }*/

    override fun handleError(message: String) {
        super.handleError(message)
    }

    override fun onApiFailed(title: String, error: String) {
        super.onApiFailed(title, error)
    }

    override fun logout() {
        super.logout()
    }

    private fun addTextWatcherPrice(input: EditText) {
        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val amount = MoneyUtil.InputDecimal(charSequence.toString())

                etNominal.removeTextChangedListener(mTextWatcher)

                if (amount.equals("")) {
                    etNominal.setText("")
                } else {
                    etNominal.setText(amount)
                }

                etNominal.addTextChangedListener(mTextWatcher)
                nominalValue = MoneyUtil.CurrencyToDouble(amount).toString()
                //Toast.makeText(this@EditProductActivity, priceValue.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun afterTextChanged(editable: Editable) {
                etNominal.setSelection(etNominal.text.length)

            }
        }
        input.addTextChangedListener(mTextWatcher)
    }
}
