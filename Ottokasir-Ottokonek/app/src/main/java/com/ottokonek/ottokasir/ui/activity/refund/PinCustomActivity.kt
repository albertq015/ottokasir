package com.ottokonek.ottokasir.ui.activity.refund

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import android.widget.Toast
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.IConfig.Companion.FIREBASE_TOKEN
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.LoginRequestModel
import com.ottokonek.ottokasir.model.api.request.RefundRequestModel
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.RefundResponseModel
import com.ottokonek.ottokasir.presenter.AuthPresenter
import com.ottokonek.ottokasir.presenter.RefundPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.auth.LoginViewInterface
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import com.ottokonek.ottokasir.ui.dialog.refund.RefundProcessingCallback
import com.ottokonek.ottokasir.ui.dialog.refund.RefundProcessingDialog
import com.ottokonek.ottokasir.ui.dialog.snack_bar.TopSnackbar
import com.ottokonek.ottokasir.utils.CryptoUtil
import kotlinx.android.synthetic.main.activity_pin_custom.*

class PinCustomActivity : BaseLocalActivity(), PinCustomAdapter.Callback, RefundProcessingCallback, LoginViewInterface, RefundPresenter.SuccessRefundIView {

    private val authPresenter = AuthPresenter(this)
    private val refundPresenter = RefundPresenter(this)
    private var phone: String = ""
    private var pin: String? = null
    private var fireBaseToken: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    private var invoiceCode: String? = null
    private var referenceNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin_custom)

        initPermission()
        initViewContent()
    }

    private fun initViewContent() {
        val bundle: Bundle? = intent.extras
        invoiceCode = bundle?.getString(IConfig.ORDER_ID)
        referenceNumber = bundle?.getString(IConfig.REFERENCE_NUMBER)

        phone = SessionManager.getPhone(this)
        fireBaseToken = FIREBASE_TOKEN
        layout_back.setOnClickListener { onBackPressed() }
        pinList.layoutManager = GridLayoutManager(this, 3)
        pinList.adapter = PinCustomAdapter(this)
    }

    override fun onUpdatePin(digit: String?) {
        if (pinCustomView.input(digit.toString()) == 6) {
            onCallApiLogin(pinCustomView.code)
        }
    }

    override fun onDeletePin() {
        pinCustomView!!.delete()
    }


    private fun onCallApiLogin(code: String) {
        showLoading()
        val request = LoginRequestModel()
        request.user_id = phone
        request.pin = code
        request.latitude = 0.0
        request.longitude = 0.0
        //request.firebase_token = fireBaseToken

        authPresenter.doLogin(request, this@PinCustomActivity)
    }

    override fun handleDataLogin(model: LoginResponseModel) {
        hideLoading()
        if (model.baseMeta.code == 200) {
            onRefund()
        } else {
            TopSnackbar.showSnackBarRed(this,
                    findViewById(R.id.snackbar_container), "Pin yang anda masukan salah.")
        }
    }

    override fun handleDataLoginSync(model: LoginResponseModel) {

    }

    private fun onRefund() {
        val request = RefundRequestModel()
        request.order_id = invoiceCode
        request.rrn = CryptoUtil.encryptRSA(referenceNumber?.toByteArray())
        refundPresenter.onRefund(request, this)
    }

    override fun onSuccessRefund(data: RefundResponseModel) {
        val data: RefundResponseModel = data

        if (data.meta.code == 200) {
            val dialog = RefundProcessingDialog(this, R.style.style_bottom_sheet, this)
            dialog.show()
        } else {
            Toast.makeText(this, data.meta.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun refundOk() {
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        authPresenter.onDestroy()
        super.onPause()
    }

    override fun handleProcessing() {
        CustomProgressDialog.showDialog(this, "Loading")
    }


    override fun handleError() {
        CustomProgressDialog.closeDialog()
        TopSnackbar.showSnackBarRed(this,
                findViewById(R.id.snackbar_container), "Gagal menghubungkan ke server...")
    }
}