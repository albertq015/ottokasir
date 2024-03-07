package com.ottokonek.ottokasir.ui.activity.product_list

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.beelabs.com.codebase.base.contract.IView
import app.beelabs.com.codebase.support.util.CacheUtil
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaeger.library.StatusBarUtil
import com.ottokonek.ottokasir.ui.fragment.OmzetFragment
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.cart.CartManager
import com.ottokonek.ottokasir.dao.sync.SyncManager.changeFromRealm
import com.ottokonek.ottokasir.dao.sync.SyncManager.getSyncData
import com.ottokonek.ottokasir.model.api.request.CheckVersionRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckVersionResponseModel
import com.ottokonek.ottokasir.model.miscModel.DrawerUiModel
import com.ottokonek.ottokasir.presenter.CheckVersionPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.presenter.productList.ProductListPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.payment.ChooseCustomerActivity
import com.ottokonek.ottokasir.ui.activity.product_list.adapter.DrawerAdapter
import com.ottokonek.ottokasir.ui.activity.scan_barcode.ScanBarcodeActivity
import com.ottokonek.ottokasir.ui.dialog.ExitAppDialog
import com.ottokonek.ottokasir.ui.dialog.force_update.ForceUpdateDialog
import com.ottokonek.ottokasir.ui.fragment.*
import com.ottokonek.ottokasir.ui.fragment.customer.CustomerFragment
import com.ottokonek.ottokasir.ui.fragment.kasbon.onboarding.KasbonOnboardingOne
import com.ottokonek.ottokasir.ui.fragment.manage_product.ManageProductFragment
import com.ottokonek.ottokasir.ui.fragment.manage_product.ManageProductIView
import com.ottokonek.ottokasir.ui.fragment.manage_product.onboarding.ManageProductOnboardingOne
import com.ottokonek.ottokasir.ui.fragment.profile.ProfileFragment
import com.ottokonek.ottokasir.utils.DeviceUtil
import com.ottokonek.ottokasir.utils.ResourceUtil
import com.ottokonek.ottokasir.utils.TextUtil
import kotlinx.android.synthetic.main.activity_productlist.*
import kotlinx.android.synthetic.main.drawer_main.*
import kotlinx.android.synthetic.main.drawer_main_header.*
import java.util.*


class ProductListActivity : BaseLocalActivity(), IView, CheckVersionPresenter.ICheckVersionView {

    private val checkVersionPresenter = CheckVersionPresenter(this)
    private val presenterSync = ProductListPresenter(this)
    private var pauseOnresumeIterationOnce: Boolean = false
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var sheetView: View
    private lateinit var textWatcher: TextWatcher
    private var resultScanBarcode: String? = null
    private var isFromStoreType = false
    //private var isFromReportLunas = false
    //private var isFromReportAktif = false

    private var isFromOnboardingKasbon = false
    private var isFromOnboardingManageProduct = false
    private var isNotFirstTimeOnboardingKasbon = false
    private var isNotFirstTimeOnboardingManageProduct = false

    private var buildNumber = ""
    private var numberSunmi = "V1s-G"
    private var numberAdvan = "O1"

    companion object {
        //const val KEY_FROM_REPORT_KASBON_ACTIVE = "key_kasbon_active"
        //const val KEY_FROM_KASBON_LUNAS = "key_kasbon_lunas"
        const val KEY_FROM_ONBOARDING_KASBON = "key_onboarding_kasbon"
        const val KEY_FROM_ONBOARDING_MANAGE_PRODUCT = "key_onboarding_manage_product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productlist)
        ButterKnife.bind(this)

        StatusBarUtil.setColorForDrawerLayout(this, drawer_layout, ContextCompat.getColor(this, R.color.colorPrimaryDark))
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorPrimaryDark))
        pauseOnresumeIterationOnce = true

        initPermission()
        initContent()
        validationDevice()

        isNotFirstTimeOnboardingKasbon = SessionManager.getIsNotFirstTimeOnboardingKasbon(this)
        isNotFirstTimeOnboardingManageProduct = SessionManager.getIsNotFirstTimeOnboardingManageProduct(this)
        val extras = intent.extras
        if (extras != null) {
            isFromStoreType = extras.getBoolean(IConfig.IS_FROM_STORE_TYPE, false)
            //isFromReportAktif = extras.getBoolean(KEY_FROM_REPORT_KASBON_ACTIVE, false)
            //isFromReportLunas = extras.getBoolean(KEY_FROM_KASBON_LUNAS, false)
            isFromOnboardingKasbon = extras.getBoolean(KEY_FROM_ONBOARDING_KASBON, false)
            isFromOnboardingManageProduct = extras.getBoolean(KEY_FROM_ONBOARDING_MANAGE_PRODUCT, false)


            if (isFromStoreType) {
                syncLogin()
                showFragment(ManageProductFragment(), R.id.container, "manageProduct")
            }

            if (isFromOnboardingKasbon) {
                showFragment(KasbonFragment(), R.id.container)
            }

            if (isFromOnboardingManageProduct) {
                showFragment(ManageProductFragment(), R.id.container, "manageProduct")
            }

            /*if (isFromReportAktif) {
                showFragment(KasbonFragment(), R.id.container, "manageProduct")
            } else if (isFromReportLunas) {

                val message: Boolean = true
                val data = Bundle()
                data.putBoolean(KasbonFragment.KEY_FROM_KASBON_LUNAS, message)
                val fragtry = KasbonFragment()
                fragtry.arguments = data
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragtry)
                        .commit()
            }*/
        }
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

    override fun onResume() {
        super.onResume()
        validationDevice()
        isNotFirstTimeOnboardingKasbon = SessionManager.getIsNotFirstTimeOnboardingKasbon(this)
    }

    private fun initContent() {
        iv_scan.visibility = View.VISIBLE
        drawer_layout!!.setScrimColor(Color.TRANSPARENT)
        drawer_layout!!.drawerElevation = 8f

        val mDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawer_layout, 0, 0) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                StatusBarUtil.setDarkMode(this@ProductListActivity)
                hideKeyboard()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                if (!DeviceUtil.isExceptionalDevice()) {
                    StatusBarUtil.setLightMode(this@ProductListActivity)
                }
            }
        }

        iv_scan.setOnClickListener {
            val intent = Intent(this, ScanBarcodeActivity::class.java)
            startActivityForResult(intent, IConfig.SCAN_BARCODE_SEARCH_PRODUCT)
        }


        drawer_layout!!.addDrawerListener(mDrawerToggle)

        val drawerItemModels = ArrayList<DrawerUiModel>()
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.transaksi), R.drawable.ic_transaction))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.daftar_transaksi), R.drawable.ic_list_transaction))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.laporan_penjualan), R.drawable.ic_report))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.kelola_produk), R.drawable.ic_manage_product))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.pelanggan), R.drawable.ic_pelanggan))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.kasbon), R.drawable.ic_kasbon))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.omzet), R.drawable.ic_omzet))
        //drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.panduan), R.drawable.ic_panduan))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.hubungi_kami), R.drawable.ic_hubungi))
        drawerItemModels.add(DrawerUiModel(ResourceUtil.stringResource(R.string.profil), R.mipmap.ic_profil))
        val drawerAdapter = DrawerAdapter(this, drawerItemModels)
        drawerAdapter.setListener { pos ->
            val intent: Intent? = null
            when (pos) {
                0 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(ProductFragment(), R.id.container, "productList")
                }
                1 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(TransactionFragment(), R.id.container)
                }
                2 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(ReportFragment(), R.id.container)
                }
                3 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    if (isNotFirstTimeOnboardingManageProduct) {
                        showFragment(ManageProductFragment(), R.id.container, "manageProduct")
                    } else {
                        showFragment(ManageProductOnboardingOne(), R.id.container)
                    }
                }
                4 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(CustomerFragment(), R.id.container)
                }
                5 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    if (isNotFirstTimeOnboardingKasbon) {
                        showFragment(KasbonFragment(), R.id.container)
                    } else {
                        showFragment(KasbonOnboardingOne(), R.id.container)
                    }

                }
                6 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(OmzetFragment(), R.id.container)
                }
                /*7 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(IConfig.PANDUAN_URL))
                    startActivity(intent)
                }*/
                7 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(HubungiFragment(), R.id.container)
                }
                8 -> {
                    drawer_layout!!.closeDrawer(GravityCompat.START)
                    showFragment(ProfileFragment(), R.id.container)
                }
                else -> {
                }
            }

        }
        tv_name.text = SessionManager.getUsername(this)
        rv_navdrawer.layoutManager = LinearLayoutManager(this)
        rv_navdrawer.adapter = drawerAdapter
        tv_location.text = CacheUtil.getPreferenceString(IConfig.SESSION_PROVINCE, this)

        showFragment(ProductFragment(), R.id.container, "productList")


    }

    fun removeEditTextWatcher() {
        if (textWatcher != null) {
            et_search.removeTextChangedListener(textWatcher)
        }
    }

    fun setEditTextWatcher() {
        textWatcher = object : TextUtil() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (supportFragmentManager.findFragmentByTag("productList") != null) {
                    val iView: ProductlistViewInterface = supportFragmentManager.findFragmentByTag("productList") as ProductlistViewInterface

                    if (iView.statusAPI) {
                        iView.callProductlistFiltered(et_search.text.toString(), "")
                    }
                } else if (supportFragmentManager.findFragmentByTag("manageProduct") != null) {
                    val iView: ManageProductIView = supportFragmentManager.findFragmentByTag("manageProduct") as ManageProductIView

                    if (iView.getStatusAPI()) {
                        iView.getProductWithFilter(et_search.text.toString())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

                if (supportFragmentManager.findFragmentByTag("productList") != null) {
                    val clearProductList: ProductlistViewInterface = supportFragmentManager.findFragmentByTag("productList") as ProductlistViewInterface

                    if (et_search.text.toString() != "") {
                        clearProductList.showClearSearch()
                    } else {
                        clearProductList.hideClearSearch()
                    }
                } else if (supportFragmentManager.findFragmentByTag("manageProduct") != null) {
                    val clearManageProduct: ManageProductIView = supportFragmentManager.findFragmentByTag("manageProduct") as ManageProductIView

                    if (et_search.text.toString() != "") {
                        clearManageProduct.showClearSearch()
                    } else {
                        clearManageProduct.hideClearSearch()
                    }
                }
            }
        }
        et_search.addTextChangedListener(textWatcher)
    }

    override fun onBackPressed() {
        if (bottomSheetDialog.isShowing) {
            bottomSheetDialog.dismiss()
        } else {
            val dialog = ExitAppDialog(this, R.style.CustomDialog, "")
            dialog.show()
        }
    }

    @OnClick(R.id.iv_burger)
    fun openDrawer() {
        drawer_layout!!.openDrawer(GravityCompat.START)
    }

    fun showBottomPanel() {
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener {
            background.visibility = View.GONE
        }

        background.visibility = View.VISIBLE
        val ivClose = sheetView!!.findViewById<ImageView>(R.id.ivClose)
        val btnCash = sheetView!!.findViewById<Button>(R.id.btnCash)
        val btnQR = sheetView!!.findViewById<Button>(R.id.btnQR)
        val btnKasbon = sheetView.findViewById<Button>(R.id.btnKasbon)
        val tvTotal = sheetView!!.findViewById<TextView>(R.id.tvTotal)

        tvTotal.text = ProductFragment.price

        ivClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        btnCash.setOnClickListener {
            val intent = Intent(this@ProductListActivity, ChooseCustomerActivity::class.java)
            intent.putExtra(ChooseCustomerActivity.KEY_PAYMENT_CASH, true)
            intent.putExtra("total", ProductFragment.price)
            startActivityForResult(intent, 122)
        }

        btnQR.setOnClickListener {
            val intent = Intent(this@ProductListActivity, ChooseCustomerActivity::class.java)
            intent.putExtra(ChooseCustomerActivity.KEY_PAYMENT_QRIS, true)
            intent.putExtra("total", ProductFragment.price)
            startActivityForResult(intent, 122)
        }

        btnKasbon.setOnClickListener {
            val intent = Intent(this@ProductListActivity, ChooseCustomerActivity::class.java)
            intent.putExtra(ChooseCustomerActivity.KEY_PAYMENT_KASBON, true)
            intent.putExtra("total", ProductFragment.price)
            startActivityForResult(intent, 122)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 122 && resultCode == Activity.RESULT_OK) {
            val iView: ProductlistViewInterface = supportFragmentManager.findFragmentByTag("productList") as ProductlistViewInterface
            CartManager.removeAllCartItem()
            bottomSheetDialog.dismiss()
            iView.addFooterData("0", "0", "0")
            iView.hideFooter()
            iView.refreshView()
        } else if (requestCode == IConfig.EDIT_PRODUCT_CODE) {
            if (supportFragmentManager.findFragmentByTag("productList") != null) {
                val iView: ProductlistViewInterface = supportFragmentManager.findFragmentByTag("productList") as ProductlistViewInterface
                iView.refreshView()
            }
        } else if (requestCode == IConfig.SCAN_BARCODE_SEARCH_PRODUCT) {
            resultScanBarcode(resultCode, data)
        }
    }


    fun showFragment(fragment: Fragment?, fragmentResourceID: Int, tag: String) {
        if (fragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(fragmentResourceID, fragment, tag)
            fragmentTransaction.detach(fragment)
            fragmentTransaction.attach(fragment)
            fragmentTransaction.commit()
        }
    }

    override fun onPause() {
        super.onPause()
        bottomSheetDialog.dismiss()
    }

    override fun onStart() {
        super.onStart()
        bottomSheetDialog = BottomSheetDialog(this@ProductListActivity, R.style.dialog)
        sheetView = getLayoutInflater().inflate(R.layout.content_panel_transaction, null)
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setCancelable(false)

    }

    private fun syncLogin() {
        if (getSyncData() != null) {
            val data = changeFromRealm(getSyncData()!!)
            presenterSync.syncLogin(data, this)
        }
    }


    private fun resultScanBarcode(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED) return
        val dataExtras = data!!.extras

        if (dataExtras != null) {
            resultScanBarcode = dataExtras.getString(IConfig.KEY_BARCODE_RESULT)
            et_search.setText(resultScanBarcode)
        }
    }

    /**
     * ================= CHECK VERSION =================
     * */
    private fun callApiCheckVersion() {
        val data = CheckVersionRequestModel()
        data.version_app = BuildConfig.VERSION_CODE

        checkVersionPresenter.onCheckVersion(data, this)
    }

    override fun onSuccessCheckVersion(result: CheckVersionResponseModel) {

        val forceUpdate = result.data!!.force_update
        if (forceUpdate) {
            ForceUpdateDialog.showDialog(this, object : ForceUpdateDialog.ActionDialog {
                override fun onForceUpdate() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ottokonek.ottokasir")))
                }
            })
        }
    }


}
