package com.ottokonek.ottokasir.ui.activity.receipt

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.support.util.CacheUtil
import com.google.gson.Gson
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CheckRefundRequestModel
import com.ottokonek.ottokasir.model.api.request.QrPaymentGenerateRequestModel
import com.ottokonek.ottokasir.model.api.response.*
import com.ottokonek.ottokasir.model.miscModel.ItemModel
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.model.pojo.TransactionBean
import com.ottokonek.ottokasir.model.pojo.kasbon.OrderBean
import com.ottokonek.ottokasir.presenter.RefundPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.refund.PinCustomActivity
import com.ottokonek.ottokasir.ui.adapter.*
import com.ottokonek.ottokasir.ui.dialog.refund.RefundConfirmationCallback
import com.ottokonek.ottokasir.ui.dialog.refund.RefundConfirmationDialog
import com.ottokonek.ottokasir.ui.dialog.share_receipt.FormatReceiptCallback
import com.ottokonek.ottokasir.ui.dialog.share_receipt.FormatReceiptDialog
import com.ottokonek.ottokasir.utils.*
import com.ottokonek.ottokasir.utils.screnshoot.FileUtil
import com.ottokonek.ottokasir.utils.screnshoot.ScreenshotUtil
import kotlinx.android.synthetic.main.activity_detail_receipt.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.FileOutputStream


class DetailReceiptActivity : BaseLocalActivity(), FormatReceiptCallback, RefundConfirmationCallback, RefundPresenter.CheckRefundIView {

    private val productAdapter: ProductDetailAdapter = ProductDetailAdapter(this)

    private val refundPresenter = RefundPresenter(this)

    private var fileReceipt: Bitmap? = null

    private var invoiceCode = ""

    private var referenceNumber: String? = null

    private var refundStatus: String? = null

    private var authority: String? = null


    companion object {
        const val KEY_PAYMENT_KASBON_LUNAS = "kasbon_lunas"
        const val KEY_PAYMENT_KASBON_LUNAS_CUSTOMER = "kasbon_lunas_customer"
        const val KEY_ITEMS_KASBON_CASH_PAYMENT = "key_item_kasbon_cash_payment"
        const val KEY_ITEMS_KASBON_QR_PAYMENT = "key_item_kasbon_qr_payment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_receipt)
        initView()

        authority = BuildConfig.APPLICATION_ID
        Log.e("authorities", authority!!)

        //onRefundTransaction()
        iv_share_receipt.setOnClickListener {
            val dialog = FormatReceiptDialog(this, R.style.style_bottom_sheet, this)
            dialog.show()
        }
    }


    fun initView() {

        if (intent.getSerializableExtra(KEY_PAYMENT_KASBON_LUNAS_CUSTOMER) != null) {
            setDetailKasbonLunasCustomer()
        }

        if (intent.getSerializableExtra(KEY_PAYMENT_KASBON_LUNAS) != null) {
            setDetailKasbonLunas()
        }

        if (intent.getSerializableExtra(KEY_ITEMS_KASBON_CASH_PAYMENT) != null) {
            setDetailKasbonCashPayment()
        }

        if (intent.getSerializableExtra(KEY_ITEMS_KASBON_QR_PAYMENT) != null) {
            setDetailFromKasbonQr()
        }

        if (intent.getSerializableExtra(IConfig.KEY_PAYMENT_HISTORY_ALL) != null) {
            setDetailFromHistoryAll()
        }

        if (intent.getSerializableExtra(IConfig.KEY_PAYMENT_KASBON) != null) {
            setDetailFromKasbon()
        }

        if (intent.getSerializableExtra(IConfig.KEY_PAYMENT_CASH) != null) {
            setDetailFromPaymentCash()
        }

        if (intent.getSerializableExtra(IConfig.KEY_PAYMENT_QR) != null) {
            setDetailFromPaymentQr()
        }


        backAction.setImageResource(R.drawable.ic_close_white)
        backAction.setOnClickListener {
            onBackPressed()
        }

    }


    fun setDetailKasbonLunasCustomer() {
        tvTitle.text = getString(R.string.bukti_pelunasan_kasbon)
        tvTitleReceipt.text = getString(R.string.bukti_pelunasan_kasbon)

        val data: TransactionBean = intent.getSerializableExtra(KEY_PAYMENT_KASBON_LUNAS_CUSTOMER) as TransactionBean

        tvStoreName.text = data.store_name
        tvPelunasanDate.visibility = View.VISIBLE
        tvPelunasanDate.text = DateUtil.simplifyDateFormat(data.cashbond_payment_date,
                "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                "dd MMMM yyyy | HH:mm")

        for (item in data.items!!) {
            fl_change.visibility = View.VISIBLE
            fl_total_pembayaran.visibility = View.VISIBLE

            invoiceCode = data.code.toString()
            tvOrderID.text = data.code
            tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                    "dd MMM yyyy | HH:mm:ss",
                    "dd MMMM yyyy")
            tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                    "dd MMM yyyy | HH:mm:ss",
                    "HH:mm")

            tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
            tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid!!.toDouble())
            tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change!!.toDouble())

            rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvItems.setHasFixedSize(true)
            rvItems.adapter = OrderItemKasbonLunasAdapter(this, data.items!!)
        }
    }

    fun setDetailKasbonCashPayment() {
        viewParent.visibility = View.GONE
        val data: KasbonCashPaymentResponseModel.DataBean = intent.getSerializableExtra(KEY_ITEMS_KASBON_CASH_PAYMENT) as KasbonCashPaymentResponseModel.DataBean

        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount.toDouble())
        tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid.toDouble())
        tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change.toDouble())

        for (order in data.orders!!) {
            invoiceCode = order.code.toString()
            tvStoreName.text = order.store_name
            fl_change.visibility = View.VISIBLE
            fl_total_pembayaran.visibility = View.VISIBLE

            val paymentType = order.payment_method
            if (paymentType == "Kasbon" || paymentType == "CashBond") {
                tvTitle.text = getString(R.string.bukti_pelunasan_kasbon)
                tvTitleReceipt.text = getString(R.string.bukti_pelunasan_kasbon)
                tvPelunasanDate.visibility = View.VISIBLE
                tvPelunasanDate.text = DateUtil.simplifyDateFormat(order.cashbond_payment_date,
                        "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                        "dd MMMM yyyy | HH:mm")
            } else {
                tvTitle.text = getString(R.string.bukti_pembelian)
            }
        }

        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.adapter = OrderItemKasbonCashPaymentAdapter(this, data.orders!!)
    }


    fun setDetailKasbonLunas() {
        val data: OrderBean = intent.getSerializableExtra(KEY_PAYMENT_KASBON_LUNAS) as OrderBean
        invoiceCode = data.code.toString()

        val paymentType = data.payment_method
        if (paymentType == "Kasbon" || paymentType == "CashBond") {
            tvTitle.text = getString(R.string.bukti_pelunasan_kasbon)
            tvTitleReceipt.text = getString(R.string.bukti_pelunasan_kasbon)
            tvPelunasanDate.visibility = View.VISIBLE
            tvPelunasanDate.text = DateUtil.simplifyDateFormat(data.cashbond_payment_date,
                    "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                    "dd MMMM yyyy | HH:mm")
        } else {
            tvTitle.text = getString(R.string.bukti_pembelian)
        }

        tvStoreName.text = data.merchant

        for (item in data.items!!) {
            fl_change.visibility = View.VISIBLE
            fl_total_pembayaran.visibility = View.VISIBLE

            tvOrderID.text = data.code
            tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                    "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                    "dd MMMM yyyy,")
            tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                    "dd MMM yyyy | HH:mm:ss",
                    "HH:mm")

            tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
            tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid!!.toDouble())
            tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change!!.toDouble())

            rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvItems.setHasFixedSize(true)
            rvItems.adapter = OrderItemKasbonLunasAdapter(this, data.items!!)
        }
    }

    fun setDetailFromKasbonQr() {
        val data: KasbonQrPaymentStatusResponseModel.DataBean = intent.getSerializableExtra(KEY_ITEMS_KASBON_QR_PAYMENT) as KasbonQrPaymentStatusResponseModel.DataBean


        tvTitle.text = getString(R.string.bukti_pelunasan_kasbon)
        tvTitleReceipt.text = getString(R.string.bukti_pelunasan_kasbon)
        fl_application_name.visibility = View.VISIBLE
        fl_account_payment.visibility = View.VISIBLE


        referenceNumber = data.code
        invoiceCode = data.code.toString()
        tvOrderID.text = data.code

        tvPelunasanDate.visibility = View.VISIBLE
        tvPelunasanDate.text = DateUtil.simplifyDateFormat(data.cashbond_payment_date,
                "dd MMM yyyy | HH:mm:ss", //15 Jul 2020 | 16:29:07
                "dd MMMM yyyy | HH:mm")

        tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm", //8 Jul 2019 | 11:00:43
                "dd MMMM yyyy,")
        tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm",
                "HH:mm")

        tvStoreName.text = CacheUtil.getPreferenceString(IConfig.SESSION_NAME, this)
        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
        tvAppName.text = data.issuer
        tvAccount.text = TextUtil.maskNumber(data.account)

        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.adapter = OrderItemKasbonQrPaymentAdapter(this, data.items)
    }


    fun setDetailFromHistoryAll() {
        var data: TransactionHistoryResponse.DataBean = intent.getSerializableExtra(
                IConfig.KEY_PAYMENT_HISTORY_ALL) as TransactionHistoryResponse.DataBean

        val paymentType = data.paymentType
        if (paymentType == "Kasbon" || paymentType == "CashBond") {
            tvTitle.text = getString(R.string.bukti_pelunasan_kasbon)
            tvTitleReceipt.text = getString(R.string.bukti_pelunasan_kasbon)
        } else {
            tvTitle.text = getString(R.string.bukti_pembelian)
        }

        refundStatus = data.refundStatus
        referenceNumber = data.referenceNumber
        invoiceCode = data.orderID.toString()
        tvOrderID.text = data.orderID

        tvDate.text = DateUtil.simplifyDateFormat(data.transactionDate,
                "yyyy-MM-dd HH:mm:ss",
                "dd MMMM yyyy,")
        tvTime.text = DateUtil.simplifyDateFormat(data.transactionDate,
                "yyyy-MM-dd HH:mm:ss",
                "HH:mm")
        tvStoreName.text = CacheUtil.getPreferenceString(IConfig.SESSION_NAME, this)
        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.amount.toDouble())
        val gson = Gson()
        val items = gson.fromJson(data.products, Array<ItemModel>::class.java)
        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)

        if (data.paymentType?.equals("CASH", ignoreCase = true)!!) {
            setDetailCashView(data.totalPaid.toDouble(), data.change.toDouble())

        } else if (data.paymentType?.equals("QR", ignoreCase = true)!!) {
            setDetailQRView(data.issuer, data.customerName)

        }
        rvItems.adapter = productAdapter
        productAdapter.setData(items)
    }

    fun setDetailFromKasbon() {
        tvTitle.text = getString(R.string.bukti_kasbon)
        tvItemName.text = getString(R.string.total_kasbon)
        tvTitleReceipt.text = getString(R.string.bukti_kasbon)
        fl_change.visibility = View.GONE
        fl_total_pembayaran.visibility = View.GONE
        var data: PaymentCashBondResponseModel.DataBean = intent.getSerializableExtra(
                IConfig.KEY_PAYMENT_KASBON) as PaymentCashBondResponseModel.DataBean
        referenceNumber = ""
        invoiceCode = data.code.toString()
        tvOrderID.text = data.code

        tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm:ss", //8 Jul 2019 | 11:00:43
                "dd MMMM yyyy,")
        tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm:ss",
                "HH:mm")
        tvStoreName.text = data.store_name
        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
        tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid!!.toDouble())
        tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change!!.toDouble())

        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.adapter = OrderItemKasbonCustomerAdapter(this, data.items)
    }


    @SuppressLint("SetTextI18n")
    fun setDetailFromPaymentCash() {
        tvTitle.text = getString(R.string.bukti_pembelian)
        fl_change.visibility = View.VISIBLE
        fl_total_pembayaran.visibility = View.VISIBLE
        var data: PaymentCashResponseModel.DataBean = intent.getSerializableExtra(
                IConfig.KEY_PAYMENT_CASH) as PaymentCashResponseModel.DataBean
        referenceNumber = ""
        invoiceCode = data.code.toString()
        tvOrderID.text = data.code

        tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm:ss", //8 Jul 2019 | 11:00:43
                "dd MMMM yyyy,")
        tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm:ss",
                "HH:mm")
        tvStoreName.text = data.store_name
        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
        tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.total_paid!!.toDouble())
        tvChange.text = MoneyUtil.convertIDRCurrencyFormat(data.change!!.toDouble())

        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.adapter = OrderedItemAdapter(this, data.items)
    }

    fun setDetailFromPaymentQr() {
        tvTitle.text = getString(R.string.bukti_pembelian)
        fl_application_name.visibility = View.VISIBLE
        fl_account_payment.visibility = View.VISIBLE

        var data: CheckStatusQrResponseModel.DataBean = intent.getSerializableExtra(
                IConfig.KEY_PAYMENT_QR) as CheckStatusQrResponseModel.DataBean
        referenceNumber = data.code
        invoiceCode = data.code.toString()
        tvOrderID.text = data.code

        tvDate.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm", //8 Jul 2019 | 11:00:43
                "dd MMMM yyyy,")
        tvTime.text = DateUtil.simplifyDateFormat(data.order_date,
                "dd MMM yyyy | HH:mm",
                "HH:mm")

        tvStoreName.text = CacheUtil.getPreferenceString(IConfig.SESSION_NAME, this)
        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(data.total_amount!!.toDouble())
        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        tvAppName.text = data.issuer
        tvAccount.text = TextUtil.maskNumber(data.customer_name)

        rvItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvItems.setHasFixedSize(true)
        rvItems.adapter = OrderItemQrPaymentAdapter(this, data.items)
    }


    fun setDetailQRView(appName: String, accountPayment: String) {
        fl_application_name.visibility = View.VISIBLE
        fl_account_payment.visibility = View.VISIBLE
        if (appName != null) tvAppName.text = appName
        if (accountPayment != null) tvAccount.text = TextUtil.maskNumber(accountPayment)
    }

    fun setDetailCashView(total_paid: Double, change: Double) {
        fl_change.visibility = View.VISIBLE
        fl_total_pembayaran.visibility = View.VISIBLE
        if (total_paid != null) tvPayAmount.text = MoneyUtil.convertIDRCurrencyFormat(total_paid)
        if (change != null) tvChange.text = MoneyUtil.convertIDRCurrencyFormat(change)
    }


    override fun formatShareToImage() {
        fileReceipt = ScreenshotUtil.instance?.takeScreenshotForView(cv_receipt)

        if (fileReceipt != null) {
            val fileName: String = IConfig.FILE_NAME_FOTO_RECEIPT + invoiceCode + IConfig.EXTENSION_FILE_FOTO

            val folderFoto = File(Environment.getExternalStorageDirectory(), IConfig.FOLDER_FOTO)
            if (!folderFoto.exists()) folderFoto.mkdirs()

            val savedPhoto = File(Environment.getExternalStorageDirectory(), fileName)
            FileUtil.instance?.storeBitmap(fileReceipt!!, savedPhoto.path)

            val contentUri = FileProvider.getUriForFile(this, "$authority", savedPhoto)


            val share = Intent(Intent.ACTION_SEND)
            share.type = "*/*"
            share.putExtra(Intent.EXTRA_EMAIL, "")
            share.putExtra(Intent.EXTRA_SUBJECT, "Receipt OttoKasir - $invoiceCode")
            share.putExtra(Intent.EXTRA_TEXT, invoiceCode)
            share.putExtra(Intent.EXTRA_STREAM, contentUri)
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(share, "Share Receipt"))
        }
    }

    override fun formatShareToPdf() {
        fileReceipt = ScreenshotUtil.instance?.takeScreenshotForView(cv_receipt)

        if (fileReceipt != null) {
            val fileNamePdf: String = IConfig.FILE_NAME_PDF_RECEIPT + invoiceCode + IConfig.EXTENSION_FILE_PDF

            val folderPdf = File(Environment.getExternalStorageDirectory(), IConfig.FOLDER_PDF)
            if (!folderPdf.exists()) folderPdf.mkdirs()

            val savedPdf = File(Environment.getExternalStorageDirectory(), fileNamePdf)
            FileUtil.instance?.storeBitmap(fileReceipt!!, savedPdf.path)

            val bitmap: Bitmap = BitmapFactory.decodeFile(savedPdf.absolutePath)
            val document = PdfDocument()
            val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page: PdfDocument.Page = document.startPage(pageInfo)

            val canvas: Canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            document.finishPage(page)

            document.writeTo(FileOutputStream(savedPdf))
            document.close()

            val contentUri = FileProvider.getUriForFile(this, "$authority", savedPdf)

            val share = Intent(Intent.ACTION_SEND)
            share.type = "*/*"
            share.putExtra(Intent.EXTRA_EMAIL, "")
            share.putExtra(Intent.EXTRA_SUBJECT, "Receipt OttoKasir - $invoiceCode")
            share.putExtra(Intent.EXTRA_TEXT, invoiceCode)
            share.putExtra(Intent.EXTRA_STREAM, contentUri)
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(share, "Share Receipt"))
        }
    }


    private fun onRefundTransaction() {
        if (referenceNumber != "") {
            onCheckRefund()
        }
    }

    private fun onCheckRefund() {
        val request = CheckRefundRequestModel()
        request.order_id = invoiceCode
        request.rrn = CryptoUtil.encryptRSA(referenceNumber?.toByteArray())
        refundPresenter.onCheckRefund(request, this)
    }

    override fun onSuccessCheckRefund(data: CheckRefundResponseModel) {
        val refundRrn = data.data.refundRrn

        if (data.meta.code == 200) {
            if (refundRrn == "") {
                btnRefund.visibility = View.VISIBLE
                btnRefund.setOnClickListener {
                    val dialog = RefundConfirmationDialog(this, R.style.style_bottom_sheet, this)
                    dialog.show()
                }
            } else if (refundRrn != "") {
                btnRefund.visibility = View.VISIBLE
                btnRefund.text = "Sudah di Refund"
                btnRefund.setTextColor(Color.RED)
            }
        }
    }


    override fun refundYes() {
        val intent = Intent(this, PinCustomActivity::class.java)
        intent.putExtra(IConfig.ORDER_ID, invoiceCode)
        intent.putExtra(IConfig.REFERENCE_NUMBER, referenceNumber)
        startActivity(intent)
    }


}
