package com.ottokonek.ottokasir.ui.activity.scan_barcode

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.google.zxing.Result
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.ui.activity.BaseLocalActivity
import kotlinx.android.synthetic.main.toolbar.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanBarcodeActivity : BaseLocalActivity(), ZXingScannerView.ResultHandler {

    companion object {
        private const val REQUEST_CAMERA = 1
    }

    private var scannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_barcode)
        tvTitle.text = getString(R.string.scan_barcode)

        scannerView = findViewById(R.id.scanBarcode)

        checkPermission()
        if (!checkPermission()) {
            requestPermission()
        }

        backAction.setOnClickListener {
            onBackPressed()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this@ScanBarcodeActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> if (grantResults.isNotEmpty()) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted) {
                    //Toast.makeText(this@ScanBarcodeActivity, "Permission Granted, Now you can access camera", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ScanBarcodeActivity, "Permission Denied, You cannot access and camera", Toast.LENGTH_SHORT).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                            displayAlertMessage("You need to allow access for both permissions", DialogInterface.OnClickListener { _, _ ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
                                }
                            })
                            return
                        }
                    }
                }
            }
        }
    }

    public override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = ZXingScannerView(this)
                    setContentView(scannerView)
                }

                scannerView!!.setResultHandler(this)
                scannerView!!.startCamera()
            } else {
                requestPermission()
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        scannerView!!.stopCamera()
    }

    fun displayAlertMessage(message: String?, listener: DialogInterface.OnClickListener?) {
        AlertDialog.Builder(this@ScanBarcodeActivity)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }

    override fun handleResult(result: Result?) {
        val scanResult = result?.text
        //Toast.makeText(this, scanResult, Toast.LENGTH_SHORT).show()

        val intent = Intent()
        intent.putExtra(IConfig.KEY_BARCODE_RESULT, scanResult)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
