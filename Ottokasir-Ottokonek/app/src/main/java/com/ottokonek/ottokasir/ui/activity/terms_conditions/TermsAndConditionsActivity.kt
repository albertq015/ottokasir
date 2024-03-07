package com.ottokonek.ottokasir.ui.activity.terms_conditions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import app.beelabs.com.codebase.support.util.CacheUtil
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.activity.store_type.StoreTypeActivity
import com.ottokonek.ottokasir.utils.screnshoot.HtmlUtils
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*
import kotlinx.android.synthetic.main.toolbar.*

class TermsAndConditionsActivity : BaseLocalActivity(), OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private val TAG: String = TncDetailActivity::class.java.simpleName

    private val TNC_FILE = "tnc_en.pdf"
    private var pageNumber = 0

    private var isFormValidationSuccess = false
    private var token: String? = null
    private var storeTypeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        initViewActionTac()
        displayFromAsset()
        token = SessionManager.getCredential(this)
        storeTypeId = SessionManager.getStoreTypeId(this)
        Log.e("store type id terms", storeTypeId.toString())
    }

    private fun initViewActionTac() {
        tvTitle.setText(R.string.syarat_dan_ketentuan)
        tvTac.text = HtmlUtils.getHTMLContent(getString(R.string.tnc_pdf))

        backAction.setOnClickListener {
            onBackPressed()
        }

        cbTac.setOnClickListener {
            if (cbTac.isChecked) {
                isFormValidationSuccess = true
                btnTac.background = ContextCompat.getDrawable(this, R.drawable.rounded_fill_blue)
            } else {
                isFormValidationSuccess = false
                btnTac.background = ContextCompat.getDrawable(this, R.drawable.rounded_rectangle_grey)
            }
        }

        /*tvTac.setOnClickListener {
            val intent = Intent(this, TncDetailActivity::class.java)
            startActivity(intent)
        }*/

        btnTac.setOnClickListener {
            if (isFormValidationSuccess && storeTypeId != 0) {
                CacheUtil.putPreferenceBoolean(IConfig.ISNT_FIRST_TNC_SHOW, true, this)
                val intent = Intent(this, ProductListActivity::class.java)
                startActivity(intent)
                finish()
            } else if (isFormValidationSuccess && storeTypeId == 0) {
                CacheUtil.putPreferenceBoolean(IConfig.ISNT_FIRST_TNC_SHOW, true, this)
                val intent = Intent(this, StoreTypeActivity::class.java)
                startActivity(intent)
                finish()
            } else if (token == "") {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun displayFromAsset() {
        pdfView.fromAsset(TNC_FILE)
                .defaultPage(pageNumber)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(DefaultScrollHandle(this))
                .spacing(10) // in dp
                .onPageError(this)
                .load()
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page;
        title = String.format("%s %s / %s", TNC_FILE, page + 1, pageCount);
    }

    override fun loadComplete(nbPages: Int) {

    }

    override fun onPageError(page: Int, t: Throwable?) {
        Log.e(TAG, "Cannot load page $page")
    }


}
