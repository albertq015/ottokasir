package com.ottokonek.ottokasir.ui.activity.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.support.util.CacheUtil
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.model.api.request.KasbonQrPaymentRequestModel
import com.ottokonek.ottokasir.model.api.request.KasbonQrPaymentStatusRequestModel
import com.ottokonek.ottokasir.model.api.request.QrPaymentCheckStatRequestModel
import com.ottokonek.ottokasir.model.api.request.QrPaymentGenerateRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckStatusQrResponseModel
import com.ottokonek.ottokasir.model.api.response.KasbonQrPaymentResponseModel
import com.ottokonek.ottokasir.model.api.response.KasbonQrPaymentStatusResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentQrResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.receipt.ReceiptActivity
import com.ottokonek.ottokasir.ui.activity.share_qris.ShareQrisActivity
import com.ottokonek.ottokasir.ui.dialog.InfoDialog
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.DateUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import com.ottokonek.ottokasir.utils.NetworkConnection
import kotlinx.android.synthetic.main.activity_payment_qr.*
import kotlinx.android.synthetic.main.toolbar.*


class PaymentQRActivity : BaseLocalActivity(), PaymentQrIView, InfoDialog.Callback, KasbonPresenter.IKasbonQrPaymentView {

    private val presenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter
    private var model: QrPaymentGenerateRequestModel = QrPaymentGenerateRequestModel()

    private var qrString: String? = ""
    private var biller = ""
    private var customerId = 0
    private var customerName: String? = null

    private val kasbonPresenter = KasbonPresenter(this)
    private var paymentKasbonAktif = false
    private var orderIdKasbon: String? = null
    private var merchantName: String? = null
    private var totalAmount: String? = null
    private var statusPaymentQr: String? = null


    companion object {
        const val KEY_TOTAL_AMOUNT = "key_total_amount"
        const val KEY_MERCHANT_NAME = "key_merchant_name"
        const val KEY_DATA_CUSTOMER_NAME = "key_data_customer_name"
        const val KEY_DATA_CUSTOMER_ID = "key_data_customer_id"
        const val KEY_PAYMENT_KASBON_AKTIF = "key_payment_kasbon_aktif"
        const val KEY_ORDER_IDS_KASBON_SELECETED = "order_ids_kasbon_seleceted"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Privacy Screenshot
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_payment_qr)

        checkConnection()
        getDataIntent()
        initContent()
    }

    private fun checkConnection() {
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, Observer { isConnected ->

            if (isConnected) {

                if (paymentKasbonAktif) {
                    btnRefresh.setOnClickListener {
                        //showLoading()
                        viewShareQr.visibility = View.INVISIBLE
                        viewRefresh.visibility = View.GONE
                        callApiGenerateQrKasbon()
                    }
                } else {
                    btnRefresh.setOnClickListener {
                        //showLoading()
                        viewShareQr.visibility = View.INVISIBLE
                        viewRefresh.visibility = View.GONE
                        callApiGenerateQr()
                    }
                }
            } else {
                //QR BELUM ADA
                if (qrString == "") {
                    viewShareQr.visibility = View.GONE
                    viewRefresh.visibility = View.VISIBLE
                }
                pb_progressbar.visibility = View.GONE
                spCheckStatus.visibility = View.GONE
            }

        })
    }

    private fun getDataIntent() {
        customerId = intent.getIntExtra(KEY_DATA_CUSTOMER_ID, 0)
        customerName = intent.getStringExtra(KEY_DATA_CUSTOMER_NAME)

        paymentKasbonAktif = intent.getBooleanExtra(KEY_PAYMENT_KASBON_AKTIF, false)
        orderIdKasbon = intent.getStringExtra(KEY_ORDER_IDS_KASBON_SELECETED)
        merchantName = intent.getStringExtra(KEY_MERCHANT_NAME)
        totalAmount = intent.getStringExtra(KEY_TOTAL_AMOUNT)
    }

    @SuppressLint("SetTextI18n")
    fun initContent() {
        tvTitle.text = getString(R.string.scan_qr)
        tvCancel.text = getString(R.string.batal)

        backAction.setOnClickListener {
            onBackPressed()
        }


        if (paymentKasbonAktif) {
            callApiGenerateQrKasbon()
            //tvTotalPurchase.text = totalAmount?.toDouble()?.let { MoneyUtil.convertCurrencyPHP1(it) }
            //tvMerchantName.text = merchantName

            tvCancel.setOnClickListener {
                onBackPressed()
            }

            btnCheckStatus.setOnClickListener {
                callApiCheckStatusQrKasbon()
            }

        } else {
            callApiGenerateQr()
            //tvTotalPurchase.text = //intent.getStringExtra("total")
            //tvMerchantName.text = //CacheUtil.getPreferenceString(IConfig.SESSION_NAME, this)

            tvCancel.setOnClickListener {
                CartManager.removeAllCartItem()
                setResult(Activity.RESULT_OK)
                finish()
            }

            btnCheckStatus.setOnClickListener {
                callApiCheckStatusQr()
            }
        }
    }


    /**
     * =========================== QR PAYMENT BIASA ===========================
     * */
    private fun callApiGenerateQr() {
        pb_progressbar.visibility = View.VISIBLE

        biller = "OK" + DateUtil.getTimestamp()
        model.customerName = customerName
        model.customer_id = customerId
        model.total_amount = presenter.getTotalPrice()
        model.mpan = CacheUtil.getPreferenceString(IConfig.SESSION_MPAN, this)
        model.mid = CacheUtil.getPreferenceString(IConfig.SESSION_MID, this)
        model.nmId = CacheUtil.getPreferenceString(IConfig.SESSION_NMID, this)
        model.bill_ref_num = biller
        model.products = presenter.getProductInOrder()
        model.date = DateUtil.getNowTime("dd MMM yyyy | HH:mm:ss")

        /*if (model.mpan == "" || model.mid == "" || model.nmId == "") {
            val dialog = InfoDialog(this, R.style.CustomDialog, resources.getString(R.string.info_toko_belum_terdaftar_qris),
                    false, this)
            dialog.show()
        } else {
            presenter.onQrPaymentGenerate(model, this)
        }*/
        presenter.onQrPaymentGenerate(model, this)
    }

    override fun onSuccessGenerateQr(data: PaymentQrResponseModel) {
        pb_progressbar.visibility = View.GONE

        viewShareQr.visibility = View.VISIBLE
        tvShareQr.visibility = View.VISIBLE

        tvTotalPurchase.text = data.data?.total_amount?.toDouble()?.let { MoneyUtil.convertCurrencyPHP1(it) }
        tvMerchantName.text = data.data?.merchant_name

        qrString = data.data?.qr_string
        ivQr.setImageBitmap(createQR(data.data?.qr_string))

        val handler = Handler()
        handler.postDelayed({
            presenter.onQrPaymentCheckStatusAuto(QrPaymentCheckStatRequestModel(biller), 0, this)
        }, 7000)

        tvShareQr.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ShareQrisActivity.KEY_DATA_PEMBELIAN_QRIS, data.data)
            ActivityUtil(this)
                    .sendData(bundle)
                    .showPage(ShareQrisActivity::class.java)
        }
    }

    private fun callApiCheckStatusQr() {
        showLoading()

        presenter.onQrPaymentCheckStatus(QrPaymentCheckStatRequestModel(biller), this)
    }


    override fun onSuccessCheckStatusQr(data: CheckStatusQrResponseModel) {
        hideLoading()

        if (data.data != null) {
            model.customerName = data.data?.account
            model.issuer = data.data?.issuer
            statusPaymentQr = data.data?.status
        }

        if (statusPaymentQr == "Success") {
            val intent = Intent(this, ReceiptActivity::class.java)
            intent.putExtra(IConfig.KEY_PAYMENT_QR, data.data)
            intent.putExtra(IConfig.PAYMENT_TYPE, IConfig.KEY_PAYMENT_QR)
            startActivity(intent)
        } else {
            Toast.makeText(this, statusPaymentQr, Toast.LENGTH_SHORT).show()
            tvSuggestionCheck.visibility = View.VISIBLE
            btnCheckStatus.visibility = View.VISIBLE
        }
    }


    /**
     * =========================== QR PAYMENT KASBON ===========================
     * */
    private fun callApiGenerateQrKasbon() {
        pb_progressbar.visibility = View.VISIBLE

        val data = KasbonQrPaymentRequestModel()
        data.order_id = orderIdKasbon
        data.mid = CacheUtil.getPreferenceString(IConfig.SESSION_MID, this)
        data.mpan = CacheUtil.getPreferenceString(IConfig.SESSION_MPAN, this)
        data.nmId = CacheUtil.getPreferenceString(IConfig.SESSION_NMID, this)
        data.total_amount = totalAmount!!.toDouble()

        kasbonPresenter.onKasbonQrPaymentGenerate(data, this)
    }

    override fun onSuccessGenerateQrKasbon(result: KasbonQrPaymentResponseModel) {
        pb_progressbar.visibility = View.GONE

        viewShareQr.visibility = View.VISIBLE
        tvShareQr.visibility = View.VISIBLE

        tvTotalPurchase.text = result.data?.total_amount?.toDouble()?.let { MoneyUtil.convertCurrencyPHP1(it) }
        tvMerchantName.text = result.data?.merchant_name ?: ""

        qrString = result.data?.qr_string
        ivQr.setImageBitmap(createQR(result.data?.qr_string))

        val handler = Handler()
        handler.postDelayed({
            callApiCheckStatusQrKasbonAuto()
        }, 7000)

        tvShareQr.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(ShareQrisActivity.KEY_FROM_PELUNASAN_KASBON, true)
            bundle.putSerializable(ShareQrisActivity.KEY_DATA_PELUNASAN_QRIS, result.data)
            ActivityUtil(this)
                    .sendData(bundle)
                    .showPage(ShareQrisActivity::class.java)
        }
    }

    private fun callApiCheckStatusQrKasbon() {
        showLoading()

        val data = KasbonQrPaymentStatusRequestModel()
        data.bill_ref_num = orderIdKasbon
        data.cashbond = true

        kasbonPresenter.onKasbonQrPaymentStatus(data, this)
    }

    private fun callApiCheckStatusQrKasbonAuto() {
        spCheckStatus.visibility = View.VISIBLE
        tvSuggestionCheck.visibility = View.GONE
        btnCheckStatus.visibility = View.GONE

        val data = KasbonQrPaymentStatusRequestModel()
        data.bill_ref_num = orderIdKasbon
        data.cashbond = true

        kasbonPresenter.onKasbonQrPaymentStatusAuto(data, 0, this)
    }

    override fun onSuccessCheckStatusQrKasbon(result: KasbonQrPaymentStatusResponseModel) {
        hideLoading()
        spCheckStatus.visibility = View.GONE

        statusPaymentQr = result.data?.status_cashbond
        if (statusPaymentQr == "Paid" || statusPaymentQr == "Lunas") {
            val intent = Intent(this, ReceiptActivity::class.java)
            intent.putExtra(ReceiptActivity.KEY_FROM_PAYMENT_KASBON_QR, true)
            intent.putExtra(ReceiptActivity.KEY_ITEMS_PAYMENT_KASBON_QR, result.data)
            intent.putExtra(IConfig.PAYMENT_TYPE, IConfig.KEY_PAYMENT_KASBON_QR)
            startActivityForResult(intent, IConfig.REQUEST_KASBON_QR)
        } else {
            Toast.makeText(this, statusPaymentQr, Toast.LENGTH_SHORT).show()
            tvSuggestionCheck.visibility = View.VISIBLE
            btnCheckStatus.visibility = View.VISIBLE
        }

    }


    /**
     * =========================== QR SET TO IMAGE ===========================
     * */
    private fun createQR(str: String?): Bitmap {

        val result: BitMatrix = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 525, 525, null)

        val w = result.width
        val h = result.height

        val pixels = IntArray(w.times(h))

        for (y in 0 until h) {
            val offset = y.times(h)
            for (x in 0 until w) {
                pixels[offset.plus(x)] = if (result.get(x, y)) BLACK else WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, 525, 0, 0, w, h)

        return bitmap
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IConfig.REQUEST_KASBON_QR) {
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCancel() {
        onBackPressed()
    }

    override fun handleCheckProcessing() {
        spCheckStatus.visibility = View.VISIBLE
        tvSuggestionCheck.visibility = View.GONE
        btnCheckStatus.visibility = View.GONE
    }

    override fun handleCheckComplete() {
        spCheckStatus.visibility = View.GONE
        tvSuggestionCheck.visibility = View.VISIBLE
        btnCheckStatus.visibility = View.VISIBLE
    }


    override fun handleError(message: String) {
        hideLoading()
        pb_progressbar.visibility = View.GONE
        spCheckStatus.visibility = View.GONE

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onBadConnectionGenerateQr() {
        hideLoading()
        pb_progressbar.visibility = View.GONE
        spCheckStatus.visibility = View.GONE


        viewRefresh.visibility = View.VISIBLE
        viewShareQr.visibility = View.GONE
        tvShareQr.visibility = View.GONE
        tvSuggestionCheck.visibility = View.GONE
        btnCheckStatus.visibility = View.GONE
    }

    override fun onBadConnectionCheckStatus() {
        hideLoading()
        pb_progressbar.visibility = View.GONE
        spCheckStatus.visibility = View.GONE

        tvSuggestionCheck.visibility = View.VISIBLE
        btnCheckStatus.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        presenter.onClear()
    }

    override fun logout() {
        super.logout()
    }
}