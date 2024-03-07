package com.ottokonek.ottokasir.ui.dialog.store_type

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_confirmation_refund.*

class ResetStoreTypeConfirmationDialog(@NonNull context: Context, style: Int, private val callbackResetStoreType: ResetStoreTypeCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirmation_reset_store_type)
        setCancelable(false)

        btnYes.setOnClickListener {
            this.dismiss()
            callbackResetStoreType.resetStoreTypeYes()
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