package com.ottokonek.ottokasir.ui.activity.store_type

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.sync.SyncManager
import com.ottokonek.ottokasir.model.api.request.StoreTypeListRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductAddRequestModel
import com.ottokonek.ottokasir.model.api.request.StoreTypeProductRequestModel
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeListResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductAddResponseModel
import com.ottokonek.ottokasir.model.api.response.StoreTypeProductResponseModel
import com.ottokonek.ottokasir.presenter.AuthPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.presenter.store_type.StoreTypePresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.activity.auth.LoginViewInterface
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import kotlinx.android.synthetic.main.activity_store_type.*
import kotlinx.android.synthetic.main.toolbar.backAction
import kotlinx.android.synthetic.main.toolbar3.*


class StoreTypeActivity : BaseLocalActivity(), StoreTypeIView, LoginViewInterface {

    private lateinit var pin: String
    private val presenter = StoreTypePresenter(this)
    private lateinit var spinner: Spinner
    var storeTypeIdSpinner: Int = 0
    var adapter: StoreTypeProductAdapter? = null
    var count: Int? = null
    var productsAddIds: ArrayList<Int> = ArrayList()
    var allProductsIds: ArrayList<Int> = ArrayList()
    val presenterSync = BasePresenter.getInstance(this, AuthPresenter::class.java) as? AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_type)

        actionViewStoreType()
        onGetStoreTypeList()
    }


    private fun actionViewStoreType() {
        backAction.setOnClickListener {
            onBackPressed()
        }
    }


    private fun onGetStoreTypeList() {
        val storeTypeName = SessionManager.getStoreTypeName(this)
        presenter.onGetStoreTypeList(StoreTypeListRequestModel(storeTypeName), this)
    }

    override fun onSuccessGetStoreTypeList(data: StoreTypeListResponseModel) {
        hideLoading()

        if (data.baseMeta?.isStatus!!) {
            spinner = this.findViewById(R.id.spinner_store_type)

            val listStoreType: List<StoreTypeListResponseModel.DataBean> = data.data!!
            val adapter = StoreTypeSpinnerAdapter(this, listStoreType)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    storeTypeIdSpinner = parent.getItemAtPosition(position) as Int

                    allProductsIds.clear()
                    presenter.onGetStoreTypeProduct(StoreTypeProductRequestModel(storeTypeIdSpinner), this@StoreTypeActivity)
                    onCallStoreTypeAdd()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        } else {
            Toast.makeText(this, data.baseMeta!!.message, Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onSuccessGetStoreTypeProduct(data: StoreTypeProductResponseModel) {
        hideLoading()

        if (cbSelectAll.isChecked) {
            allProductsIds.clear()
            cbSelectAll.isChecked = false
        }


        if (data.baseMeta?.isStatus!!) {
            rvStoreTypeProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvStoreTypeProduct.setHasFixedSize(true)
            adapter = StoreTypeProductAdapter(this, allProductsIds, data.data!!, this)
            rvStoreTypeProduct.adapter = adapter

            if (adapter != null) {
                count = adapter!!.itemCount
                tvSelectAll.text = getString(R.string.pilih_semua_spasi) + "($count)"

                if (count == 0) {
                    lyStoreTypeProduct.visibility = View.GONE
                } else {
                    lyStoreTypeProduct.visibility = View.VISIBLE
                }
            }



            cbSelectAll.setOnClickListener {
                productsAddIds.clear()

                if (cbSelectAll.isChecked) {
                    for (model in data.data!!) {
                        model.isSelected = true

                        allProductsIds.add(model.id)
                        Log.e("IDS TRUE ALL", "$allProductsIds")

                        tvNext.setOnClickListener {
                            showLoading()
                            val request = StoreTypeProductAddRequestModel()
                            request.store_type_id = storeTypeIdSpinner
                            request.master_product_ids = allProductsIds
                            presenter.onGetsToreTypeProductAdd(request, this)
                        }
                    }
                } else if (!cbSelectAll.isChecked) {
                    for (model in data.data!!) {
                        model.isSelected = false

                        allProductsIds.clear()
                        cbSelectAll.isChecked = false
                        Log.e("IDS FALSE ALL", "$allProductsIds")
                    }
                }
                adapter?.notifyDataSetChanged()
            }


        } else {
            Toast.makeText(this, data.baseMeta!!.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun selectedItem(productsIds: ArrayList<Int>) {
        productsAddIds = productsIds
        Log.e("IDS TRUE FINALLY", "$productsAddIds")

        if (productsAddIds.count() == count) {
            cbSelectAll.isChecked = true
            onCallStoreTypeAdd()

        } else {
            cbSelectAll.isChecked = false
            allProductsIds.clear()
            onCallStoreTypeAdd()
        }
    }

    override fun unSelectedItem(productsIds: ArrayList<Int>) {
        allProductsIds.clear()
        cbSelectAll.isChecked = false
    }

    /*override fun allSelectedItem(allSelectProduct: ArrayList<Int>) {
        allProductsIds = allSelectProduct
    }*/

    private fun onCallStoreTypeAdd() {
        tvNext.setOnClickListener {
            showLoading()
            val request = StoreTypeProductAddRequestModel()
            request.store_type_id = storeTypeIdSpinner
            request.master_product_ids = productsAddIds
            presenter.onGetsToreTypeProductAdd(request, this)
        }
    }

    override fun onSuccessGetStoreTypeProductAdd(data: StoreTypeProductAddResponseModel) {
        hideLoading()

        doLoginSync()
    }

    override fun duplicateMasterProduct() {
        hideLoading()
        Toast.makeText(this, "message", Toast.LENGTH_SHORT).show()

    }


    private fun doLoginSync() {
        hideLoading()
        if (SyncManager.getSyncData() != null) {
            val data = SyncManager.changeFromRealm(SyncManager.getSyncData()!!)
            presenterSync?.doLoginSync(data, this)
        }

    }

    override fun handleDataLogin(model: LoginResponseModel) {

    }

    override fun handleDataLoginSync(model: LoginResponseModel) {
        if (model.baseMeta.code == 200) {
            var token: String = ""
            if (model.data!!.access_token != null) {
                token = model.data!!.access_token!!
            }
            val email: String = model.data!!.email!!
            val phone: String = model.data!!.phone!!
            val userId: Int = model.data!!.user_id!!
            val name: String = model.data!!.name!!
            val merchantName: String = model.data!!.merchant_name!!
            val avatar: String = model.data!!.avatar!!
            val businessCategoryName: String = model.data!!.business_category_name!!
            val merchantID: String = model.data!!.merchant_id!!
            val address: String? = model.data!!.address?.complete_address
            val province: String? = model.data!!.address?.province_name
            val storeTypeId = model.data!!.store_type_id
            Log.e("id store sync", storeTypeId.toString())
            val storeTypeName: String? = model.data!!.store_type_name
            val nmid: String? = model.data!!.nmId
            val mpan: String? = model.data!!.mpan
            val mid: String? = model.data!!.mid
            pin = SessionManager.getPIN(this)

            val intent = Intent(this, ProductListActivity::class.java)
            SessionManager.putSessionLogin(true, this)
            SessionManager.putCredential(userId, token, pin, email, phone, name, merchantName, businessCategoryName, avatar, merchantID, address, province, storeTypeId, storeTypeName, nmid, mid, mpan, this)
            SessionManager.putName(name, this)

            intent.putExtra(IConfig.IS_FROM_STORE_TYPE, true)
            intent.putExtra(IConfig.PROFILE_DATA_KEY, model.data)
            startActivity(intent)
            finish()
        }
    }


    override fun sessionExpired() {
        hideLoading()

        Toast.makeText(this, R.string.info_logout, Toast.LENGTH_LONG).show()
        SessionManager.clearSessionLogin(this)
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun handleError() {
        hideLoading()

    }

    override fun onApiFailed(title: String, error: String) {
        hideLoading()
        super.onApiFailed(title, error)
    }

    override fun handleError(message: String) {
        //super.handleError(message)
        hideLoading()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun logout() {
        hideLoading()
        super.logout()
    }

    override fun handleProcessing() {

    }
}