package com.ottokonek.ottokasir.ui.activity.auth

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import app.beelabs.com.codebase.base.BaseActivity
import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.support.util.CacheUtil
import com.otto.mart.support.util.widget.dialog.AlertDialogBottomSheet
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.IConfig.Companion.PROFILE_DATA_KEY
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_DEVICE_ID
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_IS_SYNC
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.sync.SyncManager
import com.ottokonek.ottokasir.dao.sync.SyncManager.changeFromRealm
import com.ottokonek.ottokasir.dao.sync.SyncManager.getSyncData
import com.ottokonek.ottokasir.model.api.request.CheckVersionRequestModel
import com.ottokonek.ottokasir.model.api.request.LoginRequestModel
import com.ottokonek.ottokasir.model.api.response.CheckVersionResponseModel
import com.ottokonek.ottokasir.model.api.response.LoginResponseModel
import com.ottokonek.ottokasir.presenter.AuthPresenter
import com.ottokonek.ottokasir.presenter.CheckVersionPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.prominent.ProminentActivity
import com.ottokonek.ottokasir.ui.activity.store_type.StoreTypeActivity
import com.ottokonek.ottokasir.ui.activity.terms_conditions.TermsAndConditionsActivity
import com.ottokonek.ottokasir.ui.dialog.force_update.ForceUpdateDialog
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import com.ottokonek.ottokasir.ui.dialog.snack_bar.TopSnackbar
import kotlinx.android.synthetic.main.content_login_form.*


class LoginActivity : BaseLocalActivity(), LoginViewInterface, CheckVersionPresenter.ICheckVersionView {

    private val TAG = this.javaClass.simpleName
    private var imei: String = ""
    private lateinit var pin: String
    private lateinit var phone: String
    private var dialog: CustomProgressDialog? = null

    private val presenter = BasePresenter.getInstance(this, AuthPresenter::class.java) as? AuthPresenter
    private val checkVersionPresenter = CheckVersionPresenter(this)

    private var buildNumber = ""
    private var numberSunmi = "V1s-G"
    private var numberAdvan = "O1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //initViewRegisterMerchant()

        displayProminentDialog()

        //initPermission()

        getDeviceId()

        validationDevice()

        contentLogin()
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

    private fun contentLogin() {

        forgotPin.setOnClickListener {
            val dialog = com.ottokonek.ottokasir.ui.dialog.ConfirmationEmailDialog(
                    this@LoginActivity, R.style.DialogSlideAnimCoconutFullScreen)
            dialog.show()
        }

        loginButton.setOnClickListener {
            phone = phoneEdit.text.toString()
            pin = pinEdit.text.toString()

            if (phoneEdit.text.trim().isNotEmpty() && pinEdit.text.trim().isNotEmpty()) {
                showLoading()
                loginButton.isEnabled = true
                doLogin()
            } else {
                TopSnackbar.showSnackBarRed(this, findViewById(R.id.snackbar_container),
                        getString(R.string.nomor_hp_dan_pin_tidak_boleh_kosong))
            }
        }
    }

//    private fun initViewRegisterMerchant() {
//        tvDaftarMerchant.text = HtmlUtils.getHTMLContent(getString(R.string.belum_jadi_merchant_ottopay))
//
//        tvDaftarMerchant.setOnClickListener {
//            val url = "https://ottokonek.com/"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(intent)
//        }
//    }

    override fun onResume() {
        super.onResume()
        getDeviceId()
        validationDevice()
    }

    override fun onPause() {
        super.onPause()
        presenter?.onDestroy()
    }

    override fun handleProcessing() {
        showLoading()
    }

    override fun handleError() {
        hideLoading()
        TopSnackbar.showSnackBarRed(this, findViewById(R.id.snackbar_container), "Gagal menghubungkan ke server...")
    }

    override fun handleDataLogin(model: LoginResponseModel) {
        //showLoading()

        val model: LoginResponseModel = model
        CacheUtil.putPreferenceBoolean(SESSION_IS_SYNC, false, this)

        if (model.baseMeta!!.isStatus) {
            SyncManager.putSyncData(model.data!!)
            doLoginSync()
        } else {
            TopSnackbar.showSnackBarRed(this, findViewById(R.id.snackbar_container), model.baseMeta.message)
        }

        loginButton.isEnabled = true
    }

    override fun handleDataLoginSync(model: LoginResponseModel) {
        hideLoading()

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
            Log.e("store type id login", storeTypeId.toString())
            val storeTypeName: String? = model.data!!.store_type_name
            val nmid: String? = model.data!!.nmId
            val mpan: String? = model.data!!.mpan
            val mid: String? = model.data!!.mid


            val tncShow = CacheUtil.getPreferenceBoolean(IConfig.ISNT_FIRST_TNC_SHOW, this)

            if (tncShow == false) {
                val intent = Intent(this as BaseActivity, TermsAndConditionsActivity::class.java)
                SessionManager.putSessionLogin(true, this@LoginActivity)
                SessionManager.putCredential(userId, token, pin, email, phone, name, merchantName, businessCategoryName, avatar, merchantID, address, province, storeTypeId, storeTypeName, nmid, mid, mpan, this@LoginActivity)
                SessionManager.putName(name, this@LoginActivity)

                intent.putExtra(PROFILE_DATA_KEY, model.data)
                startActivity(intent)
                finish()
            } else if (storeTypeId == 0) {
                val intent = Intent(this as BaseActivity, StoreTypeActivity::class.java)
                SessionManager.putSessionLogin(true, this@LoginActivity)
                SessionManager.putCredential(userId, token, pin, email, phone, name, merchantName, businessCategoryName, avatar, merchantID, address, province, storeTypeId, storeTypeName, nmid, mid, mpan, this@LoginActivity)
                SessionManager.putName(name, this@LoginActivity)

                intent.putExtra(PROFILE_DATA_KEY, model.data)
                startActivity(intent)
            }
            /*else if (storeTypeId != 0) {
                val intent = Intent(this as BaseActivity, ProductListActivity::class.java)
                SessionManager.putSessionLogin(true, this@LoginActivity)
                SessionManager.putCredential(userId, token, pin, email, phone, name, merchantName, businessCategoryName, avatar, merchantID, address, province, storeTypeId, storeTypeName, nmid, mid, mpan, this@LoginActivity)
                SessionManager.putName(name, this@LoginActivity)

                intent.putExtra(PROFILE_DATA_KEY, model.data)
                startActivity(intent)
                finish()
            } */
            else {
                //TopSnackbar.showSnackBarRed(this, findViewById(R.id.snackbar_container), "Sync account gagal...")
                val intent = Intent(this as BaseActivity, ProductListActivity::class.java)
                SessionManager.putSessionLogin(true, this@LoginActivity)
                SessionManager.putCredential(userId, token, pin, email, phone, name, merchantName, businessCategoryName, avatar, merchantID, address, province, storeTypeId, storeTypeName, nmid, mid, mpan, this@LoginActivity)
                SessionManager.putName(name, this@LoginActivity)

                intent.putExtra(PROFILE_DATA_KEY, model.data)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun doLogin() {
        showLoading()

        val latitude = getMyLastLocation()!!.latitude
        val longitude = getMyLastLocation()!!.longitude

        val request = LoginRequestModel()
        request.user_id = phone
        request.pin = pin
        request.latitude = latitude
        request.longitude = longitude
        //request.firebase_token = FIREBASE_TOKEN

        presenter?.doLogin(request, this)
    }

    private fun doLoginSync() {
        showLoading()

        if (getSyncData() != null) {
            val data = changeFromRealm(getSyncData()!!)
            presenter?.doLoginSync(data, this)
        }
    }


    @SuppressLint("HardwareIds", "MissingPermission")
    private fun getDeviceId() {
        var imei = ""
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                imei = try {
                    telephonyManager.imei
                } catch (e: Exception) {
                    e.printStackTrace()
                    Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
                }
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1010)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                imei = telephonyManager.deviceId
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 1010)
            }
        }
        CacheUtil.putPreferenceString(SESSION_DEVICE_ID, imei, this)
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

    var prominentActivityLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    CacheUtil.putPreferenceBoolean("KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG",true,this)
                    getLastKnownLocation()
                } else {
                    displayForceProminentDialog()
                }
            }

    private fun displayProminentDialog() {
        if (!CacheUtil.getPreferenceBoolean("KEY_PREF_HIDE_PROMINENT_PERMISSION_DIALOG",this)
        ) {
            val intent = Intent(this, ProminentActivity::class.java)
            prominentActivityLauncher.launch(intent)
        }
    }

    private fun displayForceProminentDialog() {
        val dialog = AlertDialogBottomSheet(
                this,
                supportFragmentManager
        )
        dialog.setIsShowIcon(true)
        dialog.setIcon(R.drawable.dda_logo)
        dialog.setTitle(getString(R.string.dialog_ask_prominent_title))
        dialog.setMessage(getString(R.string.dialog_ask_prominent_contentf))
        dialog.setButton(getString(R.string.dialog_ask_prominent_btnf))
        dialog.setIsCancelable(false)
        dialog.setActionButton(::displayProminentDialog)
        dialog.setActionOnDismiss(::displayProminentDialog)
        dialog.show()
    }


}