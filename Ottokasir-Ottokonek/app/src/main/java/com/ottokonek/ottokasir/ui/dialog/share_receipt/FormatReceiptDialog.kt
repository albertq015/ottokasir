package com.ottokonek.ottokasir.ui.dialog.share_receipt

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_format_receipt.*

class FormatReceiptDialog(@NonNull context: Context, style: Int, private val callbackFormat: FormatReceiptCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_format_receipt)
        setCancelable(false)

        actionFormatReceipt()
    }

    private fun actionFormatReceipt() {
        iv_close.setOnClickListener {
            this.dismiss()
        }

        ly_image.setOnClickListener {
            this.dismiss()
            callbackFormat.formatShareToImage()
        }

        ly_pdf.setOnClickListener {
            this.dismiss()
            callbackFormat.formatShareToPdf()
        }
    }


    override fun onBackPressed() {
        dismiss()
    }
}