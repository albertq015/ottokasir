package com.ottokonek.ottokasir.ui.activity.edit_product

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.support.util.CacheUtil
import com.bumptech.glide.Glide
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.IConfig.Companion.IS_ADD_PRODUCT
import com.ottokonek.ottokasir.IConfig.Companion.PRODUCT_DATA
import com.ottokonek.ottokasir.IConfig.Companion.PRODUCT_TYPE
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.CreateProductRequestModel
import com.ottokonek.ottokasir.model.api.request.DeleteProductRequestModel
import com.ottokonek.ottokasir.model.api.request.MasterProductRequestModel
import com.ottokonek.ottokasir.model.api.request.UpdateProductRequestModel
import com.ottokonek.ottokasir.model.api.response.CreateProductResponse
import com.ottokonek.ottokasir.model.api.response.DeleteProductResponse
import com.ottokonek.ottokasir.model.api.response.MasterProductResponseModel
import com.ottokonek.ottokasir.model.api.response.UpdateProductResponse
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel
import com.ottokonek.ottokasir.presenter.EditProductPresenter
import com.ottokonek.ottokasir.presenter.MasterProductPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.scan_barcode.ScanBarcodeActivity
import com.ottokonek.ottokasir.ui.adapter.MasterProductAdapter
import com.ottokonek.ottokasir.ui.dialog.delete.DeleteConfirmationCallback
import com.ottokonek.ottokasir.ui.dialog.delete.DeleteConfirmationDialog
import com.ottokonek.ottokasir.ui.dialog.stock.InformationStockDialog
import com.ottokonek.ottokasir.ui.dialog.stock.InformationStockMinimumDialog
import com.ottokonek.ottokasir.ui.dialog.success_edit_dialog.SuccessDialogCallback
import com.ottokonek.ottokasir.ui.dialog.success_edit_dialog.SuccessEditDialog
import com.ottokonek.ottokasir.utils.*
import com.ottokonek.ottokasir.utils.screnshoot.HtmlUtils
import kotlinx.android.synthetic.main.activity_edit_product.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.FileOutputStream


class EditProductActivity : BaseLocalActivity(), EditProductIView, DeleteConfirmationCallback, SuccessDialogCallback, MasterProductIView {

    val TAG = EditProductActivity::class.java.simpleName
    private var mTextWatcher: TextWatcher? = null

    //Kategori Product Food/Service
    private var isProduct = true //true=food : else=service
    private var isCategoryProductService = true //true=service : else=food
    var suggestAddMasterProduct = true //true=add : else:not add

    //Suggestion Add Master Product
    private var presenterMasterProduct = MasterProductPresenter(this)
    var adapter: MasterProductAdapter? = null
    private var masterProductId: String? = ""

    private var isAddProduct: Boolean? = null
    var productType: String? = "Product" //"Product" or "MasterProduct"

    private lateinit var textWatcher: TextWatcher
    private var resultScanBarcode: String? = null

    private lateinit var productData: ProductItemModel
    private val presenter = EditProductPresenter(this)
    private var isShouldUpdate = false

    private var fileImageBase64 = ""

    private var stockAlertCopy = 0
    private var stockProductCopy = 0
    private var stockActiveCopy = false

    private var stockAlert = 0
    private var stockProduct = 0
    private var stockActive = false

    var priceValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        tvCancel.visibility = VISIBLE
        //presenter.onValidateNameAndPrice(etProductName, etPrice)

        /*val input = this.findViewById<EditText>(R.id.etPrice)
        input.keyListener = DigitsKeyListener.getInstance("0123456789.,")*/
        //setEditTextWatcherPrice()
        etPrice.setSelection(etPrice.text!!.length)
        addTextWatcherPrice(etPrice)

        productType = CacheUtil.getPreferenceString(PRODUCT_TYPE, this)
        contentManageProduct()


        val extras = intent.extras
        if (extras != null) {
            isAddProduct = extras.getBoolean(IS_ADD_PRODUCT, false)
        }

        if (isAddProduct == true) {
            tvTitle.text = getString(R.string.tambah_produk)

            btnSave.setOnClickListener {
                addNewProduct()
            }

            tvService.setOnClickListener {
                switchProductType(false)
            }

            tvProduct.setOnClickListener {
                switchProductType(true)
            }
            setEditTextWatcher()

            productType = "Product"
            suggestAddMasterProduct = false
           onScanBarcode()
            onClearBarcode()

            lyPhotoProduct.setOnClickListener {
                selectImage(this)
            }

        }
        else if (productType == "Product") {
            btnDelete.visibility = VISIBLE
            tvTitle.text = getString(R.string.ubah_produk)
            productData = intent?.getParcelableExtra(PRODUCT_DATA)!!
            btnDelete.visibility = VISIBLE
            setProductData()

            if (productData.category.equals("food")) {
                switchProductType(true)
            } else {
                switchProductType(false)
            }
            btnSave.setOnClickListener {
                updateProduct()
            }

            tvService.setOnClickListener {
                switchProductType(false)
            }

            tvProduct.setOnClickListener {
                switchProductType(true)
            }
            setEditTextWatcher()

            onScanBarcode()
            onClearBarcode()

            lyPhotoProduct.setOnClickListener {
                selectImage(this)
            }

        }
        else if (productType == "MasterProduct") {
            disableForEditMasterProduct()

            btnDelete.visibility = VISIBLE
            tvTitle.text = "Edit Product"
            productData = intent?.getParcelableExtra(PRODUCT_DATA)!!
            btnDelete.visibility = VISIBLE
            setProductData()

            if (productData.category.equals("food")) {
                switchProductType(true)
                tvService.background = resources.getDrawable(R.drawable.rounded_grey_color, null)
            } else {
                switchProductType(false)
                tvProduct.background = resources.getDrawable(R.drawable.rounded_grey_color, null)
            }

            btnSave.setOnClickListener {
                updateMasterProduct()
            }

            lyPhotoProduct.setOnClickListener {
                Toast.makeText(this, getString(R.string.tidak_bisa_mengganti_foto_master_produk), Toast.LENGTH_SHORT).show()
            }
        }

        //etPrice.showSymbol(true)

        backAction.setOnClickListener {
            onBackPressed()
        }

        tvCancel.setOnClickListener {
            onBackPressed()
        }

        btnDelete.setOnClickListener {
            val dialog = DeleteConfirmationDialog(this, R.style.dialog2, this)
            dialog.show()
        }

        enableStockValidation()

        tvInformationStock.setOnClickListener {
            val dialog = InformationStockDialog(this, R.style.style_bottom_sheet)
            dialog.show()
        }

        tvInformationStockMinimum.setOnClickListener {
            val dialog = InformationStockMinimumDialog(this, R.style.style_bottom_sheet)
            dialog.show()
        }

        validationNamePriceStock()
        validationNamePrice()
    }


    fun contentManageProduct() {
        etProductName.setSelection(etProductName.text!!.length)
        etPrice.setSelection(etPrice.text!!.length)
        tvTitleProductName.text = HtmlUtils.getHTMLContent(getString(R.string.nama_produk_required))
        tvTitleProductPrice.text = HtmlUtils.getHTMLContent(getString(R.string.harga_jual_required))
        tvTitleStockBarang.text = HtmlUtils.getHTMLContent(getString(R.string.stok_required))
    }

    private fun enableStockValidation() {
        addTextWatcher(etProductName)
        addTextWatcher(etPrice)
        addTextWatcher(etStockProduct)
        addTextWatcher(etStockProductDummy)

        enableStock.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                stockActive = true
                etStockProductDummy.setText("1")
                tvStatusStock.text = getString(R.string.aktif)
                tvStatusStock.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                lyStock.visibility = VISIBLE
            } else {
                etStockProductDummy.setText("")
                stockActive = false
                tvStatusStock.text = getString(R.string.tidak_aktif)
                tvStatusStock.setTextColor(ContextCompat.getColor(this, R.color.warm_grey))
                lyStock.visibility = GONE
            }
        }
    }


    @SuppressLint("SetTextI18n")
    fun setProductData() {
        val price = (productData.price).toDouble()
        val photoProduct = productData.image
        stockProductCopy = SessionManager.getStocks(this)
        stockAlertCopy = SessionManager.getStocksAlert(this)
        stockActiveCopy = SessionManager.getIsStockActive(this)

        if (stockActiveCopy) {
            etStockProductDummy.setText("1")
            stockActive = true
            enableStock.isChecked = true
            tvStatusStock.text = getString(R.string.aktif)
            tvStatusStock.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            lyStock.visibility = VISIBLE

        } else if (!stockActiveCopy) {
            etStockProductDummy.setText("")
            stockActive = false
            enableStock.isChecked = false
            tvStatusStock.text = getString(R.string.tidak_aktif)
            tvStatusStock.setTextColor(ContextCompat.getColor(this, R.color.warm_grey))
            lyStock.visibility = GONE
        }


        if (photoProduct != "") {

            //INI YANG BIKIN BLACK SCREEN
            //fileImageBase64 = Base64ImageUtil.convertUrlToBase64(photoProduct).toString()


            ivHolderPhoto.visibility = GONE
            ivHolderText.visibility = GONE
            ivProduct.visibility = VISIBLE

            Glide.with(this)
                    .load(photoProduct)
                    .centerCrop()
                    .into(ivProduct)
        }

        etStockAlert.setText((stockAlertCopy).toString())
        etStockProduct.setText((stockProductCopy).toString())
        etProductName.setText(productData.name)
        tvResultBarcode.setText(productData.barcode)

        if (productData.price == "-1.00") {
            etPrice.hint = "Example : 5,000"
        } else {
            etPrice.setText((price.toString()))
        }

    }

    fun addNewProduct() {

        val data = CreateProductRequestModel()
        val stockAlertStr = etStockAlert.text.toString()
        val stockProductStr = etStockProduct.text.toString()

        if (stockAlertStr != "" && stockProductStr != "") {
            stockAlert = etStockAlert.text.toString().toInt()
            stockProduct = etStockProduct.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()
                data.category = if (isProduct) 0 else 1
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.price = priceValue.toDouble()
                data.master_product_id = masterProductId
                data.barcode = tvResultBarcode.text.toString()
                data.stocks = stockProduct
                data.show_alert = true
                data.stock_alert = stockAlert
                data.stock_active = stockActive

                presenter.onCreateProduct(data, this)
            }
        } else if (stockAlertStr != "" && stockProductStr == "") {
            stockAlert = etStockAlert.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()
                data.category = if (isProduct) 0 else 1
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.price = priceValue.toDouble()
                data.master_product_id = masterProductId
                data.barcode = tvResultBarcode.text.toString()
                data.stocks = stockProduct
                data.show_alert = true
                data.stock_alert = stockAlert
                data.stock_active = stockActive

                presenter.onCreateProduct(data, this)
            }

        } else if (stockProductStr != "" && stockAlertStr == "") {
            stockProduct = etStockProduct.text.toString().toInt()

            showLoading()
            data.category = if (isProduct) 0 else 1
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.price = priceValue.toDouble()
            data.master_product_id = masterProductId
            data.barcode = tvResultBarcode.text.toString()
            data.stocks = stockProduct
            data.stock_active = stockActive

            presenter.onCreateProduct(data, this)

        } else {
            showLoading()
            data.category = if (isProduct) 0 else 1
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.price = priceValue.toDouble()
            data.master_product_id = masterProductId
            data.barcode = tvResultBarcode.text.toString()
            data.stocks = stockProduct
            data.show_alert = false
            data.stock_alert = stockAlert
            data.stock_active = stockActive

            presenter.onCreateProduct(data, this)
        }
    }

    fun updateProduct() {

        val data = UpdateProductRequestModel()
        val stockAlertStr = etStockAlert.text.toString()
        val stockProductStr = etStockProduct.text.toString()

        if (stockAlertStr != "" && stockProductStr != "") {
            stockAlert = etStockAlert.text.toString().toInt()
            stockProduct = etStockProduct.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()

                data.category = if (isProduct) 0 else 1
                data.stock_id = productData.stock_id
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.barcode = tvResultBarcode.text.toString()
                data.price = priceValue.toDouble()

                if (stockActive) {
                    data.stocks = stockProduct
                    data.show_alert = true
                    data.stock_alert = stockAlert
                    data.stock_active = stockActive
                } else {
                    data.stocks = stockProductCopy
                    data.stock_alert = stockAlertCopy
                }

                presenter.onUpdateProduct(data, this)
            }
        } else if (stockAlertStr != "" && stockProductStr == "") {
            stockAlert = etStockAlert.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()

                data.category = if (isProduct) 0 else 1
                data.stock_id = productData.stock_id
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.barcode = tvResultBarcode.text.toString()
                data.price = priceValue.toDouble()

                if (stockActive) {
                    data.stocks = stockProduct
                    data.show_alert = true
                    data.stock_alert = stockAlert
                    data.stock_active = stockActive
                } else {
                    data.stocks = stockProductCopy
                    data.stock_alert = stockAlertCopy
                }

                presenter.onUpdateProduct(data, this)
            }
        } else if (stockProductStr != "" && stockAlertStr == "") {
            stockProduct = etStockProduct.text.toString().toInt()

            showLoading()

            data.category = if (isProduct) 0 else 1
            data.stock_id = productData.stock_id
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.barcode = tvResultBarcode.text.toString()
            data.price = priceValue.toDouble()

            if (stockActive) {
                data.stocks = stockProduct
                data.stock_active = stockActive
            } else {
                data.stocks = stockProductCopy
                data.stock_active = stockActiveCopy
            }

            presenter.onUpdateProduct(data, this)
        } else {
            showLoading()

            data.category = if (isProduct) 0 else 1
            data.stock_id = productData.stock_id
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.barcode = tvResultBarcode.text.toString()
            data.price = priceValue.toDouble()

            if (stockActive) {
                data.stocks = stockProduct
                data.stock_active = stockActive
            } else {
                data.stocks = stockProductCopy
                data.stock_active = stockActiveCopy
            }

            presenter.onUpdateProduct(data, this)
        }
    }

    fun updateMasterProduct() {

        val data = UpdateProductRequestModel()
        val stockAlertStr = etStockAlert.text.toString()
        val stockProductStr = etStockProduct.text.toString()

        if (stockAlertStr != "" && stockProductStr != "") {
            stockAlert = etStockAlert.text.toString().toInt()
            stockProduct = etStockProduct.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()

                data.category = data.category
                data.stock_id = productData.stock_id
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.barcode = tvResultBarcode.text.toString()
                data.price = priceValue.toDouble()

                if (stockActive) {
                    data.stocks = stockProduct
                    data.show_alert = true
                    data.stock_alert = stockAlert
                    data.stock_active = stockActive
                } else {
                    data.stocks = stockProductCopy
                    data.stock_alert = stockAlertCopy
                }

                presenter.onUpdateProduct(data, this)
            }
        } else if (stockAlertStr != "" && stockProductStr == "") {
            stockAlert = etStockAlert.text.toString().toInt()

            if (stockAlert > stockProduct && stockActive) {
                Toast.makeText(this, getString(R.string.stok_minimum_melebihi_stok_produk),
                        Toast.LENGTH_SHORT).show()
            } else {
                showLoading()

                data.category = data.category
                data.stock_id = productData.stock_id
                data.name = etProductName.text.toString()
                data.image = fileImageBase64
                data.barcode = tvResultBarcode.text.toString()
                data.price = priceValue.toDouble()

                if (stockActive) {
                    data.stocks = stockProduct
                    data.show_alert = true
                    data.stock_alert = stockAlert
                    data.stock_active = stockActive
                } else {
                    data.stocks = stockProductCopy
                    data.stock_alert = stockAlertCopy
                }

                presenter.onUpdateProduct(data, this)
            }

        } else if (stockProductStr != "" && stockAlertStr == "") {
            stockProduct = etStockProduct.text.toString().toInt()

            showLoading()
            data.category = data.category
            data.stock_id = productData.stock_id
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.barcode = tvResultBarcode.text.toString()
            data.price = priceValue.toDouble()

            if (stockActive) {
                data.stocks = stockProduct
                data.stock_active = stockActive
            } else {
                data.stocks = stockProductCopy
                data.stock_active = stockActiveCopy
            }

            presenter.onUpdateProduct(data, this)

        } else {
            showLoading()
            data.category = data.category
            data.stock_id = productData.stock_id
            data.name = etProductName.text.toString()
            data.image = fileImageBase64
            data.barcode = tvResultBarcode.text.toString()
            data.price = priceValue.toDouble()

            if (stockActive) {
                data.stocks = stockProduct
                data.stock_active = stockActive
            } else {
                data.stocks = stockProductCopy
                data.stock_active = stockActiveCopy
            }

            presenter.onUpdateProduct(data, this)
        }
    }

    fun showEditProductDialog(message: String, isAdd: Boolean) {
        val dialog = SuccessEditDialog(this, R.style.dialog2, message, isAdd, containerConstraint, this)
        dialog.show()
    }


    override fun onBackPressed() {
        if (!isShouldUpdate) {
            super.onBackPressed()
        } else {
            setResult(Activity.RESULT_OK)
            finish()
            super.onBackPressed()
        }
    }

    override fun onResume() {
        setEditTextWatcher()
        super.onResume()
    }

    override fun onStart() {
        setEditTextWatcher()
        super.onStart()
    }

    fun switchProductType(isProduct: Boolean) {
        this.isProduct = isProduct
        if (isProduct) {
            tvProduct.background = resources.getDrawable(R.drawable.rounded_fill_blue, null)
            tvProduct.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvService.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
            tvService.setTextColor(ContextCompat.getColor(this, R.color.warm_grey))

            partial_scan_barcode.visibility = VISIBLE
            this@EditProductActivity.isCategoryProductService = true
        } else {
            tvService.background = resources.getDrawable(R.drawable.rounded_fill_blue, null)
            tvService.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvProduct.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
            tvProduct.setTextColor(ContextCompat.getColor(this, R.color.warm_grey))

            //partial_scan_barcode.visibility = GONE
            this@EditProductActivity.isCategoryProductService = false
        }
    }

    /**
     * Start Clear Barcode
     */
    private fun onClearBarcode() {
        imgClearBarcode.setOnClickListener {
            tvResultBarcode.setText("")
        }
    }

    /**
     * End Clear Barcode
     */


    /**
     * Start Add Suggestion Master Product
     */
    fun setEditTextWatcher() {
        textWatcher = object : TextUtil() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val data = MasterProductRequestModel()

                if (etProductName.text.toString() != "") {
                    if (isCategoryProductService) {
                        data.name = etProductName.text.toString()
                        data.category = ""
                        presenterMasterProduct.onMasterProduct(data, this@EditProductActivity)
                        rvMasterProduct.visibility = VISIBLE

                    } else if (!isCategoryProductService) {
                        data.name = etProductName.text.toString()
                        data.category = ""
                        presenterMasterProduct.onMasterProduct(data, this@EditProductActivity)
                        rvMasterProduct.visibility = VISIBLE
                    }
                } else if (etProductName.text.toString() == "") {
                    rvMasterProduct.visibility = GONE
                    if (isCategoryProductService) {
                        masterProductId = ""
                        etProductName.text
                        tvResultBarcode.setText("")

                        fileImageBase64 = ""
                        ivHolderPhoto.visibility = VISIBLE
                        ivHolderText.visibility = VISIBLE
                        ivProduct.visibility = GONE

                        enableForAddProductFood()
                    } else if (!isCategoryProductService) {
                        masterProductId = ""
                        etProductName.text
                        tvResultBarcode.setText("")

                        fileImageBase64 = ""
                        ivHolderPhoto.visibility = VISIBLE
                        ivHolderText.visibility = VISIBLE
                        ivProduct.visibility = GONE

                        enableForAddProductJasa()
                    }
                }


            }
        }
        etProductName.addTextChangedListener(textWatcher)
    }

    override fun onSuccessMasterProduct(data: MasterProductResponseModel) {
        val data: MasterProductResponseModel = data

        if (data.meta.code == 200) {
            rvMasterProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            rvMasterProduct.setHasFixedSize(true)
            adapter = MasterProductAdapter(this, data.data, this)
            rvMasterProduct.adapter = adapter
        }
    }

    override fun onSelectedProduct(data: MasterProductResponseModel.DataBean) {
        masterProductId = data.id.toString()
        etProductName.setText(data.name)
        tvResultBarcode.setText(data.barcode)
        if (data.image != "") {
            ivHolderPhoto.visibility = GONE
            ivHolderText.visibility = GONE
            ivProduct.visibility = VISIBLE

            Glide.with(this)
                    .load(data.image)
                    .centerCrop()
                    .into(ivProduct)
        } else {
            ivHolderPhoto.visibility = VISIBLE
            ivHolderText.visibility = VISIBLE
            ivProduct.visibility = GONE
        }

        rvMasterProduct.visibility = GONE

        if (isCategoryProductService) {
            disableForAddMasterProductFood()
        } else if (!isCategoryProductService) {
            disableForAddMasterProductJasa()
        }
    }
    /**
     * End Add Suggestion Master Product
     */


    /**
     * Start Disable Add or Edit Master Product
     */
    fun disableForEditMasterProduct() {
        etProductName.isFocusableInTouchMode = false
        tvResultBarcode.isFocusableInTouchMode = false
        tvService.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)
        etProductName.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)
        tvResultBarcode.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)

        tvService.setOnClickListener {
        }

        tvProduct.setOnClickListener {
        }
    }

    fun disableForAddMasterProductFood() {
        productType = "MasterProduct"
        suggestAddMasterProduct = true
        tvResultBarcode.isFocusableInTouchMode = false
        tvService.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)
        tvResultBarcode.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)

        tvService.setOnClickListener {
        }

        tvProduct.setOnClickListener {
        }

        lyPhotoProduct.setOnClickListener {
            Toast.makeText(this, getString(R.string.tidak_bisa_mengganti_foto_master_produk), Toast.LENGTH_SHORT).show()
        }
    }

    fun disableForAddMasterProductJasa() {
        productType = "MasterProduct"
        suggestAddMasterProduct = true
        tvProduct.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)
        tvResultBarcode.background = resources.getDrawable(R.drawable.rounded_grey_color_one, null)

        tvService.setOnClickListener {
        }

        tvProduct.setOnClickListener {
        }
    }


    fun enableForAddProductFood() {
        isProduct = true
        productType = "Product"
        suggestAddMasterProduct = false
        tvService.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
        etProductName.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
        tvResultBarcode.background = resources.getDrawable(R.drawable.rounded_border_grey, null)

        tvService.setOnClickListener {
            switchProductType(false)
        }

        tvProduct.setOnClickListener {
            switchProductType(true)
        }
        onScanBarcode()
        onClearBarcode()
    }

    fun enableForAddProductJasa() {
        tvProduct.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
        etProductName.background = resources.getDrawable(R.drawable.rounded_border_grey, null)
        tvResultBarcode.background = resources.getDrawable(R.drawable.rounded_border_grey, null)

        tvService.setOnClickListener {
            switchProductType(false)
        }

        tvProduct.setOnClickListener {
            switchProductType(true)
        }
    }
    /**
     * End Disable Add or Edit Master Product
     */


    /**
     * Start Scan Barcode
     */
    fun onScanBarcode() {

        iv_scan.setOnClickListener {
            if (isAddProduct == true && productType == "Product" && !suggestAddMasterProduct) {
                val intent = Intent(this, ScanBarcodeActivity::class.java)
                startActivityForResult(intent, IConfig.SCAN_BARCODE_ADD_PRODUCT)
            } else if (isAddProduct == true && productType == "Product") {
                val intent = Intent(this, ScanBarcodeActivity::class.java)
                startActivityForResult(intent, IConfig.SCAN_BARCODE_ADD_PRODUCT)
            } else if (productType == "Product") {
                val intent = Intent(this, ScanBarcodeActivity::class.java)
                startActivityForResult(intent, IConfig.SCAN_BARCODE_EDIT_PRODUCT)
            }
        }
    }


    private fun resultScanBarcode(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) return
        val dataExtras = data!!.extras

        if (dataExtras != null) {
            resultScanBarcode = dataExtras.getString(IConfig.KEY_BARCODE_RESULT)
            tvResultBarcode.setText(resultScanBarcode)
        }
    }

    /**
     * End Scan Barcode
     */


    /**
     *Start pick photo product
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IConfig.SCAN_BARCODE_ADD_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK) {
                    resultScanBarcode(resultCode, data)
                }
            }

            IConfig.SCAN_BARCODE_EDIT_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK) {
                    resultScanBarcode(resultCode, data)
                }
            }


            IConfig.REQUEST_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        Base64ImageUtil.getPath(this@EditProductActivity, it)?.let { filePath ->
                            val file = File(filePath)

                            if (!file.exists()) {
                                MessageUserUtil.shortMessage(this@EditProductActivity, getString(R.string.gagal_mendapatkan_gambar))
                            } else {
                                //file product
                                fileImageBase64 = Base64ImageUtil.fileImageToBase64(file)
                                ivHolderPhoto.visibility = GONE
                                ivHolderText.visibility = GONE
                                ivProduct.visibility = VISIBLE

                                Glide.with(this)
                                        .load(fileImageBase64)
                                        .centerCrop()
                                        .into(ivProduct)
                            }
                        }
                    }
                } else MessageUserUtil.shortMessage(this@EditProductActivity, getString(R.string.gagal_mendapatkan_gambar))
            }

            IConfig.REQUEST_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    when {
                        data == null || data.extras == null || data.extras!!.get("data") == null -> {
                            MessageUserUtil.shortMessage(this@EditProductActivity, getString(R.string.gagal_mendapatkan_gambar))
                        }

                        else -> {
                            val bitmapImage = data.extras!!.get("data") as Bitmap

                            val folderOttoKasir = File(getExternalFilesDir(null), IConfig.FOLDER_APP)
                            if (!folderOttoKasir.exists())
                                folderOttoKasir.mkdir()

                            val filename = "${IConfig.FOLDER_APP}/camera.jpeg"
                            val dest = File(getExternalFilesDir(null), filename)

                            try {
                                val out = FileOutputStream(dest)
                                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
                                out.flush()
                                out.close()
                            } catch (e: Exception) {
                                LogHelper(TAG, e.message).run()
                                MessageUserUtil.shortMessage(this@EditProductActivity, e.message
                                        ?: "")
                            }

                            val file = File(getExternalFilesDir(null), filename)
                            if (!file.exists()) {
                                MessageUserUtil.shortMessage(this@EditProductActivity, "Failed to Get the image!\n${file.absolutePath}")
                            } else {
                                // file product
                                fileImageBase64 = Base64ImageUtil.fileImageToBase64(file)

                                ivHolderPhoto.visibility = GONE
                                ivHolderText.visibility = GONE
                                ivProduct.visibility = VISIBLE

                                Glide.with(this)
                                        .load(fileImageBase64)
                                        .centerCrop()
                                        .into(ivProduct)
                            }
                        }
                    }
                } else MessageUserUtil.shortMessage(this@EditProductActivity, getString(R.string.gagal_mendapatkan_gambar))
            }
        }
    }


    override fun onSuccessCreateProduct(data: CreateProductResponse) {
        hideLoading()

        showEditProductDialog(resources.getString(R.string.produk_berhasil_ditambahkan),
                intent.getBooleanExtra(IS_ADD_PRODUCT, false))
    }

    override fun onSuccessUpdateProduct(data: UpdateProductResponse) {
        hideLoading()
        showEditProductDialog(resources.getString(R.string.produk_berhasil_diubah), false)
    }

    override fun onSuccessDeleteProduct(data: DeleteProductResponse) {
        hideLoading()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onUpdateStatusNameAndPrice(isNameNotEmpty: Boolean, isPriceNotEmpty: Boolean) {
        btnSave.isEnabled = isNameNotEmpty && isPriceNotEmpty
        if (btnSave.isEnabled) {
            btnSave.background = resources.getDrawable(R.drawable.rounded_fill_blue, null)
        } else {
            btnSave.background = resources.getDrawable(R.drawable.rounded_rectangle_grey, null)
        }
    }

    private fun validationNamePriceStock() {

        if (etProductName.text.toString().isNotEmpty() && etPrice.text.toString().isNotEmpty() && etPrice.text.toString() != "-1" && etStockProduct.text.toString().isNotEmpty() && etStockProductDummy.text.toString().isNotEmpty()) {
            btnSave.isEnabled = true
            btnSave.background = resources.getDrawable(R.drawable.rounded_fill_blue, null)
        } else {
            btnSave.isEnabled = false
            btnSave.background = resources.getDrawable(R.drawable.rounded_rectangle_grey, null)
        }

    }

    private fun validationNamePrice() {

        if (etProductName.text.toString().isNotEmpty() && etPrice.text.toString().isNotEmpty() && etPrice.text.toString() != "-1") {
            btnSave.isEnabled = true
            btnSave.background = resources.getDrawable(R.drawable.rounded_fill_blue, null)
        } else {
            btnSave.isEnabled = false
            btnSave.background = resources.getDrawable(R.drawable.rounded_rectangle_grey, null)
        }

    }

    private fun addTextWatcher(input: EditText) {
        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                if (etStockProductDummy.text.toString().isNotEmpty()) {
                    validationNamePriceStock()
                } else {
                    validationNamePrice()
                }


            }
        })
    }

    override fun onDelete() {
        showLoading()
        presenter.onDeleteProduct(DeleteProductRequestModel(productData.stock_id), this)
    }

    override fun onAddMore() {
        productType = ""
        isShouldUpdate = true

        etPrice.setText("")
        etProductName.setText("")
        tvResultBarcode.setText("")
        etStockProduct.setText("")
        etStockAlert.setText("")

        stockProduct = 0
        stockAlert = 0

        ivHolderPhoto.visibility = VISIBLE
        ivHolderText.visibility = VISIBLE
        ivProduct.visibility = GONE
    }

    override fun onSuccessUpdate() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBackToTransaction() {
        setResult(Activity.RESULT_OK)
        val intent = Intent(this, ProductListActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDuplicateProduct(message: String) {
        hideLoading()
        MessageUserUtil.toastMessage(this, message)
    }

    override fun handleError(message: String) {
        hideLoading()
    }


    private fun addTextWatcherPrice(input: EditText) {
        mTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (etPrice.text.toString() != ""){
                    val amount = MoneyUtil.InputDecimal(charSequence.toString())

                    etPrice.removeTextChangedListener(mTextWatcher)

                    if (amount.equals("")) {
                        etPrice.setText("")
                    } else {
                        etPrice.setText(amount)
                    }

                    etPrice.addTextChangedListener(mTextWatcher)
                    priceValue = MoneyUtil.CurrencyToDouble(amount).toString()
                }
            }

            override fun afterTextChanged(editable: Editable) {
                etPrice.setSelection(etPrice.text.length)

            }
        }
        input.addTextChangedListener(mTextWatcher)
    }
}