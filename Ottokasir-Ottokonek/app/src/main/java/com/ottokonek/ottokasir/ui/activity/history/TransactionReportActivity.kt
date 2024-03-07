package com.ottokonek.ottokasir.ui.activity.history

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.beelabs.com.codebase.base.BasePresenter
import com.ottokonek.ottokasir.App.Companion.context
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.request.ExportTransactionRequestModel
import com.ottokonek.ottokasir.model.api.request.HistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.TransactionExportResponseModel
import com.ottokonek.ottokasir.model.api.response.TransactionReportResponse
import com.ottokonek.ottokasir.presenter.ReportPresenter
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import com.ottokonek.ottokasir.ui.adapter.TransactionReportAdapter
import com.ottokonek.ottokasir.ui.adapter.TransactionTypeAdapter
import com.ottokonek.ottokasir.ui.dialog.InfoDialog
import com.ottokonek.ottokasir.ui.dialog.report.ExportReportToCallback
import com.ottokonek.ottokasir.ui.dialog.report.ExportReportToDialog
import com.ottokonek.ottokasir.ui.dialog.snack_bar.TopSnackbar
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_transaction_report.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File


class TransactionReportActivity : BaseLocalActivity(), ReportPresenter.ITransactionReportIView, ExportReportToCallback {

    private var authority: String? = null
    private var urlExportReport = ""
    private var date_start: String = ""
    private var date_end: String = ""

    val presenter: ReportPresenter = BasePresenter.getInstance(this, ReportPresenter::class.java) as ReportPresenter
    val adapter = TransactionReportAdapter(this)
    val adapter2 = TransactionTypeAdapter(this)
    lateinit var infoDialog: InfoDialog

    private var downloadId: Long = 0
    private var contentUri: Uri? = null
    private var saveToDevice = false
    private var shareToEmail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_report)
        infoDialog = InfoDialog(this, R.style.CustomDialog, resources.getString(R.string.tidak_terdapat_transaksi_pada_periode_tersebut), false)

        //catching download complete events from android download manager which broadcast message
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        authority = BuildConfig.APPLICATION_ID
        Log.e("authorities", authority!!)


        initView()

        actionTransactionReport()
    }


    private fun actionTransactionReport() {
        btnExportReport.setOnClickListener {
            callApiTransactionExport()
        }
    }

    override fun onSuccessTransactionReport(data: TransactionReportResponse) {
        hideLoading()
        if (data.data.jenisProduk != null) {
            adapter.setData(data.data.jenisProduk)
        }
        if (data.data.jenisTransaksi != null) {
            if (data.data.jenisTransaksi.size != 0 && data.data.jenisProduk.size != 0) {
                adapter2.setData(data.data.jenisTransaksi)
            } else {
                infoDialog.show()
            }
        } else {
            infoDialog.show()
        }
        tvAvailableOmzet.text = MoneyUtil.convertCurrencyPHP1(data.data.totalAmount)
    }

    override fun onSuccessTransactionExport(data: TransactionExportResponseModel) {
        hideLoading()

        urlExportReport = data.data?.path.toString()
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
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Downloading Report")

        //registerReceiver(onDownloadCompleted, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));




       // if(isNetworkConnected()) {
//            Handler().postDelayed(Runnable {
//
//                val cursor: Cursor? = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
//
//                if (cursor != null && cursor.moveToNext()) {
//                    val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
//                    cursor.close()
//                    if (status == DownloadManager.STATUS_FAILED) {
//                        // do something when failed
//                        TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Fail Downloaded")
//
//                    } else if (status == DownloadManager.STATUS_PENDING || status == DownloadManager.STATUS_PAUSED) {
//                        // do something pending or paused
//                        //TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Pending")
//                    } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
//                        // do something when successful
//                        TopSnackbar.showSnackBarBlue(this, findViewById(R.id.snackbar_container), "Report Downloaded")
//                    } else if (status == DownloadManager.STATUS_RUNNING) {
//                        // do something when running
//                    }
//                }
//
//            }, 1500)
      //  }
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
        val downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(request)
        contentUri = FileProvider.getUriForFile(this, "$authority", file)
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo?.isConnected!!
    }

    private val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == id) {

                if (contentUri != null && shareToEmail) {
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "*/*"
                    share.putExtra(Intent.EXTRA_EMAIL, "")
                    share.putExtra(Intent.EXTRA_SUBJECT, "OttoKasir Transaction Report")
                    share.putExtra(Intent.EXTRA_STREAM, contentUri)
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    share.setPackage("com.google.android.gm")
                    startActivity(Intent.createChooser(share, "Share Report"))
                } else {
                    //TopSnackbar.showSnackBarBlue(this@TransactionReportActivity, findViewById(R.id.snackbar_container), "Downloading Successful")
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


    override fun onPause() {
        super.onPause()
        presenter.onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        //unregisterReceiver(onDownloadCompleted)
    }



    override fun handleError(message: String) {
        hideLoading()
        if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
            Toast.makeText(this, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun initView() {
        tvTitle.setText(R.string.laporan_penjualan)
        backAction.setOnClickListener {
            onBackPressed()
        }
        repick.setOnClickListener {
            onBackPressed()
        }
        if (intent?.getStringExtra("startDate")!!.isNotEmpty() && intent?.getStringExtra("endDate")!!.isNotEmpty()) {
            date_start = intent.getStringExtra("startDate")!!
            date_end = intent.getStringExtra("endDate")!!

            callApiTransactionReport()
            tvTransactionPeriod.text = getString(R.string.periode) + intent.getStringExtra("start") + " - " + intent.getStringExtra("end")
        }
        val mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val mLayoutManager2 = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val mDividerItemDecoration = DividerItemDecoration(rvPaymenthods.getContext(),
                mLayoutManager.getOrientation())
        val mDividerItemDecoration2 = DividerItemDecoration(rvProducts.getContext(),
                mLayoutManager2.getOrientation())
        rvPaymenthods.layoutManager = mLayoutManager
        rvProducts.layoutManager = mLayoutManager2
        rvPaymenthods.addItemDecoration(mDividerItemDecoration)
        rvProducts.addItemDecoration(mDividerItemDecoration2)
        rvPaymenthods.setHasFixedSize(true)
        rvProducts.setHasFixedSize(true)
        rvPaymenthods.adapter = adapter2
        rvProducts.adapter = adapter
    }

    override fun logout() {
        super.logout()
    }

    fun callApiTransactionReport() {
        showLoading()
        val request = HistoryRequestModel()
        request.date_start = date_start
        request.date_end = date_end
        presenter.getTransactionReport(request, this)
    }

    fun callApiTransactionExport() {
        showLoading()

        val request = ExportTransactionRequestModel()
        request.date_start = date_start
        request.date_end = date_end

        presenter.onExportTransaction(request, this)
    }
}
