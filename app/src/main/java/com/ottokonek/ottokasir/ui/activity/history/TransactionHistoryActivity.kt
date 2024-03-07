package com.ottokonek.ottokasir.ui.activity.history

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.transaction.TransactionManager
import com.ottokonek.ottokasir.model.api.request.HistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import com.ottokonek.ottokasir.model.db.OrderItemRealmModel
import com.ottokonek.ottokasir.model.db.TransactionModel
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.adapter.TransactionAdapter
import com.ottokonek.ottokasir.utils.TextUtil
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.toolbar.*


class TransactionHistoryActivity : BaseLocalActivity(), TransactionIView {

    private var page = 1
    private lateinit var orderData: RealmResults<OrderItemRealmModel>
    val presenter: OrderPresenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter
    private lateinit var transactionHistory: RealmResults<TransactionModel>
    private lateinit var adapter: TransactionAdapter

    //    private dialog Loading
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        initContent()

        callAPI()
        if (intent.getBooleanExtra(IConfig.IS_FROM_OMZET, false)) {
            //showLoading()
            callAPI()
            //transactionHistory = TransactionManager.transactionData as RealmResults<TransactionModel>
            //adapter.setData(transactionHistory, transactionHistory.size)
        } else {
            transactionHistory = TransactionManager.transactionData as RealmResults<TransactionModel>
            adapter.setData(transactionHistory, transactionHistory.size)
        }
        refreshLayout.isRefreshing = false
        refreshLayout.setOnRefreshListener {
            refreshLayout.isRefreshing = true
            callAPI()
        }
    }

    override fun handleSuccess(data: TransactionHistoryResponse) {
        hideLoading()
        if (data.data?.size != 0) {
            saveToRealm(data)
            transactionHistory = TransactionManager.transactionData as RealmResults<TransactionModel>
            adapter.setData(transactionHistory, transactionHistory.size)
        }

        refreshLayout.isRefreshing = false
    }

   /* override fun handleError(s: String) {
        if (s != null) {
            Toast.makeText(this, s, Toast.LENGTH_LONG).show()
        }
        refreshLayout.isRefreshing = false
    }*/

    override fun handleError(message: String) {
        super.handleError(message)
    }

    override fun saveToRealm(data: TransactionHistoryResponse) {
        if (data != null) {
            presenter.saveToRealm(data)
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.onDestroy()
    }

    override fun handleSuccessLoadMore(data: DailyOmzetResponseModel) {
        pbLoadMore.visibility = View.GONE
        if (data != null) {
            TransactionManager.updateTransaction(data.data)
            orderData = TransactionManager.orderData as RealmResults<OrderItemRealmModel>
            rvTransactionHistory.adapter?.notifyDataSetChanged()
        }

    }

    override fun sessionExpired() {
        Toast.makeText(this, R.string.info_logout, Toast.LENGTH_LONG).show()
        SessionManager.clearSessionLogin(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun initContent() {
        tvTitle.text = getString(R.string.daftar_transaksi)
        ivSearch.visibility = View.VISIBLE
        backAction.setOnClickListener {
            onBackPressed()
        }

        rvTransactionHistory.setHasFixedSize(true)
        rvTransactionHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = TransactionAdapter(this)
        rvTransactionHistory.adapter = adapter

        ivSearch.setOnClickListener {
            val upBottom = AnimationUtils.loadAnimation(this, R.anim.slide_down_component)
            val bottomUp = AnimationUtils.loadAnimation(this, R.anim.slide_up_component)
            if (searchContainer.visibility == View.GONE) {
                searchContainer.startAnimation(upBottom)
                searchContainer.visibility = View.VISIBLE
            } else {
                searchContainer.startAnimation(bottomUp)
                searchContainer.visibility = View.GONE
            }
        }

        tvTesting.setOnClickListener {
            if (TransactionManager.getTransactionBySearch(et_search.text.toString()) != null) {
                transactionHistory = TransactionManager.getTransactionBySearch(et_search.text.toString())!!
                adapter.setData(transactionHistory, transactionHistory.size)
            } else {
                transactionHistory = ArrayList<TransactionModel>() as RealmResults<TransactionModel>
                adapter.setData(transactionHistory, transactionHistory.size)
            }
        }

        et_search.addTextChangedListener(object : TextUtil() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TransactionManager.getTransactionBySearch(et_search.text.toString()) != null) {
                    transactionHistory = TransactionManager.getTransactionBySearch(et_search.text.toString())!!
                    adapter.setData(transactionHistory, transactionHistory.size)
                } else {
                    transactionHistory = ArrayList<TransactionModel>() as RealmResults<TransactionModel>
                    adapter.setData(transactionHistory, transactionHistory.size)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (et_search.text.toString() != ""){
                    iv_clear.visibility = View.VISIBLE
                }else{
                    iv_clear.visibility = View.GONE
                }
            }
        })

        iv_clear.setOnClickListener {
            et_search.setText("")
        }
    }


    private fun callAPI() {
        showLoading()
        val request = HistoryRequestModel()
        request.date_start = ""
        request.date_end = ""
        presenter.getTransactionHistory(this, request)
    }

}
