package com.ottokonek.ottokasir.ui.activity.kasbon

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.ottokonek.ottokasir.App
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.KasbonExportRequestModel
import com.ottokonek.ottokasir.model.api.request.KasbonReportRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonExportResponseModel
import com.ottokonek.ottokasir.model.api.response.KasbonReportResponseModel
import com.ottokonek.ottokasir.presenter.KasbonPresenter
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.adapter.KasbonReportAdapter
import com.ottokonek.ottokasir.ui.callback.SelectedReportKasbonType
import com.ottokonek.ottokasir.ui.dialog.report.ExportReportToCallback
import com.ottokonek.ottokasir.ui.dialog.report.ExportReportToDialog
import com.ottokonek.ottokasir.ui.dialog.snack_bar.TopSnackbar
import com.ottokonek.ottokasir.utils.MessageUserUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_kasbon_transaction_report.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.util.ArrayList

class KasbonTransactionReportActivity : BaseLocalActivity(), KasbonPresenter.IKasbonReportView, KasbonPresenter.IKasbonExportView, ExportReportToCallback {

    private val kasbonPresenter = KasbonPresenter(this)
    private var kasbonReportItems = ArrayList<KasbonReportResponseModel.DataBean.JenisTransaksiBean>()
    private var kasbonReportAdapter: KasbonReportAdapter? = null
    private var authority: String? = null

    var urlExportReport = ""
    var startDate = ""
    var endDate = ""
    var startDateConvert = ""
    var endDateConvert = ""

    private var downloadId: Long = 0
    private var contentUri: Uri? = null
    private var saveToDevice = false
    private var shareToEmail = false

    companion object {
        const val KEY_START_DATE = "start_date"
        const val KEY_END_DATE = "end_date"
        const val KEY_START_DATE_CONVERT = "start_date_convert"
        const val KEY_END_DATE_CONVERT = "end_date_convert"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kasbon_transaction_report)

        authority = BuildConfig.APPLICATION_ID
        Log.e("authorities", authority!!)

        //catching download complete events from android download manager which broadcast message
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        getDataIntent()

        contentKasbonReport()

        actionKasbonReport()

        callApiKasbonReport()

        configureList()
    }

    private fun getDataIntent() {

        startDateConvert = intent.getStringExtra(KEY_START_DATE_CONVERT)!!
        endDateConvert = intent.getStringExtra(KEY_END_DATE_CONVERT)!!
        startDate = intent.getStringExtra(KEY_START_DATE)!!
        endDate = intent.getStringExtra(KEY_END_DATE)!!

    }


    private fun contentKasbonReport() {
        tvTitle.text = getString(R.string.laporan_kasbon)

        tvTransactionPeriod.text = "Period $startDateConvert - $endDateConvert"
    }

    private fun actionKasbonReport() {

        backAction.setOnClickListener {
            onBackPressed()
        }

        repick.setOnClickListener {
            onBackPressed()
        }

        btnExportReport.setOnClickListener {
            callApiKasbonExport()
        }
    }


    /**
     * Start Call Api Kasbon Report
     * */
    private fun callApiKasbonReport() {
        showLoading()

        val data = KasbonReportRequestModel()
        data.sort_by = ""
        data.sorting = ""
        data.keyword = ""
        data.start_date = startDate
        data.end_date = endDate
        data.status = ""

        kasbonPresenter.onKasbonReport(data, this)
    }

    override fun onSuccessKasbonReport(result: KasbonReportResponseModel) {
        hideLoading()

        kasbonReportItems.clear()
        result.data?.jenisTransaksi.let {
            it?.let { it1 -> kasbonReportItems.addAll(it1) }
        }

        tvKasbonAmount.text = result.data?.totalAmount?.toDouble()?.let {
            MoneyUtil.convertIDRCurrencyFormat(it)
        }

        refreshList()
    }

    private fun configureList() {

        if (kasbonReportAdapter == null)
            kasbonReportAdapter = KasbonReportAdapter(kasbonReportItems, object : SelectedReportKasbonType {


                override fun kasbonType(type: String?) {

                    /*val bundle = Bundle()
                    if (type == "Cashbond Lunas") {
                        bundle.putBoolean(ProductListActivity.KEY_FROM_KASBON_LUNAS, true)
                        ActivityUtil(this@KasbonTransactionReportActivity)
                                .sendData(bundle)
                                .showPage(ProductListActivity::class.java)
                    } else if (type == "Cashbond Aktif") {
                        bundle.putBoolean(ProductListActivity.KEY_FROM_REPORT_KASBON_ACTIVE, true)
                        ActivityUtil(this@KasbonTransactionReportActivity)
                                .sendData(bundle)
                                .showPage(ProductListActivity::class.java)
                    }*/

                }
            })

        rvKasbonReport?.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = kasbonReportAdapter
        }
    }

    private fun refreshList() {
        kasbonReportAdapter.let {
            if (it != null)
                it.notifyDataSetChanged()
            else
                configureList()
        }
    }


    /**
     * Start Call Api Kasbon Export
     * */
    private fun callApiKasbonExport() {
        showLoading()

        val data = KasbonExportRequestModel()
        data.sort_by = ""
        data.sorting = ""
        data.keyword = ""
        data.start_date = startDate
        data.end_date = endDate
        data.status = ""

        kasbonPresenter.onKasbonExport(data, this)
    }


    override fun onSuccessKasbonExport(result: KasbonExportResponseModel) {
        hideLoading()

        urlExportReport = result.data?.path.toString()

        if (urlExportReport != "") {
            val dialog = ExportReportToDialog(this, R.style.style_bottom_sheet, this)
            dialog.show()
        } else {
            Toast.makeText(this, "Url download not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun ExportToDevice() {
        exportReportToDevice(urlExportReport)

    }

    override fun ExportToEmail() {
        exportReportToEmail(urlExportReport)

    }

    private fun exportReportToDevice(url: String) {
        saveToDevice = true
        contentUri = null
        val filename = url.substring(url.lastIndexOf("/") + 1)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/OttoKasir/" + filename)
        Log.d("Environment", "Environment extraData=" + file.path)
        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(filename)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = App.context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Downloading Report")
//        if(isNetworkConnected()){
//        Handler().postDelayed(Runnable {
//            val cursor: Cursor? = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
//
//            if (cursor != null && cursor.moveToNext()) {
//                val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//                cursor.close()
//                if (status == DownloadManager.STATUS_FAILED) {
//                    // do something when failed
//                    TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Fail Downloaded")
//
//                } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
//                    // do something pending or paused
//                    TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Pending")
//                } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                    // do something when successful
//                    TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Downloaded")
//                } else if (status == DownloadManager.STATUS_RUNNING) {
//                    // do something when running
//                }
//            }        }, 1500)
//    }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = App.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun exportReportToEmail(url: String) {
        shareToEmail = true
        val filename = url.substring(url.lastIndexOf("/") + 1)
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/OttoKasir/" + filename)
        Log.d("Environment", "Environment extraData=" + file.path)
        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(filename)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        val downloadManager = App.context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        contentUri = FileProvider.getUriForFile(this, "$authority", file)
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == id) {

                if (contentUri != null && shareToEmail) {
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "*/*"
                    share.putExtra(Intent.EXTRA_EMAIL, "")
                    share.putExtra(Intent.EXTRA_SUBJECT, "OttoKasir Pay Later Report")
                    share.putExtra(Intent.EXTRA_STREAM, contentUri)
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    share.setPackage("com.google.android.gm")
                    startActivity(Intent.createChooser(share, "Share Report"))
                } else {
                    //TopSnackbar.showSnackBarBlue(this@KasbonTransactionReportActivity, findViewById(R.id.snackbar_container), "Downloading Successful")
                }

            }
            val action = intent.action
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == action) {
                //Show a notification
                // Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG).show()
                TopSnackbar.showSnackBarBlue(context, findViewById(R.id.snackbar_container), "Report Downloaded")
            }
        }
    }

    override fun handleError(message: String) {
        hideLoading()
        when (message) {
            "Invalid request token!" -> {
                MessageUserUtil.shortMessage(this, message)
                SessionManager.logoutDevice(this)
            }
            else -> {
                if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
                    MessageUserUtil.shortMessage(this, getString(R.string.tidak_ada_koneksi))
                } else {
                    MessageUserUtil.shortMessage(this, message)
                }
            }
        }
    }
}