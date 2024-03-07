package com.ottokonek.ottokasir.ui.dialog.refund

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_confirmation_refund.*

class RefundConfirmationDialog(@NonNull context: Context, style: Int, private val callbackConfirmationRefund: RefundConfirmationCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirmation_refund)
        setCancelable(false)

        btnYes.setOnClickListener {
            this.dismiss()
            callbackConfirmationRefund.refundYes()
        }

        btnCancel.setOnClickListener {
            this.dismiss()
        }

    }

    override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }
}