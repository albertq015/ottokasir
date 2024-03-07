package com.ottokonek.ottokasir.ui.activity.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CustomerUpdateRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerDetailResponseModel
import com.ottokonek.ottokasir.model.api.response.CustomerUpdateResponseModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerUpdateCallback
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerUpdateConfirmationDialog
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerUpdateSuccessDialog
import com.ottokonek.ottokasir.utils.FormValidation
import com.ottokonek.ottokasir.utils.MessageUserUtil
import com.ottokonek.ottokasir.utils.screnshoot.HtmlUtils
import kotlinx.android.synthetic.main.activity_customer_update.btnSave
import kotlinx.android.synthetic.main.activity_customer_update.etCustomerEmail
import kotlinx.android.synthetic.main.activity_customer_update.etCustomerName
import kotlinx.android.synthetic.main.activity_customer_update.etCustomerPhone
import kotlinx.android.synthetic.main.activity_customer_update.tvTitleName
import kotlinx.android.synthetic.main.activity_customer_update.tvTitlePhone
import kotlinx.android.synthetic.main.toolbar.*


class CustomerUpdateActivity : BaseLocalActivity(), CustomerPresenter.ICustomerUpdateView, CustomerUpdateCallback {

    private val customerPresenter = CustomerPresenter(this)
    private var formValidation: Boolean = false
    private var formValidationEmail: Boolean = true
    private var formValidationPhone: Boolean = true

    var customerDetail: CustomerDetailResponseModel.DataBean.MerchantCustomersBean? = null

    companion object {
        const val KEY_DATA = "key_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_update)

        getDataIntent()

        contentCustomerUpdate()
        actionCustomerUpdate()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_DATA)) {
                customerDetail = it.getSerializable(KEY_DATA) as CustomerDetailResponseModel.DataBean.MerchantCustomersBean?


            }
        }
    }

    private fun contentCustomerUpdate() {
        addTextWatcher(etCustomerName)
        addTextWatcher(etCustomerPhone)
        addTextWatcher(etCustomerEmail)

        tvTitle.visibility = View.VISIBLE
        tvTitle.text = getString(R.string.ubah_pelanggan)

        etCustomerName.setText(customerDetail?.name)
        etCustomerPhone.setText(customerDetail?.phone)
        etCustomerEmail.setText(customerDetail?.email)

        tvTitleName.text = HtmlUtils.getHTMLContent(getString(R.string.nama_pelanggan_required))
        tvTitlePhone.text = HtmlUtils.getHTMLContent(getString(R.string.no_handphone_required))
    }

    private fun actionCustomerUpdate() {

        backAction.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            if (formValidation) {
                //call api customer create
                val dialog = CustomerUpdateConfirmationDialog(this,
                        R.style.style_bottom_sheet, this)
                dialog.show()
            } else {
                if (!formValidationPhone) {
                    Toast.makeText(this, getString(R.string.nomor_hp_tidak_valid), Toast.LENGTH_SHORT).show()
                } else if (!formValidationEmail) {
                    Toast.makeText(this, getString(R.string.format_email_salah), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.cek_kembali_form_input_anda), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validationCustomer() {

        if (FormValidation.validName(etCustomerName.text.toString()) && isValidMobilePhone(etCustomerPhone.text.toString())) {

            formValidation = true
            formValidationPhone = true
            btnSave.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_blue)

            if (etCustomerEmail.text.toString() != "") {
                formValidation = false
                formValidationEmail = false
                btnSave.background =
                        ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_grey)

                if (FormValidation.required(etCustomerEmail.text.toString())
                        && FormValidation.validEmail(etCustomerEmail.text.toString())) {
                    formValidation = true
                    formValidationEmail = true
                    btnSave.background =
                            ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_blue)
                } else {
                    formValidation = false
                    formValidationEmail = false
                    btnSave.background =
                            ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_grey)
                }

            }

        } else {
            formValidation = false
            formValidationPhone = false
            btnSave.background =
                    ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_grey)
        }
    }

    fun isValidMobilePhone(input: String): Boolean {
        if (input.length > 8) {
            if (input.length >= 3) {
                if (input.get(0).toString() == "6" && input.get(1).toString() == "3" && input.get(2).toString() == "9") {
                    return true
                }
                /*else if (input.substring(0, 2) == "62") {
                    return true
                }*/
            }
        } else {
            false
        }
        return false
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


    override fun onCustomerUpdateYes() {
        getCustomerUpdate()
    }

    override fun onCustomerUpdateSuccess() {
        onBackPressed()
    }

    /**
     * Start Call Api Customer Update
     * */
    private fun getCustomerUpdate() {
        showLoading()

        val data = CustomerUpdateRequestModel()
        data.id = customerDetail?.id ?: 0
        data.name = etCustomerName.text.toString()
        data.phone = etCustomerPhone.text.toString()
        data.email = etCustomerEmail.text.toString()

        customerPresenter.onCustomerUpdate(data, this)
    }

    override fun onSuccessCustomerUpdate(data: CustomerUpdateResponseModel) {
        hideLoading()

        val dialog = CustomerUpdateSuccessDialog(this,
                R.style.style_bottom_sheet, this)
        dialog.show()

    }
    /**
     * End Call Api Customer Update
     * */

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