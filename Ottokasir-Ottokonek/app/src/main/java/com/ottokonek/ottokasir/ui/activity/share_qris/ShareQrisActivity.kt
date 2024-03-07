package com.ottokonek.ottokasir.ui.activity.share_qris

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.ottokonek.ottokasir.BuildConfig
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.KasbonQrPaymentResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentQrResponseModel
import com.ottokonek.ottokasir.utils.DeviceUtil
import com.ottokonek.ottokasir.utils.MoneyUtil
import com.ottokonek.ottokasir.utils.screnshoot.FileUtil
import com.ottokonek.ottokasir.utils.screnshoot.ScreenshotUtil
import kotlinx.android.synthetic.main.activity_share_qris.*
import java.io.File
import java.util.*


class ShareQrisActivity : AppCompatActivity() {

    private val calendar = Calendar.getInstance()

    private var authority: String? = null
    private var fileQris: Bitmap? = null

    private var pelunasanKasbon = false
    private var dataQrisPembelian = PaymentQrResponseModel.DataBean()
    private var dataQrisPelunasanKasbon = KasbonQrPaymentResponseModel.DataBean()

    companion object {
        const val KEY_FROM_PELUNASAN_KASBON = "pelunasan_kasbon"
        const val KEY_DATA_PEMBELIAN_QRIS = "key_data_pembelian_qris"
        const val KEY_DATA_PELUNASAN_QRIS = "key_data_pelunasan_qris"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_qris)

        authority = BuildConfig.APPLICATION_ID


        getDataIntent()

        contentShareQris()

        val handler = Handler()
        handler.postDelayed({
            shareQris()
        }, 1000)
    }

    private fun getDataIntent() {
        intent?.extras?.let {

            if (it.containsKey(KEY_FROM_PELUNASAN_KASBON)) {
                pelunasanKasbon = it.getBoolean(KEY_FROM_PELUNASAN_KASBON, false)
            }

            if (it.containsKey(KEY_DATA_PEMBELIAN_QRIS)) {
                dataQrisPembelian = it.getSerializable(KEY_DATA_PEMBELIAN_QRIS) as PaymentQrResponseModel.DataBean
            }

            if (it.containsKey(KEY_DATA_PELUNASAN_QRIS)) {
                dataQrisPelunasanKasbon = it.getSerializable(KEY_DATA_PELUNASAN_QRIS) as KasbonQrPaymentResponseModel.DataBean
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun contentShareQris() {

        if (pelunasanKasbon) {
            tvMerchantName.text = dataQrisPelunasanKasbon.merchant_name
            tvNmid.text = "NMID : " + dataQrisPelunasanKasbon.nmid
            tvAmount.text = dataQrisPelunasanKasbon.total_amount?.toDouble()?.let { MoneyUtil.convertCurrencyPHP1(it) }
            ivQris.setImageBitmap(createQR(dataQrisPelunasanKasbon.qr_string))
            //tvPrintVersion.text = getString(R.string.versi_cetak) + DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy.MM.dd")

        } else {
            tvMerchantName.text = dataQrisPembelian.merchant_name
            tvNmid.text = "NMID : " + dataQrisPembelian.nmid
            tvAmount.text = dataQrisPembelian.total_amount?.toDouble()?.let { MoneyUtil.convertCurrencyPHP1(it) }
            ivQris.setImageBitmap(createQR(dataQrisPembelian.qr_string))
            //tvPrintVersion.text = getString(R.string.versi_cetak) + DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy.MM.dd")
        }
    }

    private fun createQR(str: String?): Bitmap {

        val widthScreen = resources.displayMetrics.widthPixels
        val margin = DeviceUtil.dpToPx(64)
        val actualSize = widthScreen.minus(margin).minus(margin)

        val result: BitMatrix = MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, actualSize, actualSize, null)

        val w = result.width
        val h = result.height

        val pixels = IntArray(w.times(h))

        for (y in 0 until h) {
            val offset = y.times(h)
            for (x in 0 until w) {
                pixels[offset.plus(x)] = if (result.get(x, y)) Color.BLACK else Color.WHITE
            }
        }

//        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val bitmap = Bitmap.createBitmap(pixels, 0, actualSize, w, h, Bitmap.Config.ARGB_8888)


        return bitmap
    }


    fun shareQris() {
        fileQris = ScreenshotUtil.instance?.takeScreenshotForView(viewShareQris)

        if (fileQris != null) {
            val fileName: String = IConfig.FILE_NAME_FOTO_QRIS + "OttoKasir" + IConfig.EXTENSION_FILE_FOTO

            val folderFoto = File(Environment.getExternalStorageDirectory(), IConfig.FOLDER_FOTO)
            if (!folderFoto.exists()) folderFoto.mkdirs()

            val savedPhoto = File(Environment.getExternalStorageDirectory(), fileName)
            FileUtil.instance?.storeBitmap(fileQris!!, savedPhoto.path)

            val contentUri = FileProvider.getUriForFile(this, "$authority", savedPhoto)


            val share = Intent(Intent.ACTION_SEND)
            val chooser = Intent.createChooser(share, "Share QRIS")
            share.type = "*/*"
            share.putExtra(Intent.EXTRA_EMAIL, "")
            share.putExtra(Intent.EXTRA_SUBJECT, "QRIS")
            share.putExtra(Intent.EXTRA_TEXT, "Easy Cashless Payment with Smartphone. Scan This QR with your e-money App. For More information visit https://ottokonek.com")
            share.putExtra(Intent.EXTRA_STREAM, contentUri)
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(chooser)
        }
    }


}