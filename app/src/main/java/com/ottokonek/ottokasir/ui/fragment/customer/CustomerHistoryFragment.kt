package com.ottokonek.ottokasir.ui.fragment.customer

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.transaction.TransactionManager
import com.ottokonek.ottokasir.model.api.request.CustomerHistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import com.ottokonek.ottokasir.model.db.TransactionModel
import com.ottokonek.ottokasir.presenter.CustomerPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.adapter.TransactionAdapter
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerFilterCallback
import com.ottokonek.ottokasir.ui.dialog.customer.CustomerFilterHistoryDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import com.ottokonek.ottokasir.utils.MessageUserUtil
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_customer_history.*

class CustomerHistoryFragment : BaseLocalFragment(), CustomerFilterCallback, CustomerPresenter.ICustomerHistoryView {

    private var customerId = 0
    private var typeTransaction = ""
    private var startDate = ""
    private var endDate = ""

    private val customerPresenter = CustomerPresenter(this)
    private lateinit var transactionHistory: RealmResults<TransactionModel>
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_customer_history, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tab, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {

            val dialog = CustomerFilterHistoryDialog(requireActivity(), R.style.style_bottom_sheet, this)
            dialog.show()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentCustomerHistory()

        getHistoryTransaction()

    }


    private fun contentCustomerHistory() {
        customerId = CacheUtil.getPreferenceInteger(IConfig.KEY_CUSTOMER_ID,
                requireActivity() as BaseActivity)

        rvHistoryCustomer.setHasFixedSize(true)
        rvHistoryCustomer.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        adapter = TransactionAdapter(requireActivity())
        rvHistoryCustomer.adapter = adapter
    }


    override fun onFilterCustomer(type: String, start: String, end: String) {
        typeTransaction = type
        startDate = start
        endDate = end

        getHistoryTransaction()
    }


    /**
     * Start Call Api History Customer
     * */
    private fun getHistoryTransaction() {
        showLoading()

        val data = CustomerHistoryRequestModel()
        data.customer_id = customerId
        data.payment_type = typeTransaction
        data.date_start = startDate
        data.date_end = endDate

        customerPresenter.onCustomerHistory(data, requireActivity() as BaseActivity)
    }


    override fun onSuccessHistoryTransaction(result: TransactionHistoryResponse) {
        hideLoading()

        saveToRealm(result)
        transactionHistory = TransactionManager.transactionData as RealmResults<TransactionModel>
        adapter.setData(transactionHistory, transactionHistory.size)

        if (result.data == null) {
            //Toast.makeText(requireActivity(), "Tidak ada transaksi $typeTransaction", Toast.LENGTH_SHORT).show()
        }
    }

    override fun saveToRealm(result: TransactionHistoryResponse) {

        customerPresenter.saveToRealm(result)

    }

    /**
     * End Call Api History Customer
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