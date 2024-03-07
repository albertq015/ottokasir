package com.ottokonek.ottokasir.ui.fragment.kasbon

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.KasbonCustomerRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonCustomerResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.kasbon.KasbonDetailTransactionActivity
import com.ottokonek.ottokasir.ui.adapter.KasbonCustomerAdapter
import com.ottokonek.ottokasir.ui.callback.SelectedIdCustomer
import com.ottokonek.ottokasir.ui.dialog.SortingCustomerKasbonDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_kasbon_customer.*
import kotlinx.android.synthetic.main.partial_filter_sorting.*
import java.util.*

class KasbonCustomerFragment : BaseLocalFragment(), KasbonPresenter.IKasbonCustomerView, SortingCustomerKasbonDialog.ISortingView {

    private val kasbonPresenter = KasbonPresenter(this)
    private var kasbonCustomerItems = ArrayList<KasbonCustomerResponseModel.DataBean>()
    private var kasbonCustomerAdapter: KasbonCustomerAdapter? = null
    private var sorting = "DESC"
    private var keyWord = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasbon_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentKasbonCustomer()
        actionKasbonCustomer()

        getKasbonCustomer()
        configureList()
    }


    private fun contentKasbonCustomer() {
        ivFilter.visibility = View.GONE

    }


    private fun actionKasbonCustomer() {

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                keyWord = edtSearchCustomer.text.toString()
                getKasbonCustomerResume()
                if (edtSearchCustomer.text.toString() != "") {
                    iv_clear.visibility = View.VISIBLE
                } else {
                    iv_clear.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        iv_clear.setOnClickListener {
            edtSearchCustomer.setText("")
        }

        tvSorting.setOnClickListener {
            val dialog = SortingCustomerKasbonDialog(requireContext(), R.style.style_bottom_sheet, this)
            dialog.show()
        }

    }

    override fun onResume() {
        getKasbonCustomerResume()
        super.onResume()
    }

    override fun onSelectedSorting(sortingBy: String) {
        sorting = sortingBy
        getKasbonCustomer()
    }


    /**
     * Start Call Api Kasbon Customer List
     * */
    private fun getKasbonCustomerResume() {

        val data = KasbonCustomerRequestModel()
        data.sorting = sorting
        data.keyword = keyWord

        kasbonPresenter.onKasbonCustomer(data, requireActivity() as BaseActivity)
    }

    private fun getKasbonCustomer() {
        showLoading()

        val data = KasbonCustomerRequestModel()
        data.sorting = sorting
        data.keyword = keyWord

        kasbonPresenter.onKasbonCustomer(data, requireActivity() as BaseActivity)
    }


    override fun onSuccessKasbonCustomer(result: KasbonCustomerResponseModel) {
        hideLoading()

        if (result.data!!.isEmpty()) {
            nested_scroll.visibility = View.GONE
            view_kasbon_customer_empty.visibility = View.VISIBLE
        } else {
            nested_scroll.visibility = View.VISIBLE
            view_kasbon_customer_empty.visibility = View.GONE
        }

        kasbonCustomerItems.clear()
        result.data.let {
            it?.let { it1 -> kasbonCustomerItems.addAll(it1) }
        }

        refreshList()
    }

    /**
     * End Call Api Kasbon Customer List
     * */

    private fun configureList() {
        if (kasbonCustomerAdapter == null)
            kasbonCustomerAdapter = KasbonCustomerAdapter(kasbonCustomerItems, object : SelectedIdCustomer {

                override fun idCustomer(idCustomer: Int) {

                    val sendData = Bundle()
                    sendData.putBoolean(KasbonDetailTransactionActivity.KEY_ACTIVITY_FROM_KASBON_CUSTOMER, true)
                    sendData.putInt(KasbonDetailTransactionActivity.KEY_ID_KASBON_CUSTOMER, idCustomer)
                    ActivityUtil(requireActivity())
                            .sendData(sendData)
                            .showPage(KasbonDetailTransactionActivity::class.java)
                }
            })

        rvKasbonCustomer?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonCustomerAdapter
        }
    }

    private fun refreshList() {
        kasbonCustomerAdapter.let {
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
                SessionManager.logoutDevice(requireContext())
            }
            else -> {


                if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
                    MessageUserUtil.shortMessage(requireActivity(), getString(R.string.tidak_ada_koneksi))
                } else {
                    if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
                        MessageUserUtil.shortMessage(requireActivity(), getString(R.string.tidak_ada_koneksi))
                    } else {
                        MessageUserUtil.shortMessage(requireActivity(), message)
                    }
                }
            }
        }
    }

}