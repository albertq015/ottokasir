package com.ottokonek.ottokasir.ui.activity.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.terms_conditions.TermsAndConditionsActivity

class SplashScreenActvity : AppCompatActivity() {

    private var tncShow = true
    private var storeTypeId: Int = 0
    private var token: String = ""

    private var handler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler()
        handler!!.postDelayed({ this.autoRouteToDashboard() }, 1000)

    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) handler = null
    }

    private fun autoRouteToDashboard() {
        token = SessionManager.getCredential(this)
        storeTypeId = SessionManager.getStoreTypeId(this)
        tncShow = CacheUtil.getPreferenceBoolean(IConfig.ISNT_FIRST_TNC_SHOW, this)

        Log.e("token splash", token)
        Log.e("store type id splash", storeTypeId.toString())
        Log.e("tnc show splash", tncShow.toString())

        if (token != "" && storeTypeId != 0 && tncShow) {
            val intent = Intent(this, ProductListActivity::class.java)
            startActivity(intent)
            finish()
        } else if (token != "" && !tncShow) {
            val intent = Intent(this, TermsAndConditionsActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
