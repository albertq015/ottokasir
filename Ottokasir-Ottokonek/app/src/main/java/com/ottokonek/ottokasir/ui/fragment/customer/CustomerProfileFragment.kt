package com.ottokonek.ottokasir.ui.fragment.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CustomerDeleteRequestModel
import com.ottokonek.ottokasir.model.api.request.CustomerDetailRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerDetailResponseModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.customer.CustomerUpdateActivity
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerDeleteCallback
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerDeleteConfirmationDialog
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerDeleteSuccessDialog
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_customer_profile.*

class CustomerProfileFragment : BaseCustomerFragment(), CustomerPresenter.ICustomerDetailView, CustomerDeleteCallback {


    private var customerId = 0
    private var customerPhone = ""
    private var customerName = ""
    private var customerEmail = ""

    //var customerDetail: CustomerDetailResponseModel.DataBean.MerchantCustomersBean? = null
    lateinit var customerDetail: CustomerDetailResponseModel
    private val customerPresenter = CustomerPresenter(this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataCustomer()
        getDetailCustomer()

        actionCustomerProfile()
    }

    override fun onResume() {
        getDetailCustomer()
        super.onResume()
    }

    private fun getDataCustomer() {
        customerId = CacheUtil.getPreferenceInteger(IConfig.KEY_CUSTOMER_ID,
                requireActivity() as BaseActivity)

    }


    private fun actionCustomerProfile() {

        btnCustomerDelete.setOnClickListener {
            val dialog = CustomerDeleteConfirmationDialog(requireActivity(),
                    R.style.style_bottom_sheet, this)
            dialog.show()
        }

        btnCustomerUpdate.setOnClickListener {
            val sendData = Bundle()
            sendData.putSerializable(CustomerUpdateActivity.KEY_DATA, customerDetail.data?.merchant_customers)

            ActivityUtil(requireActivity())
                    .sendData(sendData)
                    .showPage(CustomerUpdateActivity::class.java)
        }

    }

    override fun onCustomerDeleteYes() {
        callApiCustomerDelete()
    }

    override fun onCustomerDeleteSuccess() {
        requireActivity().onBackPressed()
    }


    /**
     * Start Call Api Detail Customer
     * */
    private fun getDetailCustomer() {
        showLoading()

        val data = CustomerDetailRequestModel()
        data.id = customerId
        getMainActivity().let {
            customerPresenter.onCustomerDetail(data, requireActivity() as BaseActivity)
        }
    }

    override fun onSuccessCustomerDetail(data: CustomerDetailResponseModel) {
        hideLoading()

        customerDetail = data
        customerPhone = data.data?.merchant_customers?.phone.toString()
        customerName = data.data?.merchant_customers?.name.toString()
        customerEmail = data.data?.merchant_customers?.email.toString()

        etCustomerName.setText(customerName)
        etCustomerPhone.setText(customerPhone)
        etCustomerEmail.setText(customerEmail)
    }


    private fun callApiCustomerDelete() {
        showLoading()

        val data = CustomerDeleteRequestModel()
        data.id = customerId
        getMainActivity().let {
            customerPresenter.onCustomerDelete(data, requireActivity() as BaseActivity)
        }

    }

    override fun onSuccessCustomerDelete(data: BaseResponse) {
        hideLoading()

        val dialog = CustomerDeleteSuccessDialog(requireActivity(),
                R.style.style_bottom_sheet, this)
        dialog.show()
    }

    /**
     * End Call Api Detail Customer
     * */

    override fun handleError(message: String) {
        hideLoading()
        when (message) {
            "Invalid request token!" -> {
                MessageUserUtil.shortMessage(requireActivity(), message)
                SessionManager.logoutDevice(requireActivity())
            }
            else -> {
                if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
                    MessageUserUtil.shortMessage(requireActivity(), getString(R.string.tidak_ada_koneksi))
                } else {
                    MessageUserUtil.shortMessage(requireActivity(), message)
                }            }
        }
    }

}