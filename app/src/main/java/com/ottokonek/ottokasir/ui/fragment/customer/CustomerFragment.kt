package com.ottokonek.ottokasir.ui.fragment.customer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CustomerListRequestModel
import com.ottokonek.ottokasir.model.api.response.CustomerListResponseModel
import com.ottokonek.ottokasir.model.pojo.CustomerModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.customer.CustomerCreateActivity
import com.ottokonek.ottokasir.ui.activity.customer.CustomerMainActivity
import com.ottokonek.ottokasir.ui.adapter.CustomerListAdapter
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_customer.*


class CustomerFragment : BaseLocalFragment(), CustomerPresenter.ICustomerListView {

    private val customerPresenter = CustomerPresenter(this)
    private val mItems: ArrayList<CustomerModel> = ArrayList()
    private var adapterList: CustomerListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentCustomer()
        actionCustomer()

        //call api customer list
        getCustomerList()
    }


    private fun contentCustomer() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)

        title.visibility = View.VISIBLE
        title.text = getString(R.string.pelanggan)

        search.visibility = View.GONE
        scanBarcode.visibility = View.GONE

        // content list customer
        //dummyData()
        configureList()
    }

    private fun actionCustomer() {

        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                searchCustomer(s.toString())

                if (et_search.text.toString() != "") {
                    iv_clear.visibility = View.VISIBLE
                } else {
                    iv_clear.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        iv_clear.setOnClickListener {
            et_search.setText("")
        }

        btnAdd.setOnClickListener {
            val intent = Intent(requireActivity(), CustomerCreateActivity::class.java)
            startActivity(intent)
        }
    }

    //search
    fun searchCustomer(text: String) {
        val filteredList: ArrayList<CustomerModel> = ArrayList()
        for (item in mItems) {
            if (item.fullName.toLowerCase().contains(text.toLowerCase()) ||
                    item.noHandphone.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adapterList?.filterList(filteredList)
    }

    private fun configureList() {
        if (adapterList == null)
            adapterList = CustomerListAdapter(mItems, object : ActionList {

                override fun clickItem(position: Int, value: Any) {
                    val items = value as CustomerModel

                    val sendData = Bundle()
                    sendData.putInt(CustomerMainActivity.KEY_DATA, items.id)

                    ActivityUtil(requireActivity())
                            .sendData(sendData)
                            .showPage(CustomerMainActivity::class.java)
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

    private fun dummyData() {
        mItems.clear()

        mItems.addAll(mutableListOf(
                CustomerModel(1, "AH", "Ardi Hidayat", "0989898989"),
                CustomerModel(2, "AL", "Ayu Laras", "0967676767"),
                CustomerModel(3, "MN", "Miya Nuril", "0122222222")
        ))

        refreshList()
    }


    override fun onResume() {
        getCustomerList()
        super.onResume()
    }

    /**
     * Start Call Api Customer List
     * */
    private fun getCustomerList() {
        showLoading()

        val data = CustomerListRequestModel()
        data.sorting = ""
        data.keyword = ""
        customerPresenter.onCustomerList(data, requireActivity() as BaseActivity)
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

    override fun handleError(message: String?) {
        hideLoading()
        when (message) {
            "Invalid request token!" -> {
                MessageUserUtil.shortMessage(requireActivity(), message)
                SessionManager.logoutDevice(requireActivity())
            }
            else -> {
                if (message.toString().contains("Failed to connect to ") || message.toString().contains("Unable to resolve host")) {
                    MessageUserUtil.shortMessage(requireActivity(), getString(R.string.tidak_ada_koneksi))
                } else {
                    MessageUserUtil.shortMessage(requireActivity(), message.toString())
                }            }
        }
    }

}