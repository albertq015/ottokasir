//package id.ottopay.kasir.ui.activity.manage_product
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import id.ottopay.kasir.IConfig
//import id.ottopay.kasir.R
//import id.ottopay.kasir.model.api.request.ProductListRequestModel
//import id.ottopay.kasir.model.miscModel.ProductItemModel
//import id.ottopay.kasir.presenter.EditProductPresenter
//import id.ottopay.kasir.ui.activity.BaseLocalActivity
//import id.ottopay.kasir.ui.activity.edit_product.EditProductActivity
//import id.ottopay.kasir.ui.adapter.EditProductAdapter
//import id.ottopay.kasir.ui.fragment.manage_product.ManageProductIView
//import id.ottopay.kasir.utils.TextUtil
//import kotlinx.android.synthetic.main.activity_manage_product.*
//import kotlinx.android.synthetic.main.toolbar.*
//
//class ManageProductActivity : BaseLocalActivity(), ManageProductIView {
//
//    var presenter = EditProductPresenter(this)
//    val adapter = EditProductAdapter()
//    private var isAvailableToCall = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_manage_product)
//
//        initView()
//    }
//
//    fun initView() {
//        presenter = EditProductPresenter(this)
//        getProductNoFilter()
//
//        rvProducts.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//        rvProducts.adapter = adapter
//
//        editTextSearch.visibility = View.VISIBLE
//        btnAdd.setOnClickListener {
//            val intent = Intent(this@ManageProductActivity, EditProductActivity::class.java)
//            intent.putExtra(IConfig.IS_ADD_PRODUCT, true)
//            startActivityForResult(intent, IConfig.EDIT_PRODUCT_CODE)
//        }
//
//        backAction.setOnClickListener {
//            onBackPressed()
//        }
//        editTextSearch.addTextChangedListener(object : TextUtil() {
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (isAvailableToCall) getProductWithFilter(editTextSearch.text.toString())
//            }
//        })
//    }
//
//    override fun loadProducts(models: List<ProductItemModel?>?) {
//        pb_progressbar.visibility = View.GONE
//        adapter.onUpdateProduct(models as MutableList<ProductItemModel>)
//    }
//
//    override fun setStatusAPI(boolean: Boolean) {
//        isAvailableToCall = boolean
//    }
//
//    private fun getProductNoFilter() {
//        presenter.onGetProduct(false, "1", "", ProductListRequestModel(
//                "products.name asc"
//        ))
//    }
//
//    private fun getProductWithFilter(query: String) {
//        presenter.onGetProduct(true, "1", query, ProductListRequestModel(
//                "products.name asc"
//        ))
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == IConfig.EDIT_PRODUCT_CODE) {
//            getProductNoFilter()
//        }
//    }
//}
