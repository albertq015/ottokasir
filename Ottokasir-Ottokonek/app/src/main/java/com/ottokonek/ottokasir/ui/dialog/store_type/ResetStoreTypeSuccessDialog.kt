package com.ottokonek.ottokasir.ui.dialog.store_type

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.dialog_added_customer.*

class ResetStoreTypeSuccessDialog(@NonNull context: Context, style: Int, private val callbackResetStoreType: ResetStoreTypeCallback) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_success_reset_store_type)
        setCancelable(false)

        btnOk.setOnClickListener {
            this.dismiss()
            successResetStoreType()
        }
    }

    private fun successResetStoreType() {
        this.dismiss()
        callbackResetStoreType.resetStoreTypeSuccess()
    }

    override fun onBackPressed() {
        successResetStoreType()
        super.onBackPressed()
    }
}