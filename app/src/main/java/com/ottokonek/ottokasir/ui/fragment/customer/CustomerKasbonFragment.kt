package com.ottokonek.ottokasir.ui.fragment.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CustomerKasbonRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerKasbonResponseModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.adapter.CustomerKasbonAdapter
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_customer_kasbon.*

class CustomerKasbonFragment : BaseLocalFragment(), CustomerPresenter.ICustomerKasbonView {


    private var customerId = 0
    private val customerPresenter = CustomerPresenter(this)
    private var customerKasbon = mutableListOf<CustomerKasbonResponseModel.DataBean>()
    private var adapterList: CustomerKasbonAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_kasbon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentCustomerKasbon()
        actionCustomerKasbon()

        getCustomerKasbon()
        configureList()
    }

    private fun contentCustomerKasbon() {
        customerId = CacheUtil.getPreferenceInteger(IConfig.KEY_CUSTOMER_ID,
                requireActivity() as BaseActivity)

    }


    private fun actionCustomerKasbon() {

    }


    override fun onResume() {
        getCustomerKasbonResume()
        super.onResume()
    }

    /**
     * start call api customer kasbon
     * */
    //Resume Not Dialog
    private fun getCustomerKasbonResume() {

        val data = CustomerKasbonRequestModel()
        data.customer_id = customerId

        customerPresenter.onCustomerKasbon(data, requireActivity() as BaseActivity)
    }

    private fun getCustomerKasbon() {
        showLoading()

        val data = CustomerKasbonRequestModel()
        data.customer_id = customerId

        customerPresenter.onCustomerKasbon(data, requireActivity() as BaseActivity)
    }

    override fun onSuccessCustomerKasbon(result: CustomerKasbonResponseModel) {
        hideLoading()

        customerKasbon.clear()
        result.data.let {
            it?.let { it1 -> customerKasbon.addAll(it1) }
        }

        refreshList()

    }

    /**
     * end call api customer kasbon
     * */

    private fun configureList() {
        if (adapterList == null)
            adapterList = CustomerKasbonAdapter(requireActivity(), customerKasbon, object : ActionList {

                override fun clickItem(position: Int, value: Any) {

                }
            })

        rvCustomerKasbon?.apply {
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
                }
            }
        }
    }
}