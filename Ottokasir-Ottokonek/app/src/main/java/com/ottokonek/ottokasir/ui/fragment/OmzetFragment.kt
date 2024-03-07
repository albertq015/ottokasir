package com.ottokonek.ottokasir.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.IConfig

import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.AvailableOmzetResponseModel
import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.presenter.OrderPresenter
import kotlinx.android.synthetic.main.fragment_omzet.*
import com.ottokonek.ottokasir.ui.activity.history.TransactionHistoryActivity

class OmzetFragment : BaseLocalFragment(), OmzetIView {

    val presenter: OrderPresenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_omzet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        callAPI()
    }

    override fun setDailyOmzet(model: DailyOmzetResponseModel) {
        var amount = 0
        model.data?.transactions?.forEach {
            amount = amount + it.amount
        }
        tvDailyOmzet.text = amount.toString()
    }

    override fun setAvailableOmzet(model: AvailableOmzetResponseModel) {
        hideLoading()
        if (tvAvailableOmzet != null) {
            if (model.data?.omzet != null && !model.data?.omzet.equals("")) {
                tvAvailableOmzet.text = model.data?.omzet.toString()
            } else {
                tvAvailableOmzet.text = "0"
            }
            if (model.data != null) {
                tvDailyOmzet.text = model.data?.omzetDaily!!
            }
            hideLoading()
        }
    }

    override fun handleError(s: String) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    fun initContent() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)
        scanBarcode.visibility = View.GONE

        title.visibility = View.VISIBLE
        title.text = getString(R.string.omzet)
        search.visibility = View.GONE

        tvDetail.setOnClickListener {
            val intent = Intent(requireActivity(), TransactionHistoryActivity::class.java)
            intent.putExtra(IConfig.IS_FROM_OMZET, true)
            startActivity(intent)
        }
    }

    fun callAPI() {
        showLoading()
        presenter.getAvailableOmzet(requireActivity() as BaseActivity)
    }

    override fun getCurrentActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }

    override fun onPause() {
        super.onPause()
        presenter.onDestroy()
    }
}
