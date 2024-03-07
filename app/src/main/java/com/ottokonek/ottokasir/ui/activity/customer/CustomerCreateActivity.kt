package com.ottokonek.ottokasir.ui.activity.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CustomerCreateRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerCreateResponseModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerCreateCallback
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerCreateDialog
import com.ottokonek.ottokasir.utils.FormValidation
import com.ottokonek.ottokasir.utils.MessageUserUtil
import com.ottokonek.ottokasir.utils.screnshoot.HtmlUtils
import kotlinx.android.synthetic.main.activity_create_customer.*
import kotlinx.android.synthetic.main.toolbar.*

class CustomerCreateActivity : BaseLocalActivity(), CustomerCreateCallback, CustomerPresenter.ICustomerCreateView {

    private val customerPresenter = CustomerPresenter(this)
    private var formValidation: Boolean = false
    private var formValidationEmail: Boolean = true
    private var formValidationPhone: Boolean = true

    private var chooseCustomer: Boolean = false

    companion object {
        const val KEY_FROM_CHOOSE_CUSTOMER = "key_choose_customer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_customer)

        getDataIntent()
        contentAddedCustomer()
        actionAddedCustomer()
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_FROM_CHOOSE_CUSTOMER)) {
                chooseCustomer = it.getBoolean(KEY_FROM_CHOOSE_CUSTOMER)
            }
        }
    }

    private fun contentAddedCustomer() {
        addTextWatcher(etCustomerName)
        addTextWatcher(etCustomerPhone)
        addTextWatcher(etCustomerEmail)

        tvTitle.visibility = View.VISIBLE
        tvTitle.text = getString(R.string.tambah_pelanggan)

        tvTitleName.text = HtmlUtils.getHTMLContent(getString(R.string.nama_pelanggan_required))
        tvTitlePhone.text = HtmlUtils.getHTMLContent(getString(R.string.no_handphone_required))

    }


    private fun actionAddedCustomer() {

        backAction.setOnClickListener {
            onBackPressed()
        }

        btnSave.setOnClickListener {
            if (formValidation) {
                //call api customer create
                getCustomerCreate()
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

    override fun onBackToListCustomer() {
        if (chooseCustomer) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
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

            }

            override fun afterTextChanged(editable: Editable) {
                validationCustomer()
            }
        })
    }

    /**
     * Start Call Api Customer Create
     * */
    private fun getCustomerCreate() {
        showLoading()

        val data = CustomerCreateRequestModel()
        data.name = etCustomerName.text.toString()
        data.phone = etCustomerPhone.text.toString()
        data.email = etCustomerEmail.text.toString()

        customerPresenter.onCustomerCreate(data, this)
    }

    override fun onSuccessCustomerCreate(data: CustomerCreateResponseModel) {
        hideLoading()
        val dialog = CustomerCreateDialog(this, R.style.style_bottom_sheet, this)
        dialog.show()
    }

    /**
     * End Call Api Customer Create
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