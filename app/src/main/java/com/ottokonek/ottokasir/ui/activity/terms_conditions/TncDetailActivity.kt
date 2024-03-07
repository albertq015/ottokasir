package com.ottokonek.ottokasir.ui.activity.terms_conditions

import android.os.Bundle
import android.util.Log
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import kotlinx.android.synthetic.main.activity_tnc_detail.*
import kotlinx.android.synthetic.main.toolbar.*

class TncDetailActivity : BaseLocalActivity(), OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener {

    private val TAG: String = TncDetailActivity::class.java.getSimpleName()

    private val TNC_FILE = "tnc_en.pdf"
    private var pageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tnc_detail)

        initContent()
        displayFromAsset()


    }

    fun initContent() {
        tvTitle.text = "Syarat dan Ketentuan"
        backAction.setOnClickListener {
            onBackPressed()
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