package com.ottokonek.ottokasir.ui.dialog.refund

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_processing_refund.*

class RefundProcessingDialog(@NonNull context: Context, style: Int, private val callbackProcessingRefund: RefundProcessingCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_processing_refund)
        setCancelable(false)

        btnOk.setOnClickListener {
            this.dismiss()
            refundOk()
        }
    }

    private fun refundOk() {
        this.dismiss()
        callbackProcessingRefund.refundOk()
    }

    override fun onBackPressed() {
        refundOk()
        super.onBackPressed()
    }
}