package com.ottokonek.ottokasir.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import app.beelabs.com.codebase.base.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.pojo.MyLastLocation
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.ui.callback.ViewBaseInterface
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import pl.tajchert.nammu.PermissionListener
import java.util.*

open class BaseLocalActivity : BaseActivity(), ViewBaseInterface {
    private val TAG = this.javaClass.simpleName
    private val REQUEST_ACCESS_FINE_LOCATION = 1080
    private val REQUEST_READ_PHONE_STATE = 1081
    private var myLastLocation: MyLastLocation = MyLastLocation()

    private var activityStarted = false

    private val permissions = arrayOf(Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)


    override fun onApiFailed(title: String, error: String) {
        hideLoading()

        if (error.contains("Failed to connect to ") || error.contains("Unable to resolve host")) {
            Toast.makeText(this, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        /*val viewGroup = (this
                .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        val snackbar = Snackbar
                .make(viewGroup, error, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.cherry_red))
        val stv = snackbar.view.findViewById<TextView>(R.id.snackbar_text)
        stv.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()*/

        //TopSnackbar.showSnackBarRed(this, findViewById(R.id.snackbar_container), error)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onConnectionFailed(msg: String) {
        hideLoading()
        Toast.makeText(this, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_SHORT).show()
        //onApiFailed("Connection Lost", msg)
    }

    override fun showLoading() {
        CustomProgressDialog.showDialog(this, "Loading")
    }

    override fun hideLoading() {
        CustomProgressDialog.closeDialog()
    }

    override fun handleError(message: String) {
        hideLoading()
        if (message.contains("Failed to connect to ") || message.contains("Unable to resolve host")) {
            Toast.makeText(this, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun logout() {
        hideLoading()
        SessionManager.clearSessionLogin(this)
        val x = Intent(this, LoginActivity::class.java)
        x.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(x)

    }

    override fun onStop() {
        hideLoading()
        super.onStop()
    }

    override fun showPinCheckerView() {

    }


    override fun showToast(msg: String) {
        hideKeyboard()
        if (msg.contains("Failed to connect to ") || msg.contains("Unable to resolve host")) {
            Toast.makeText(this, getString(R.string.tidak_ada_koneksi), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun killActivity() {
        finish()
    }

    override fun startActivity(intent: Intent) {
        if (!activityStarted) {
            activityStarted = true
            super.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        activityStarted = false
    }

    fun hideKeyboard() {
        val imm = this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun initPermission() {
        val localPerms = ArrayList<String>()
        localPerms.addAll(listOf(*permissions))
        Nammu.init(applicationContext)
        Nammu.getGrantedPermissions()

        Nammu.permissionCompare(object : PermissionListener {
            override fun permissionsChanged(s: String) {

            }

            override fun permissionsGranted(s: String) {
                localPerms.remove(s)
            }

            override fun permissionsRemoved(s: String) {

            }
        })
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (!Nammu.checkPermission(Manifest.permission.CALL_PHONE) || !Nammu.checkPermission(Manifest.permission.CAMERA)) {
                Nammu.askForPermission(this, localPerms.toTypedArray(), object : PermissionCallback {
                    override fun permissionGranted() {

                    }

                    override fun permissionRefused() {
                        this@BaseLocalActivity.finish()
                    }
                })
            }
        } else {
            if (!Nammu.hasPermission(this, permissions))
                Nammu.askForPermission(this, localPerms.toTypedArray(), object : PermissionCallback {
                    override fun permissionGranted() {

                    }

                    override fun permissionRefused() {
                        this@BaseLocalActivity.finish()
                    }
                })
        }

    }

    /*@SuppressLint("NewApi")
    open fun getImei(){
        Nammu.init(this)
        if (!Nammu.checkPermission(Manifest.permission.READ_PHONE_STATE)) {
            val permission = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_STATE)
            ActivityCompat.requestPermissions(this, permission, 0)
        }else{
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = tm.imei
            Log.d(TAG, " Location Lat : $IMEI")
        }
    }*/

    /*@RequiresApi(Build.VERSION_CODES.O)
    fun getDeviceId() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 0)
        } else {
            val manager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val imei = manager.imei
        }
    }*/

    open fun getCurrentLocation(): Location? {
        Nammu.init(this)
        if (!Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            val permission = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permission, 0)
        } else {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


            if (network_enabled) {
                var longitude = ""
                var latitude = ""

                val criteria = Criteria()
                criteria.accuracy = Criteria.ACCURACY_FINE
                criteria.powerRequirement = Criteria.POWER_HIGH
                criteria.isAltitudeRequired = false
                criteria.isSpeedRequired = true
                criteria.isCostAllowed = true
                criteria.isBearingRequired = false
                criteria.horizontalAccuracy = Criteria.ACCURACY_HIGH
                criteria.verticalAccuracy = Criteria.ACCURACY_HIGH

                var provider = locationManager.getBestProvider(criteria, true)
                return locationManager.getLastKnownLocation(provider!!)
            }
            return null
        }
        return null
    }

    /**
     * Start Get Location
     */
    open fun getMyLastLocation(): MyLastLocation? {
        return myLastLocation
    }

    open fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // request write permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) { // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        try {
            myLastLocation.latitude = bestLocation!!.latitude
            myLastLocation.longitude = bestLocation.longitude
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            myLastLocation.latitude = 0.0
            myLastLocation.longitude = 0.0
        } finally {
            if (bestLocation != null) {
                locationManager.requestLocationUpdates(bestLocation?.provider!!, 2000, 10f, locationListener)
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d(TAG, " Location Lat : " + location.latitude)
            Log.d(TAG, " Location Lng : " + location.longitude)
            myLastLocation.latitude = location.latitude
            myLastLocation.longitude = location.longitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    /**
     * Start Pick Foto Product
     * */
    fun selectImage(context: Context) {
        val options = arrayOf<CharSequence>(getString(R.string.ambil_dari_kamera), getString(R.string.ambil_dari_galeri), getString(R.string.batal))
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.pilih_gambar_produk))
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == getString(R.string.ambil_dari_kamera) -> {
                    Dexter.withActivity(this)
                            .withPermissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).withListener(object : MultiplePermissionsListener {
                                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                    if (report.areAllPermissionsGranted()) {
                                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        startActivityForResult(takePicture, IConfig.REQUEST_CAMERA)
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied) {
                                        // show alert dialog navigating to Settings
                                        showSettingsDialog(context);
                                    }
                                }

                                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                                    token.continuePermissionRequest();
                                }

                            }).check()
                }
                options[item] == getString(R.string.ambil_dari_galeri) -> {
                    Dexter.withActivity(this)
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(object : MultiplePermissionsListener {
                                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                                    if (report.areAllPermissionsGranted()) {
                                        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                        startActivityForResult(pickPhoto, IConfig.REQUEST_GALLERY)
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied) {
                                        // show alert dialog navigating to Settings
                                        showSettingsDialog(context);
                                    }
                                }

                                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                                    token.continuePermissionRequest();
                                }

                            })
                            .withErrorListener {
                                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show()
                            }
                            .check()
                }
                options[item] == getString(R.string.batal) -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }


    /**
     * Dialog Permission
     * */
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    fun showSettingsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }
}
