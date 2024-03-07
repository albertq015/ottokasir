package com.ottokonek.ottokasir.ui.fragment.kasbon

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.KasbonAktifRequestModel
import com.ottokonek.ottokasir.model.api.request.KasbonAktifSelectedRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonAktifResponseModel
import com.ottokonek.ottokasir.model.api.response.KasbonAktifSelectedResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.kasbon.KasbonDetailTransactionActivity
import com.ottokonek.ottokasir.ui.adapter.KasbonAktifAdapter
import com.ottokonek.ottokasir.ui.callback.SelectedItemKasbonAktif
import com.ottokonek.ottokasir.ui.dialog.SortingDialog
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonFilterCallback
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonFilterDialog
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonWarningCallback
import com.ottokonek.ottokasir.ui.dialog.kasbon.KasbonWarningDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.ActivityUtil
import com.ottokonek.ottokasir.utils.MessageUserUtil
import kotlinx.android.synthetic.main.fragment_kasbon_aktif.*
import kotlinx.android.synthetic.main.partial_filter_sorting.*
import java.util.*

class KasbonAktifFragment : BaseLocalFragment(), KasbonPresenter.IKasbonAktifView, KasbonPresenter.IKasbonAktifSelectedView, SortingDialog.ISortingView, KasbonFilterCallback, KasbonWarningCallback {


    private val kasbonPresenter = KasbonPresenter(this)
    private var kasbonAktifAdapter: KasbonAktifAdapter? = null
    private var orderIdsItemKasbon: ArrayList<String> = ArrayList()
    private var kasbonAktifItems = ArrayList<KasbonAktifResponseModel.DataBean>()
    private var isFormValidationSuccess = false

    private var sorting = "DESC"
    private var keyWord = ""
    private var startDate = ""
    private var endDate = ""
    private var periode = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kasbon_aktif, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionKasbonAktif()

        getKasbonAktif()

        configureList()
    }


    private fun actionKasbonAktif() {

        edtSearchCustomer.hint = getString(R.string.cari_nama_nomor_hp_resi)

        edtSearchCustomer.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                keyWord = edtSearchCustomer.text.toString()
                getKasbonAktifResume()

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

        btnNext.setOnClickListener {
            if (isFormValidationSuccess) {
                getKasbonAktifSelected()
            }
        }

    }


    override fun onResume() {
        orderIdsItemKasbon.clear()
        isFormValidationSuccess = false
        btnNext.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_rectangle_grey)
        getKasbonAktifResume()
        super.onResume()
    }


    override fun onSelectedSorting(sortingBy: String) {
        sorting = sortingBy
        getKasbonAktif()
    }

    override fun onSelectedFilter(period: Int, start: String, end: String) {
        periode = period
        startDate = start
        endDate = end
        getKasbonAktif()
    }


    /**
     * Call Api Kasbon Aktif List
     * */
    private fun getKasbonAktifResume() {
        val data = KasbonAktifRequestModel()
        data.sort_by = ""
        data.sorting = sorting
        data.keyword = keyWord
        data.start_date = startDate
        data.end_date = endDate
        data.status = "Aktif"
        data.period = periode

        kasbonPresenter.onKasbonAktif(data, requireActivity() as BaseActivity)
    }

    private fun getKasbonAktif() {
        showLoading()

        val data = KasbonAktifRequestModel()
        data.sort_by = ""
        data.sorting = sorting
        data.keyword = ""
        data.start_date = startDate
        data.end_date = endDate
        data.status = "Aktif"
        data.period = periode

        kasbonPresenter.onKasbonAktif(data, requireActivity() as BaseActivity)
    }


    override fun onSuccessKasbonAktif(result: KasbonAktifResponseModel) {
        hideLoading()

        if (result.data!!.isEmpty()) {
            btnNext.visibility = View.GONE
            view_kasbon_aktif_empty.visibility = View.VISIBLE
        } else {
            view_kasbon_aktif_empty.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
        }


        kasbonAktifItems.clear()
        result.data.let {
            it?.let { it1 -> kasbonAktifItems.addAll(it1) }
        }

        refreshList()
    }


    /**
     * Call Api Kasbon Aktif Selected
     * */
    private fun getKasbonAktifSelected() {
        showLoading()

        val data = KasbonAktifSelectedRequestModel()
        data.order_ids = orderIdsItemKasbon

        kasbonPresenter.onKasbonAktifSelected(data, requireActivity() as BaseActivity)
    }


    override fun onSuccessKasbonAktifSelected(result: KasbonAktifSelectedResponseModel) {
        hideLoading()

        if (isFormValidationSuccess) {
            val bundle = Bundle()
            bundle.putBoolean(KasbonDetailTransactionActivity.KEY_ACTIVITY_FROM_KASBON_AKTIF, true)
            bundle.putStringArrayList(KasbonDetailTransactionActivity.KEY_ORDER_IDS_KASBON_SELECTED, orderIdsItemKasbon)
            ActivityUtil(requireActivity())
                    .sendData(bundle)
                    .showPage(KasbonDetailTransactionActivity::class.java)
        }

    }

    override fun onDuplicateSelected() {
        hideLoading()
        val dialog = KasbonWarningDialog(requireActivity(), R.style.style_bottom_sheet, this)
        dialog.show()
    }

    override fun onOkClearOrderIdsSelected() {
        orderIdsItemKasbon.clear()
        isFormValidationSuccess = false
        btnNext.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_rectangle_grey)
        getKasbonAktifResume()
    }


    private fun configureList() {
        orderIdsItemKasbon.clear()
        if (kasbonAktifAdapter == null)
            kasbonAktifAdapter = KasbonAktifAdapter(kasbonAktifItems, object : SelectedItemKasbonAktif {


                override fun orderIdsItemKasbonAktif(orderIdsKasbon: ArrayList<String>) {

                    if (orderIdsKasbon.isNotEmpty()) {
                        orderIdsItemKasbon = orderIdsKasbon
                        isFormValidationSuccess = true
                        btnNext.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_fill_blue)
                    } else {
                        orderIdsItemKasbon.clear()
                        isFormValidationSuccess = false
                        btnNext.background = ContextCompat.getDrawable(requireActivity(), R.drawable.rounded_rectangle_grey)
                    }
                }
            })

        rvKasbonAktif?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonAktifAdapter
        }
    }

    private fun refreshList() {
        kasbonAktifAdapter.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureList()
        }
    }



    override fun handleError(message: String) {
        hideLoading()

        when (message) {
            "Bulk payments can be processed by the same customer only!" -> {
                onDuplicateSelected()
            }
            "Invalid request token!" -> {
                MessageUserUtil.shortMessage(requireActivity(), message)
                SessionManager.logoutDevice(requireContext())
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