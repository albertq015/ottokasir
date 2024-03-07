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
import com.ottokonek.ottokasir.model.api.request.KasbonLunasRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonLunasResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.adapter.KasbonLunasAdapter
import com.ottokonek.ottokasir.ui.callback.ActionList
import com.ottokonek.ottokasir.ui.dialog.SortingDialog
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonFilterCallback
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonFilterDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_kasbon_lunas.*
import kotlinx.android.synthetic.main.partial_filter_sorting.*
import java.util.*

class KasbonLunasFragment : BaseLocalFragment(), KasbonPresenter.IKasbonLunasView, SortingDialog.ISortingView, KasbonFilterCallback {

    private val kasbonPresenter = KasbonPresenter(this)
    private var kasbonLunasItems = ArrayList<KasbonLunasResponseModel.DataBean>()
    private var kasbonLunasAdapter: KasbonLunasAdapter? = null

    private var sorting = "DESC"
    private var keyWord = ""
    private var startDate = ""
    private var endDate = ""
    private var periode = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasbon_lunas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionKasbonLunas()

        getKasbonLunas()

        configureList()
    }


    private fun actionKasbonLunas() {

        edtSearchCustomer.hint = getString(R.string.cari_nama_nomor_hp_resi)

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                keyWord = edtSearchCustomer.text.toString()
                getKasbonLunasResume()
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
            val dialog = SortingDialog(requireContext(), R.style.style_bottom_sheet, this)
            dialog.show()
        }

        ivFilter.setOnClickListener {
            val dialog = KasbonFilterDialog(requireActivity(), R.style.style_bottom_sheet, this)
            dialog.show()
        }

    }

    override fun onResume() {
        getKasbonLunasResume()
        super.onResume()
    }

    override fun onSelectedSorting(sortingBy: String) {
        sorting = sortingBy
        getKasbonLunas()
    }

    override fun onSelectedFilter(period: Int, start: String, end: String) {
        periode = period
        startDate = start
        endDate = end
        getKasbonLunas()
    }

    //search
    /*fun searchKasbon(text: String) {
        val filteredList: ArrayList<KasbonLunasResponseModel.DataBean> = ArrayList()
        for (item in kasbonLunasItems) {
            if (item.customer_name?.toLowerCase()?.contains(text.toLowerCase())!! ||
                    item.customer_phone?.toLowerCase()?.contains(text.toLowerCase())!! ||
                    item.code?.toLowerCase()?.contains(text.toLowerCase())!!) {
                filteredList.add(item)
            }
        }
        kasbonLunasAdapter?.filterList(filteredList)
    }*/


    /**
     * Start Call Api Kasbon Lunas List
     * */
    private fun getKasbonLunasResume() {
        val data = KasbonLunasRequestModel()
        data.sort_by = ""
        data.sorting = sorting
        data.keyword = keyWord
        data.start_date = startDate
        data.end_date = endDate
        data.status = "Lunas"
        data.period = periode

        kasbonPresenter.onKasbonLunas(data, requireActivity() as BaseActivity)


    }

    private fun getKasbonLunas() {
        showLoading()

        val data = KasbonLunasRequestModel()
        data.sort_by = ""
        data.sorting = sorting
        data.keyword = ""
        data.start_date = startDate
        data.end_date = endDate
        data.status = "Lunas"
        data.period = periode

        kasbonPresenter.onKasbonLunas(data, requireActivity() as BaseActivity)

    }

    override fun onSuccessKasbonLunas(result: KasbonLunasResponseModel) {
        hideLoading()

        if (result.data!!.isEmpty()) {
            //nested_scroll.visibility = View.GONE
            view_kasbon_lunas_empty.visibility = View.VISIBLE
        } else if (result.data!!.isNotEmpty()) {
            //nested_scroll.visibility = View.VISIBLE
            view_kasbon_lunas_empty.visibility = View.GONE
        }

        kasbonLunasItems.clear()
        result.data.let {
            it?.let { it1 -> kasbonLunasItems.addAll(it1) }
        }

        refreshList()
    }

    /**
     * End Call Api Kasbon Lunas List
     * */


    private fun configureList() {
        if (kasbonLunasAdapter == null)
            kasbonLunasAdapter = KasbonLunasAdapter(requireActivity(), kasbonLunasItems, object : ActionList {

                override fun clickItem(position: Int, value: Any) {

                }
            })

        rvKasbonLunas?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonLunasAdapter
        }
    }

    private fun refreshList() {
        kasbonLunasAdapter.let {
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
                    }                }
            }
        }
    }

}