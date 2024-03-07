package com.ottokonek.ottokasir.ui.fragment.manage_product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CheckVersionRequestModel
import com.ottokonek.ottokasir.model.api.request.ProductListRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckVersionResponseModel
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel
import com.ottokonek.ottokasir.presenter.CheckVersionPresenter
import com.ottokonek.ottokasir.presenter.EditProductPresenter
import com.ottokonek.ottokasir.ui.activity.edit_product.EditProductActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.adapter.EditProductAdapter
import com.ottokonek.ottokasir.ui.dialog.force_update.ForceUpdateDialog
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment
import kotlinx.android.synthetic.main.fragment_manage_product.*
import kotlinx.android.synthetic.main.fragment_manage_product.view.*

class ManageProductFragment : BaseLocalFragment(), ManageProductIView, CheckVersionPresenter.ICheckVersionView {

    private val checkVersionPresenter = CheckVersionPresenter(this)
    var presenter = EditProductPresenter(this)
    val adapter = EditProductAdapter(this@ManageProductFragment)
    lateinit var mView: View
    private var isAvailableToCall = true

    private lateinit var etSearch: EditText
    private lateinit var ivClearSearch: ImageView

    private var buildNumber = ""
    private var numberSunmi = "V1s-G"
    private var numberAdvan = "O1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_manage_product, container, false)

        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        search.visibility = View.VISIBLE

        val tvTitle = activity!!.findViewById<TextView>(R.id.title)
        tvTitle.visibility = View.GONE

        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)
        scanBarcode.visibility = View.VISIBLE

        ivClearSearch = activity!!.findViewById(R.id.iv_clear)
        ivClearSearch.setOnClickListener {
            etSearch.setText("")
        }

        etSearch = activity!!.findViewById(R.id.et_search)
        (activity as ProductListActivity?)!!.setEditTextWatcher()
        etSearch.visibility = View.VISIBLE

        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentManageProduct()
        actionManageProduct()
        validationDevice()
    }

    private fun validationDevice() {

        buildNumber = Build.MODEL.toString()

        when (buildNumber) {
            numberSunmi -> {
                //do
            }
            numberAdvan -> {
                //do
            }
            else -> {
                callApiCheckVersion()
            }
        }
    }


    private fun contentManageProduct() {
        presenter = EditProductPresenter(this)
        getProductNoFilter()

        mView.rvProducts.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        mView.rvProducts.adapter = adapter
    }

    private fun actionManageProduct() {
        srl_manage_product.setOnRefreshListener {
            getProductNoFilter()
        }


        mView.btnAdd.setOnClickListener {
            val intent = Intent(requireActivity(), EditProductActivity::class.java)
            intent.putExtra(IConfig.IS_ADD_PRODUCT, true)
            startActivityForResult(intent, IConfig.EDIT_PRODUCT_CODE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as ProductListActivity?)!!.removeEditTextWatcher()
        etSearch.setText("")
    }

    override fun onResume() {
        super.onResume()
        onResumeProduct()
        validationDevice()

    }

    private fun onResumeProduct() {
        if (etSearch.text.toString() == "") {
            getProductNoFilter()
        }
    }

    override fun loadProducts(models: List<ProductItemModel?>?) {
        srl_manage_product.isRefreshing = false

        if (models?.isEmpty()!!) {
            mView.pb_progressbar.visibility = View.GONE
            view_product_empty.visibility = View.VISIBLE
            view_product_notEmpty.visibility = View.GONE
        } else {
            view_product_empty.visibility = View.GONE
            view_product_notEmpty.visibility = View.VISIBLE
            mView.pb_progressbar.visibility = View.GONE
            adapter.onUpdateProduct(models as MutableList<ProductItemModel>)
        }

    }

    override fun setStatusAPI(boolean: Boolean) {
        isAvailableToCall = boolean
    }

    override fun getStatusAPI(): Boolean {
        return isAvailableToCall
    }

    override fun showClearSearch() {
        ivClearSearch.visibility = View.VISIBLE
    }

    override fun hideClearSearch() {
        ivClearSearch.visibility = View.GONE
    }

    private fun getProductNoFilter() {
        presenter.onGetProduct(false, "1", "", ProductListRequestModel(
                "products.name asc", "", ""
        ))
    }

    override fun getProductWithFilter(query: String) {
        presenter.onGetProduct(true, "1", query, ProductListRequestModel(
                "products.name asc", query, ""
        ))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IConfig.EDIT_PRODUCT_CODE) {
            getProductWithFilter(etSearch.text.toString())
        }
        else{

        }
    }

    override fun showToast(msg: String?) {

        if (msg.toString().contains("Failed to connect to ") || msg.toString().contains("Unable to resolve host")) {
            Toast.makeText(context, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun hideLoading() {
        super.hideLoading()
        //srl_manage_product.isRefreshing = false
        mView.pb_progressbar.visibility = View.GONE
    }

    /**
     * ================= CHECK VERSION =================
     * */
    private fun callApiCheckVersion() {
        val data = CheckVersionRequestModel()
        data.version_app = BuildConfig.VERSION_CODE

        checkVersionPresenter.onCheckVersion(data, requireActivity() as BaseActivity)
    }

    override fun onSuccessCheckVersion(result: CheckVersionResponseModel) {

        val forceUpdate = result.data!!.force_update
        if (forceUpdate) {
            ForceUpdateDialog.showDialog(requireActivity(), object : ForceUpdateDialog.ActionDialog {
                override fun onForceUpdate() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ottokonek.ottokasir")))
                }
            })
        }
    }


}
