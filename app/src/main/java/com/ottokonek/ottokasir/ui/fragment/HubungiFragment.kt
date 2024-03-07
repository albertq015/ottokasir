package com.ottokonek.ottokasir.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.fragment_hubungi.*

class HubungiFragment : BaseLocalFragment() {

    companion object {
        const val EMAIL_CALL_CENTER = "contactus@ottokonek.com"
        const val PHONE_NUMBER_CALL_CENTER = "+639501938743"
        const val URL_WEBSITE = "http://www.ottokonek.com/"
        /*const val PHONE_NUMBER_WA = "0918303100"
        const val URL_WA_BROADCAST = "https://wa.me/$PHONE_NUMBER_WA?text=Hai,%20saya%20sedang%20butuh%20bantuan%20nih.%20Mohon%20dibantu%20yah%20:)%20Terima%20kasih."*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hubungi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentHubungi()
        actionHubungi()

    }

    private fun contentHubungi() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)

        title.visibility = View.VISIBLE
        title.text = getString(R.string.hubungi_kami)

        search.visibility = View.GONE
        scanBarcode.visibility = View.GONE

    }


    private fun actionHubungi() {

        lyHubEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", EMAIL_CALL_CENTER, null))
            startActivity(Intent.createChooser(intent, "Choose an Email client :"))
        }

        lyHubTelepon.setOnClickListener {
            Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.CALL_PHONE)
                    .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                        @SuppressLint("MissingPermission")
                        override fun onPermissionGranted(response: PermissionGrantedResponse) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:$PHONE_NUMBER_CALL_CENTER"))
                            startActivity(intent)
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse) { // check for permanent denial of permission
                            if (response.isPermanentlyDenied) { // navigate user to app settings
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                            token.continuePermissionRequest()
                        }
                    }).check()
        }

        /*lyHubWhatsApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_WA_BROADCAST))
            startActivity(intent)
        }*/

//        lyHubWebsite.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_WEBSITE))
//            startActivity(intent)
//        }

    }
}