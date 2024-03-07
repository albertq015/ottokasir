package com.ottokonek.ottokasir.ui.fragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.dao.sync.SyncManager.changeFromRealm
import com.ottokonek.ottokasir.dao.sync.SyncManager.getSyncData
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel
import com.ottokonek.ottokasir.presenter.AuthPresenter
import com.ottokonek.ottokasir.presenter.productList.ProductListPresenter
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductlistViewInterface
import com.ottokonek.ottokasir.ui.activity.product_list.adapter.ProductAdapter
import kotlinx.android.synthetic.main.fragment_product.*
import kotlinx.android.synthetic.main.partial_cartsum.*
import java.util.*

class ProductFragment : BaseLocalFragment(), ProductlistViewInterface {

    private lateinit var title: TextView
    private lateinit var etSearch: EditText
    private lateinit var ivScan: ImageView
    private lateinit var ivClear: ImageView
    private lateinit var viewSearch: ConstraintLayout
    private lateinit var rvProduct: RecyclerView

    private val productData: List<ProductItemModel> = ArrayList()
    private var adapter: ProductAdapter? = null
    private var productListPresenter: ProductListPresenter? = null
    private var authPresenter: AuthPresenter? = null
    private var panelSlide: View? = null
    private var isAvailableToCall = true

    companion object {
        var price = ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product, null, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        title = activity!!.findViewById(R.id.title)
        title.visibility = View.GONE

        viewSearch = activity!!.findViewById(R.id.view_search)
        viewSearch.visibility = View.VISIBLE

        etSearch = activity!!.findViewById(R.id.et_search)
        etSearch.visibility = View.VISIBLE

        ivClear = activity!!.findViewById(R.id.iv_clear)
        ivClear.setOnClickListener { etSearch.setText("") }

        ivScan = activity!!.findViewById(R.id.iv_scan)
        ivScan.visibility = View.VISIBLE

        rvProduct = activity!!.findViewById(R.id.rv_product)

        (activity as ProductListActivity?)!!.setEditTextWatcher()
        initView()
    }

    private fun initView() {
        price = ""
        CartManager.removeAllCartItem()
        productListPresenter = BasePresenter.getInstance(this, ProductListPresenter::class.java) as ProductListPresenter
        adapter = ProductAdapter(requireActivity())
        adapter!!.setListener { productListPresenter!!.toggleFooter() }

        rvProduct.adapter = adapter
        rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        productListPresenter!!.toggleFooter()

        panelSlide = View(activity)

        srl_prodlist!!.setOnRefreshListener {
            if (CacheUtil.getPreferenceBoolean(IConfig.SESSION_IS_SYNC, requireContext())) {
                callProductlistNofilter()
            } else {
                syncLogin()
            }
        }
        clFooter!!.setOnClickListener { view1: View? -> (activity as ProductListActivity?)!!.showBottomPanel() }
        if (CacheUtil.getPreferenceBoolean(IConfig.SESSION_IS_SYNC, requireContext())) {
            callProductlistNofilter()
        } else {
            syncLogin()
        }
    }

    private fun syncLogin() {
        if (getSyncData() != null) {
            showProgressbar()
            val data = changeFromRealm(getSyncData()!!)
            productListPresenter!!.syncLogin(data, (activity as ProductListActivity?)!!)
        }
    }

    override fun loadProducts(models: List<ProductItemModel>) {
        clearProducts()
        if (models.isEmpty()) {
            pb_progressbar!!.visibility = View.GONE
            view_product_empty!!.visibility = View.VISIBLE
            view_product_notEmpty!!.visibility = View.GONE
        } else {
            view_product_empty!!.visibility = View.GONE
            view_product_notEmpty!!.visibility = View.VISIBLE
            adapter!!.fillModels(models)
            adapter!!.setListener { productListPresenter!!.toggleFooter() }
            pb_progressbar!!.visibility = View.GONE
            if (srl_prodlist!!.isRefreshing) {
                srl_prodlist!!.isRefreshing = false
            }
        }
    }

    override fun addProduct(models: List<ProductItemModel>) {
        adapter!!.addModels(models)
        if (srl_prodlist!!.isRefreshing) {
            srl_prodlist!!.isRefreshing = false
        }
    }

    override fun clearProducts() {
        adapter!!.removeModels()
        pb_progressbar!!.visibility = View.VISIBLE
    }

    override fun showProgressbar() {
        pb_progressbar!!.visibility = View.VISIBLE
    }

    override fun hideProgressbar() {
        pb_progressbar!!.visibility = View.GONE
    }

    override fun callProductlistNofilter() {
        showProgressbar()
        productListPresenter!!.onGetProduct(false, "1", "", ProductListRequestModel(
                "p_name desc", "", "")
        )
    }

    override fun callProductlistFiltered(query: String, category: String) {
        showProgressbar()
        productListPresenter!!.onGetProduct(true, "1", query, ProductListRequestModel(
                "p_name desc", query, ""
        ))
    }

    override fun onPause() {
        super.onPause()
        productListPresenter?.onClear()
        authPresenter?.onDestroy()
    }

    override fun hideLoading() {
        super.hideLoading()
        hideProgressbar()
        srl_prodlist!!.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        productListPresenter!!.onDestroy()
        (activity as ProductListActivity?)!!.removeEditTextWatcher()
        etSearch.setText("")
    }

    override fun showFooter() {
        fl_footer!!.visibility = View.VISIBLE
        clFooter!!.background = resources.getDrawable(R.drawable.rounded_rectangle_blue)
        val outValue = TypedValue()
        activity!!.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
        clFooter!!.isEnabled = true
        clFooter!!.isClickable = true
        fl_footer!!.setBackgroundResource(outValue.resourceId)
        tv_action!!.visibility = View.VISIBLE
    }

    override fun hideFooter() {
        clFooter!!.background = resources.getDrawable(R.drawable.rounded_rectangle_grey)
        clFooter!!.isEnabled = false
        tv_action!!.visibility = View.GONE
    }

    override fun refreshView() {
        callProductlistNofilter()
    }

    override fun addFooterData(itemCount: String, total: String, discount: String) {
        price = total
        tv_total.text = total
    }

    override fun showClearSearch() {
        ivClear.visibility = View.VISIBLE
    }

    override fun hideClearSearch() {
        ivClear.visibility = View.GONE
    }

    override fun showToast(msg: String) {
        if (msg.contains("Failed to connect to ") || msg.contains("Unable to resolve host")) {
            Toast.makeText(context, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getCurrentActivity(): BaseActivity {
        return activity as BaseActivity
    }

    override fun setStatusAPI(status: Boolean) {
        isAvailableToCall = status
    }

    override fun getStatusAPI(): Boolean {
        return isAvailableToCall
    }
}