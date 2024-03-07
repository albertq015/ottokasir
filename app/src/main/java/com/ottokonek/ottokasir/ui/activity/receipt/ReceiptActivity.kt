package com.ottokonek.ottokasir.ui.activity.receipt

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_CASH
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_KASBON
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_KASBON_CASH
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_QR
import com.ottokonek.ottokasir.IConfig.Companion.KEY_PAYMENT_KASBON_QR
import com.ottokonek.ottokasir.IConfig.Companion.PAYMENT_TYPE
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.QrPaymentGenerateRequestModel
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.support.PrinterFormatter
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.utils.AidlUtil
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import com.ottokonek.ottokasir.utils.TextUtil
import kotlinx.android.synthetic.main.activity_receipt.*
import kotlinx.android.synthetic.main.toolbar.*


class ReceiptActivity : BaseActivity() {

    private var dataKasbonCashPayment = KasbonCashPaymentResponseModel.DataBean()
    private var dataKasbonQrPayment = KasbonQrPaymentStatusResponseModel.DataBean()

    private var buildNumber = ""
    private var numberSunmi = "V1s-G"
    private var numberAdvan = "O1"

    private var fromQrKasbon = false

    companion object {
        const val KEY_ITEMS_KASBON_PAYMENT_CASH = "key_item_payment_kasbon_cash"
        const val KEY_ITEMS_PAYMENT_KASBON_QR = "key_item_payment_kasbon_qr"
        const val KEY_FROM_PAYMENT_KASBON_QR = "key_from_payment_kasbon_qr"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)
        tvTitle.text = getString(R.string.pembayaran)

        buildNumber = Build.MODEL.toString()
        Log.e("MODEL_NUMBER", buildNumber)

        getDataIntent()

        initContent()

        actionReceipt()
    }

    override fun onResume() {
        super.onResume()
        AidlUtil.getInstance().connectPrinterService(this)
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_FROM_PAYMENT_KASBON_QR))
                fromQrKasbon = it.getBoolean(KEY_FROM_PAYMENT_KASBON_QR, false)

            if (it.containsKey(KEY_ITEMS_KASBON_PAYMENT_CASH))
                dataKasbonCashPayment = it.getSerializable(KEY_ITEMS_KASBON_PAYMENT_CASH) as KasbonCashPaymentResponseModel.DataBean

            if (it.containsKey(KEY_ITEMS_PAYMENT_KASBON_QR))
                dataKasbonQrPayment = it.getSerializable(KEY_ITEMS_PAYMENT_KASBON_QR) as KasbonQrPaymentStatusResponseModel.DataBean
        }
    }

    fun initContent() {

        when (buildNumber) {
            numberSunmi -> {
                viewCetakStruk.visibility = View.VISIBLE
            }
            numberAdvan -> {
                viewCetakStruk.visibility = View.VISIBLE
            }
            else -> {
                viewCetakStruk.visibility = View.GONE
            }
        }


        val intentToDetail = Intent(this, DetailReceiptActivity::class.java)
        if (intent.extras != null) {
            if (intent.extras!![PAYMENT_TYPE]?.equals(KEY_PAYMENT_CASH)!!) {
                fl_amount.visibility = View.VISIBLE
                fl_change.visibility = View.VISIBLE
                fl_application_name.visibility = View.GONE
                fl_store_name.visibility = View.GONE
                intentToDetail.putExtra(KEY_PAYMENT_CASH, intent.extras!![KEY_PAYMENT_CASH] as PaymentCashResponseModel.DataBean)
                val data = intent.extras!![KEY_PAYMENT_CASH] as PaymentCashResponseModel.DataBean
                tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount?.toDouble())
                tvAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid?.toDouble())
                tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change?.toDouble())
                tvOrderID.text = data.code
                tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMM yyyy | HH:mm")


            } else if (intent.extras!![PAYMENT_TYPE]?.equals(KEY_PAYMENT_KASBON)!!) {
                fl_amount.visibility = View.GONE
                fl_change.visibility = View.GONE
                fl_application_name.visibility = View.GONE
                fl_store_name.visibility = View.GONE
                intentToDetail.putExtra(KEY_PAYMENT_KASBON, intent.extras!![KEY_PAYMENT_KASBON] as PaymentCashBondResponseModel.DataBean)
                val data = intent.extras!![KEY_PAYMENT_KASBON] as PaymentCashBondResponseModel.DataBean
                tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount?.toDouble())
                tvAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid?.toDouble())
                tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change?.toDouble())
                tvOrderID.text = data.code
                tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMM yyyy | HH:mm")
                tvTitleTotal.text = getString(R.string.total_kasbon)


            } else if (intent.extras!![PAYMENT_TYPE]?.equals(KEY_PAYMENT_KASBON_CASH)!!) {
                fl_payment_date.visibility = View.VISIBLE
                fl_amount.visibility = View.VISIBLE
                fl_change.visibility = View.VISIBLE
                fl_application_name.visibility = View.GONE
                fl_store_name.visibility = View.GONE

                intentToDetail.putExtra(DetailReceiptActivity.KEY_ITEMS_KASBON_CASH_PAYMENT, dataKasbonCashPayment)
                tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(dataKasbonCashPayment.total_amount.toDouble())
                tvAmount.text = MoneyUtil.convertIDRCurrencyFormat(dataKasbonCashPayment.total_paid.toDouble())
                tvChange.text = MoneyUtil.convertIDRCurrencyFormat(dataKasbonCashPayment.change.toDouble())

                for (data in dataKasbonCashPayment.orders!!) {
                    tvOrderID.text = data.code
                    tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                            "dd MMM yyyy | HH:mm:ss",
                            "dd MMM yyyy | HH:mm")
                    tvPaymentDate.text = DateUtil.simplifyDateFormat(data.cashbond_payment_date,
                            "dd MMM yyyy | HH:mm:ss",
                            "dd MMM yyyy | HH:mm")
                }


            } else if (intent.extras!![PAYMENT_TYPE]?.equals(KEY_PAYMENT_KASBON_QR)!!) {
                fl_payment_date.visibility = View.VISIBLE
                fl_application_name.visibility = View.VISIBLE
                fl_store_name.visibility = View.VISIBLE
                fl_amount.visibility = View.GONE
                fl_change.visibility = View.GONE

                intentToDetail.putExtra(DetailReceiptActivity.KEY_ITEMS_KASBON_QR_PAYMENT, dataKasbonQrPayment)
                tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(dataKasbonQrPayment.total_amount?.toDouble())
                tvName.text = TextUtil.maskNumber(dataKasbonQrPayment.account)
                tvAppName.text = dataKasbonQrPayment.issuer
                tvOrderID.text = dataKasbonQrPayment.code
                tvDate.text = DateUtil.simplifyDateFormat(dataKasbonQrPayment.order_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMM yyyy | HH:mm")
                tvPaymentDate.text = DateUtil.simplifyDateFormat(dataKasbonQrPayment.cashbond_payment_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMM yyyy | HH:mm")


            } else if (intent.extras!![PAYMENT_TYPE]?.equals(KEY_PAYMENT_QR)!!) {
                fl_application_name.visibility = View.VISIBLE
                fl_store_name.visibility = View.VISIBLE
                fl_amount.visibility = View.GONE
                fl_change.visibility = View.GONE
                intentToDetail.putExtra(KEY_PAYMENT_QR, intent.extras!![KEY_PAYMENT_QR] as CheckStatusQrResponseModel.DataBean)
                val data = intent.extras!![KEY_PAYMENT_QR] as CheckStatusQrResponseModel.DataBean
                tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount?.toDouble())
                tvName.text = TextUtil.maskNumber(data.customer_name)
                tvAppName.text = data.issuer
                tvOrderID.text = data.code
                tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                        "dd MMM yyyy | HH:mm:ss",
                        "dd MMM yyyy | HH:mm")
            }
        }

        tvDetail.setOnClickListener {
            startActivity(intentToDetail)
        }

        tvPrintReceipt.setOnClickListener {

            if (intent.extras != null) {
                if (buildNumber == numberSunmi) {

                    if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_CASH)) {
                        PrinterFormatter().printCashSunMi(intent.extras!![KEY_PAYMENT_CASH] as PaymentCashResponseModel.DataBean)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_QR)) {
                        PrinterFormatter().printQrSunMi(intent.extras!![KEY_PAYMENT_QR] as QrPaymentGenerateRequestModel, this@ReceiptActivity)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON)) {
                        PrinterFormatter().printKasbonSunMi(intent.extras!![KEY_PAYMENT_KASBON] as PaymentCashBondResponseModel.DataBean)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON_CASH)) {
                        PrinterFormatter().printKasbonCashSunMi(dataKasbonCashPayment)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON_QR)) {
                        PrinterFormatter().printKasbonQrSunMi(dataKasbonQrPayment)
                    }

                } else if (buildNumber == numberAdvan) {

                    if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_CASH)) {
                        PrinterFormatter().printCashAdvan(intent.extras!![KEY_PAYMENT_CASH] as PaymentCashResponseModel.DataBean)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_QR)) {
                        PrinterFormatter().printQrAdvan(intent.extras!![KEY_PAYMENT_QR] as QrPaymentGenerateRequestModel, this@ReceiptActivity)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON)) {
                        PrinterFormatter().printKasbonAdvan(intent.extras!![KEY_PAYMENT_KASBON] as PaymentCashBondResponseModel.DataBean)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON_CASH)) {
                        PrinterFormatter().printKasbonCashAdvan(dataKasbonCashPayment)

                    } else if (intent.extras!![PAYMENT_TYPE]!!.equals(KEY_PAYMENT_KASBON_QR)) {
                        PrinterFormatter().printKasbonQrAdvan(dataKasbonQrPayment)
                    }
                }
            }

        }
    }

    fun actionReceipt() {

        backAction.setOnClickListener {
            onBackAction()
        }

        btnNextTransaction.setOnClickListener {
            onBackAction()
        }
    }

    override fun onBackPressed() {
        onBackAction()
        super.onBackPressed()
    }

    private fun onBackAction() {
        if (fromQrKasbon) {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        } else {
            val intent = Intent(this, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        AidlUtil.getInstance().disconnectPrinterService(this)
    }
}

